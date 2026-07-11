# Vinamilk QA Automation Framework — Tài liệu luồng dự án & chức năng file

> Framework kiểm thử tự động đa module (Java/Maven) cho nền tảng thương mại điện tử Vinamilk,
> chạy trên các microservices: `user-service`, `product-service`, `order-service`, `payment-service`.
> Bao phủ 4 lớp kiểm thử: **UI (Selenium)**, **API (REST Assured)**, **Event-driven (Kafka)**,
> **Service virtualization (WireMock)** — trong cùng một Maven reactor, một mô hình cấu hình,
> một pipeline CI/CD.

---

## 1. Tại sao chia theo *loại kiểm thử* thay vì theo *tính năng*

Một hệ thống microservices không thể kiểm thử như một khối nguyên (monolith). Cùng một luồng
nghiệp vụ ("đặt hàng") cần được xác minh ở 3 tầng khác nhau: giao diện, hợp đồng API của từng
service, và sự kiện (event) nối các service với nhau. Vì vậy dự án tách theo **loại test**, không
theo trang hay theo tính năng, để mỗi tầng có thể:

- Chạy độc lập / song song trong CI (`-pl api-tests`, `-pl ui-tests`, ...)
- Tăng số lượng test riêng mà không làm chậm các tầng khác
- Chỉ phụ thuộc vào `automation-core`, không phụ thuộc code test của module khác
  (`messaging-tests` tái sử dụng client của `api-tests` như một **thư viện** — scope `test` — để
  kích hoạt luồng nghiệp vụ cần lắng nghe sự kiện, không viết lại logic HTTP)

```
vinamilk-automation-parent (pom)
├── automation-core     # framework dùng chung: config, vòng đời WebDriver, listener, tiện ích
├── ui-tests             # Selenium 4 + Page Object Model, phụ thuộc automation-core
├── api-tests             # REST Assured client/model theo từng microservice, phụ thuộc automation-core
├── messaging-tests       # Kafka producer/consumer + test theo hướng sự kiện
└── mock-services         # WireMock nhúng để cô lập 1 service đang test
```

| Module | Loại test | Phụ thuộc |
|---|---|---|
| `automation-core` | Không có (thư viện dùng chung) | Selenium, Jackson, Log4j2, Allure |
| `ui-tests` | Selenium/TestNG — regression UI | `automation-core` |
| `api-tests` | REST Assured — contract & integration test | `automation-core` |
| `messaging-tests` | Kafka — event-driven | `automation-core`, `api-tests` (scope test) |
| `mock-services` | Test cô lập/stub qua WireMock | `automation-core` |

---

## 2. Luồng chạy tổng thể (end-to-end flow)

```
                ┌────────────────────────┐
                │   ConfigManager         │  ← đọc environments/<env>.properties
                │  (-Denv=dev|staging|... │     + override bằng -D<key>=value
                └───────────┬─────────────┘
                            │
        ┌───────────────────┼────────────────────┬──────────────────┐
        ▼                   ▼                    ▼                  ▼
   ┌──────────┐       ┌───────────┐        ┌─────────────┐   ┌──────────────┐
   │ ui-tests │       │ api-tests │        │messaging-    │   │ mock-services│
   │(Selenium)│       │(REST      │        │tests (Kafka) │   │  (WireMock)  │
   │          │       │ Assured)  │        │              │   │              │
   └────┬─────┘       └─────┬─────┘        └──────┬───────┘   └──────┬───────┘
        │                   │                     │                  │
        ▼                   ▼                     ▼                  ▼
  DriverFactory        RequestSpecFactory    dùng lại OrderServiceClient   WireMockServerManager
  tạo WebDriver         tạo RequestSpec      (từ api-tests) để tạo đơn      khởi động server giả
  (local/Grid)          theo từng service    hàng, rồi lắng nghe Kafka      lập, trả response/lỗi
                                              topic "order-created"          đã định sẵn (JSON)
        │                   │                     │                  │
        └───────────────────┴──────────┬──────────┴──────────────────┘
                                        ▼
                          TestListener / RetryAnalyzer / RetryTransformer
                          (log, chụp ảnh màn hình lỗi, tự retry test flaky)
                                        │
                                        ▼
                         target/allure-results (mỗi module) + surefire-reports
                                        │
                                        ▼
                          mvn io.qameta.allure:allure-maven:report
                                        │
                                        ▼
                         Báo cáo Allure HTML + JUnit XML + screenshots
                          (được Jenkinsfile / Harness pipeline.yml publish)
```

**Vòng đời một lần chạy CI/CD** (giống hệt nhau dù chạy trên Jenkins hay Harness):

1. **Checkout** mã nguồn.
2. **Build & Static Analysis**: `mvn clean install -DskipTests` rồi `mvn checkstyle:check`.
3. **API Tests** (nếu bật): `mvn -pl api-tests -am test -Denv=<env> -DsuiteXmlFile=testng-api.xml`.
4. **UI Tests** (nếu bật): tương tự, thêm `-Dbrowser.headless=true -Dselenium.grid.enabled=true`
   để chạy trên Selenium Grid (từ `docker-compose.yml`).
5. **Messaging Tests** (tuỳ chọn, cần Kafka broker sống): chạy `testng-messaging.xml`.
6. **Publish**: gộp `target/allure-results` của tất cả module thành báo cáo Allure; đính kèm
   JUnit XML + screenshot lỗi.

Bộ điều phối (Jenkins/Harness) **chỉ lên lịch stage**, toàn bộ logic test vẫn nằm trong các lệnh
Maven — nên 2 pipeline luôn tương đương nhau.

---

## 3. Chức năng từng file — theo module

### 3.1. Cấu hình gốc (root)

| File | Chức năng |
|---|---|
| `pom.xml` | Maven reactor cha (`packaging=pom`), khai báo 5 module con, quản lý version tập trung (`dependencyManagement`) cho Selenium, REST Assured, Kafka, WireMock, Jackson, Log4j2, Allure, TestNG; cấu hình chung cho `maven-compiler-plugin`, `maven-surefire-plugin` (đọc biến `-DsuiteXmlFile`), `maven-checkstyle-plugin`. |
| `checkstyle.xml` | Bộ luật style-check dùng chung cho toàn bộ module, chạy ở phase `verify`. |
| `docker-compose.yml` | Hạ tầng local: Selenium Grid (`hub` + node Chrome/Firefox) để chạy UI test song song đa trình duyệt, và Kafka + Zookeeper cho `messaging-tests`. WireMock **không** container hoá vì nó chạy nhúng theo tiến trình test (xem `WireMockServerManager`). |
| `Jenkinsfile` | Pipeline Jenkins dạng declarative: tham số hoá theo môi trường (`TEST_ENV`) và cờ bật/tắt từng suite (`RUN_UI_TESTS`, `RUN_API_TESTS`, `RUN_MESSAGING_TESTS`); publish Allure + JUnit + screenshot lỗi. |
| `ci/harness/pipeline.yml` | Pipeline tương đương cho Harness CI — cùng các stage, cùng lệnh Maven, để có thể chạy trên orchestrator khác mà không viết lại logic test. |
| `ci/docker/Dockerfile` | Image runner cố định Maven 3.9.11 + JDK 17 để build tái lập được, không phụ thuộc agent build có sẵn gì; copy các `pom.xml` trước để cache layer dependency, sau đó copy toàn bộ source. |
| `ci/scripts/run-tests.sh` | Script wrapper cho local, gọi cùng các lệnh Maven như CI. Cú pháp: `run-tests.sh [dev|staging|prod] [ui|api|messaging|all]`. |
| `README.md` | Tài liệu tổng quan chính thức của dự án (kiến trúc, cách chạy, cách thêm microservice mới). |

### 3.2. `automation-core` — framework dùng chung (không chứa test)

| File | Chức năng |
|---|---|
| `config/ConfigManager.java` | Singleton đọc `environments/<env>.properties` (chọn qua `-Denv=` hoặc biến môi trường `TEST_ENV`, mặc định `dev`); mọi key đều override được bằng `-D<key>=value` khi chạy Maven. Là **nguồn cấu hình duy nhất** cho toàn framework. |
| `driver/DriverFactory.java` | Tạo `WebDriver` theo cấu hình (`browser=chrome/firefox/edge`, `browser.headless`, `selenium.grid.enabled`) — driver local (qua WebDriverManager tự tải binary) hoặc `RemoteWebDriver` trỏ tới Selenium Grid. |
| `driver/DriverManager.java` | Giữ `WebDriver` theo `ThreadLocal`, đảm bảo các test chạy song song (`parallel="classes"` trong testng.xml) không share session trình duyệt giữa các thread. |
| `driver/BrowserType.java` | Enum `CHROME/FIREFOX/EDGE`, parse an toàn từ chuỗi cấu hình. |
| `exceptions/FrameworkException.java` | Runtime exception dùng chung khi cần báo lỗi framework có ngữ cảnh rõ ràng. |
| `listeners/RetryAnalyzer.java` | Cho phép 1 test fail được retry tối đa 2 lần trước khi coi là fail thật — hấp thụ độ "flaky" vốn có của UI/browser timing. |
| `listeners/RetryTransformer.java` | Đăng ký trong `<listener>` của mỗi `testng.xml`, tự động gắn `RetryAnalyzer` cho **mọi** `@Test` mà không cần khai báo `retryAnalyzer=...` thủ công từng test. |
| `listeners/TestListener.java` | Log vòng đời test (start/pass/fail/skip); khi fail và có `WebDriver` đang chạy trên thread đó, tự động chụp ảnh màn hình và đính kèm vào báo cáo Allure (no-op với suite không phải UI). |
| `utils/JsonUtils.java` | Đọc JSON từ classpath thành object (`fromClasspath`), serialize/deserialize JSON (`toJson`/`fromJson`) dùng Jackson `ObjectMapper`. |
| `utils/ScreenshotUtils.java` | Chụp màn hình dạng `byte[]` (để đính kèm Allure) hoặc lưu ra file `target/screenshots/<test>-<timestamp>.png`. |
| `utils/WaitUtils.java` | Bọc `WebDriverWait` (mặc định 15s) cho 3 điều kiện hay dùng nhất: chờ hiển thị, chờ click được, chờ biến mất — Page Object dùng thay vì gọi Selenium wait trực tiếp. |
| `resources/environments/{dev,staging,prod}.properties` | Giá trị cấu hình theo môi trường: base URL của UI, base URL từng microservice API, Kafka broker/topic/consumer-group, cổng WireMock. |
| `resources/log4j2.xml` | Cấu hình logging: ghi ra console + file xoay vòng `target/logs/automation.log` (giới hạn 20MB/file, giữ tối đa 10 file). |

### 3.3. `ui-tests` — Selenium 4 + Page Object Model

**Page Objects** (`src/main/java/.../ui/pages`):

| File | Chức năng |
|---|---|
| `BasePage.java` | Lớp cha trừu tượng cho mọi Page Object: các thao tác chung `click`, `type`, `textOf`, `isDisplayed`, tất cả đều thông qua `WaitUtils` (tự chờ phần tử sẵn sàng, tránh flaky do timing). |
| `LoginPage.java` | Đăng nhập (`loginAs(email, password)` → trả về `HomePage`), đọc thông báo lỗi đăng nhập. |
| `HomePage.java` | Tìm kiếm sản phẩm (`searchProduct` → `ProductListingPage`), mở giỏ hàng (`openCart` → `CartPage`). |
| `ProductListingPage.java` | Đếm số sản phẩm trong kết quả tìm kiếm, mở sản phẩm đầu tiên (`openFirstProduct` → `ProductDetailPage`). |
| `ProductDetailPage.java` | Lấy tên sản phẩm, thêm vào giỏ (`addToCart` → `CartPage`). |
| `CartPage.java` | Đếm số dòng sản phẩm trong giỏ, tiến hành thanh toán (`proceedToCheckout`). |
| `components/HeaderComponent.java` | Component tái sử dụng cho phần header/logo xuất hiện trên nhiều trang, tách riêng khỏi từng `Page` cụ thể để có thể ghép vào bất kỳ trang nào render nó. |

**Test & hạ tầng test** (`src/test/java/.../ui`):

| File | Chức năng |
|---|---|
| `base/BaseUiTest.java` | Lớp cha cho mọi UI test: `@BeforeMethod` tạo `WebDriver` qua `DriverFactory` + gán vào `DriverManager`, maximize cửa sổ; `@AfterMethod` gọi `DriverManager.quitDriver()`. Gắn sẵn `TestListener`. |
| `tests/LoginTest.java` | 2 test: đăng nhập thành công với tài khoản hợp lệ (redirect về HomePage); đăng nhập sai bị từ chối và hiển thị thông báo lỗi chứa "invalid". |
| `tests/ProductSearchTest.java` | Tìm kiếm theo từ khoá SKU thực tế ("Sua Tuoi Vinamilk 100%") → khẳng định có ít nhất 1 kết quả. |
| `tests/AddToCartTest.java` | Luồng: tìm kiếm → mở sản phẩm đầu tiên → thêm vào giỏ → khẳng định giỏ hàng có ít nhất 1 sản phẩm. |
| `resources/testng-ui.xml` | Suite TestNG: chạy song song theo `classes` với 3 thread, đăng ký listener `RetryTransformer` + `AllureTestNg`, liệt kê 3 class test ở trên. |
| `resources/test-data/users.json` | Dữ liệu test: tài khoản hợp lệ (`registered_customer`) và tài khoản sai (`invalid_user`) dùng cho `LoginTest`. |

### 3.4. `api-tests` — REST Assured, client + model theo từng microservice

| File | Chức năng |
|---|---|
| `config/ApiConfig.java` | Lấy base URL của từng service qua `ApiConfig.ServiceName` (`USER/PRODUCT/ORDER/PAYMENT`), lấy token xác thực (`api.auth.token`). |
| `specs/RequestSpecFactory.java` | Tạo `RequestSpecification` riêng cho từng service: base URI, `Content-Type: application/json`, filter Allure (`AllureRestAssured`), tự thêm header `Authorization: Bearer <token>` nếu có — nhờ vậy các client HTTP chỉ còn là 1 dòng gọi. |
| `clients/UserServiceClient.java` | Gọi `GET /users/{userId}`, `POST /users` tới `user-service`. |
| `clients/ProductServiceClient.java` | Gọi `GET /products/{sku}`, `GET /products?q=` (tìm kiếm) tới `product-service`. |
| `clients/OrderServiceClient.java` | Gọi `POST /orders` (tạo đơn), `GET /orders/{orderId}` tới `order-service`. |
| `models/request/CreateOrderRequest.java` | Payload tạo đơn hàng: `customerId`, `shippingAddress`, danh sách `OrderLineItem` (sku + quantity). |
| `models/request/LoginRequest.java` | Payload đăng nhập API (email/password). |
| `models/response/UserResponse.java` | Model response của `user-service` (id, email, fullName, status). |
| `models/response/ProductResponse.java` | Model response của `product-service` (id, sku, name, price, stockQuantity). |
| `models/response/OrderResponse.java` | Model response của `order-service` (id, customerId, status, totalAmount). |
| `base/BaseApiTest.java` | Lớp cha cho API test, chỉ gắn listener `AllureTestNg`. |
| `tests/user/UserServiceTests.java` | Kiểm tra `GET /users/{id}` trả 200 đúng field, và trả 404 khi user không tồn tại. |
| `tests/product/ProductServiceTests.java` | Kiểm tra response `GET /products/{sku}` khớp JSON Schema (`product-schema.json`) — phát hiện contract bị lệch dù status code vẫn 200; kiểm tra tìm kiếm sản phẩm trả về kết quả. |
| `tests/order/OrderServiceTests.java` | Test **tích hợp thật giữa các service** (không mock): tạo đơn hàng qua `order-service`, rồi xác minh tồn kho ở `product-service` giảm đúng số lượng đã đặt — chứng minh 2 service phối hợp đúng qua HTTP thật (đối lập với `mock-services`, nơi phụ thuộc bị stub). |
| `resources/schemas/product-schema.json` | JSON Schema (draft-07) mô tả hợp đồng bắt buộc của `ProductResponse` (id, sku, name, price bắt buộc). |
| `resources/testng-api.xml` | Suite TestNG: chạy song song theo `methods`, 5 thread; đăng ký `AllureTestNg`; liệt kê 3 class test API. |

### 3.5. `messaging-tests` — Kafka, kiểm thử theo hướng sự kiện

| File | Chức năng |
|---|---|
| `kafka/KafkaTestConfig.java` | Tạo `Properties` cho Kafka producer/consumer từ `ConfigManager` (bootstrap servers, group id có hậu tố riêng theo test, `auto.offset.reset=earliest`), và cung cấp tên topic `order-created`. |
| `kafka/KafkaProducerClient.java` | Wrapper gửi message Kafka đồng bộ (`publish`, chờ ack tối đa 10s), implement `AutoCloseable`. |
| `kafka/KafkaConsumerClient.java` | Wrapper subscribe 1 topic và `awaitMessage(predicate, timeout)` — poll liên tục tới khi tìm được message khớp điều kiện hoặc hết thời gian chờ. |
| `tests/OrderEventKafkaTest.java` | Test then chốt của module: **tái sử dụng `OrderServiceClient` từ `api-tests`** để tạo đơn hàng thật qua API, sau đó lắng nghe topic `order-created-events` và xác minh event phát ra chứa đúng `customerId` và `status=CREATED`. Đây là minh chứng "microservice có thực sự phát sự kiện mà service khác cần" — điều mà test UI/API đơn thuần không thấy được. |
| `resources/testng-messaging.xml` | Suite TestNG chạy tuần tự (`parallel="none"`, vì phụ thuộc trạng thái Kafka topic), 1 class test. |

### 3.6. `mock-services` — WireMock nhúng, cô lập service khi test

| File | Chức năng |
|---|---|
| `WireMockServerManager.java` | Khởi động **một** `WireMockServer` nhúng mỗi JVM (cổng lấy từ `wiremock.port`, mặc định 8089), phục vụ stub từ `src/main/resources/wiremock`; test trỏ client vào server này thay vì service thật. |
| `tests/OrderServiceIsolationTest.java` | Ví dụ: bật WireMock trước class (`@BeforeClass`), gọi `GET /products/SKU-VNM-STV-100` vào server giả, xác minh response khớp fixture đã định nghĩa sẵn — không cần `product-service` thật đang chạy. |
| `resources/wiremock/mappings/get-product-200.json` | Stub mapping: `GET /products/SKU-VNM-STV-100` → trả 200 với body lấy từ `product-response.json`. |
| `resources/wiremock/mappings/payment-service-timeout.json` | Stub mô phỏng lỗi hạ tầng: `POST /payments/authorize` → trễ 5 giây rồi ngắt kết nối (`CONNECTION_RESET_BY_PEER`) — dùng để test khả năng chịu lỗi/timeout của consumer mà không cần dựng lỗi thật trên `payment-service`. |
| `resources/wiremock/__files/product-response.json` | Fixture JSON trả về cho stub `get-product-200.json` (sku, name, price, stockQuantity cố định). |

### 3.7. Các thư mục phụ trợ khác

| File/thư mục | Chức năng |
|---|---|
| `.github/modernize/java-upgrade/hooks/scripts/recordToolUse.{ps1,sh}` | Script hook ghi log việc dùng tool, phục vụ công cụ hỗ trợ nâng cấp Java (Java-upgrade tooling) của GitHub — không thuộc logic test, chỉ phục vụ quá trình modernize codebase. |
| `.gitignore` | Loại trừ output build (`target/`, log, report) khỏi version control. |
| `*/target/**` | Toàn bộ artefact sinh ra khi build/test: file `.class`, báo cáo Surefire/Allure, log — **không phải mã nguồn**, sẽ bị xoá/tạo lại mỗi lần `mvn clean`/`mvn test`. |

---

## 4. Cách chạy nhanh (tóm tắt từ README)

```bash
# Hạ tầng local (tuỳ chọn — chỉ cần khi chạy qua Grid/broker)
docker-compose up -d

# API contract & integration test
mvn -pl api-tests -am test -DsuiteXmlFile=src/test/resources/testng-api.xml

# UI regression
mvn -pl ui-tests -am test -DsuiteXmlFile=src/test/resources/testng-ui.xml

# Kafka event-driven test (cần broker sống)
mvn -pl messaging-tests -am test -DsuiteXmlFile=src/test/resources/testng-messaging.xml

# Test cô lập qua WireMock (không cần dependency ngoài — WireMock chạy nhúng)
mvn -pl mock-services -am test

# Gộp báo cáo Allure từ tất cả module
mvn io.qameta.allure:allure-maven:report
```

Hoặc dùng script có sẵn: `ci/scripts/run-tests.sh <dev|staging|prod> <ui|api|messaging|all>`.

## 5. Muốn thêm 1 microservice mới thì sửa ở đâu

1. Thêm base URL vào từng `environments/*.properties`.
2. Thêm entry vào `ApiConfig.ServiceName` (`api-tests`).
3. Tạo `<Service>Client` + model request/response tương ứng trong `api-tests`.
4. Nếu service tham gia luồng đặt hàng, thêm schema sự kiện nó publish vào
   `messaging-tests`/`mock-services` nếu cần.
