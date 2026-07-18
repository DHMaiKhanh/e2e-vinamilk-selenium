# Luồng code Dashboard (`dashboard/`)

Dashboard là một app React (Vite) **chỉ đọc** kết quả test, không tự chạy test.
Toàn bộ dữ liệu đi qua một pipeline 3 bước:

```
mvn test (ui-tests, Allure listener)
        │  ghi ra
        ▼
ui-tests/target/allure-results/*.json  (+ *-attachment ảnh screenshot)
        │  đọc & tổng hợp bởi
        ▼
dashboard/scripts/build-report.mjs
        │  ghi ra
        ▼
dashboard/public/report.json  +  dashboard/public/screenshots/*.png
        │  fetch bởi
        ▼
dashboard/src/App.jsx  →  render UI
```

## 1. Nguồn dữ liệu: Allure results

`ui-tests/pom.xml` khai báo dependency `allure-testng`, và
`testng-ui.xml` gắn listener `io.qameta.allure.testng.AllureTestNg`. Khi chạy
`mvn -pl ui-tests test`, mỗi test case sinh ra 1 file
`<uuid>-result.json` trong `ui-tests/target/allure-results/`, cộng thêm các
file `*-attachment` (ảnh chụp màn hình lúc fail, do `TestListener` gọi
`Allure.addAttachment`).

Do TestNG re-run test flaky (qua `RetryTransformer`, tối đa 2 lần retry), một
test case có thể có nhiều `*-result.json` với cùng `historyId`.

## 2. Script tổng hợp: `dashboard/scripts/build-report.mjs`

Chạy bằng `node scripts/build-report.mjs` (script `report` trong
`package.json`). Các bước chính trong `main()`:

1. **Đọc toàn bộ `*-result.json`** trong `allure-results`.
2. **Dedupe theo `historyId`** — chỉ giữ attempt có `stop` (thời điểm kết
   thúc) lớn nhất, để 1 test case = 1 dòng trên dashboard dù có retry. Số lần
   thử được lưu lại vào `attemptCount`.
3. **Khớp ảnh screenshot với test fail bằng heuristic thời gian**: vì
   attachment trong `allure-results` hiện không được link đúng vào test
   (`TestListener` ghi file nhưng orphan), script so khớp mỗi test
   failed/broken với file `*-attachment` có `mtime` gần `stop` nhất (trong cửa
   sổ 10 giây), copy ảnh đó vào `public/screenshots/<uuid>.png`.
4. **Trích xuất vị trí fail trong code** (`extractFailLocation`,
   `extractProjectFrames`): parse stack trace, tìm dòng đầu tiên thuộc package
   `com.vinamilk.automation` — đây là điểm fail gần nhất trong code dự án
   (không phải trong thư viện Selenium/TestNG).
5. **Gom nhóm theo feature**: dùng Allure label `feature` (hoặc `testClass`
   nếu thiếu) làm key, mỗi feature chứa danh sách test kèm trạng thái, thời
   gian chạy, message lỗi, stack trace, ảnh, v.v.
6. **Tính tổng hợp** (`summary`): tổng số test, số pass/fail/broken/skipped,
   `passRate`.
7. **Ghi ra `dashboard/public/report.json`** — đây là file JSON duy nhất mà
   frontend đọc.

Nếu chưa có `ui-tests/target/allure-results` (chưa chạy test lần nào), script
thoát với lỗi rõ ràng thay vì sinh report rỗng.

## 3. Frontend: React (Vite)

- `src/main.jsx` — entry point, mount `<App />`.
- `src/App.jsx` — component gốc:
  - `useEffect` fetch `report.json` (đường dẫn `${BASE_URL}report.json`, tức
    file tĩnh do Vite serve từ `public/`).
  - Giữ state `filter` (all/failed/broken/passed/skipped) và `selectedTest`
    (test đang xem chi tiết).
  - Nếu fetch lỗi → hiển thị hướng dẫn chạy `npm run report` / `mvn test`.
- `src/components/SummaryCards.jsx` — 5 thẻ số liệu tổng (Total/Pass/Fail/
  Broken/Skipped) từ `report.summary`.
- `src/components/DonutChart.jsx` — biểu đồ donut tỉ lệ pass/fail/broken/
  skipped.
- `src/components/FeatureList.jsx` — danh sách feature dạng accordion; mỗi
  feature liệt kê test case (tên, mã `TC-...`, icon trạng thái, thời gian
  chạy); click 1 dòng test gọi `onSelectTest(t)` để mở chi tiết.
- `src/components/TestDetail.jsx` — panel/modal chi tiết 1 test: vị trí fail
  (`failLocation`), chuỗi gọi hàm trong code dự án (`projectFrames`), thông
  điệp lỗi, ảnh chụp màn hình (`screenshot`), stack trace đầy đủ.

Không có state quản lý toàn cục (Redux/Context) — toàn bộ dữ liệu là một cây
JSON tĩnh (`report.json`) được load 1 lần khi mở trang; muốn xem kết quả mới
phải chạy lại `npm run report` (hoặc `npm run dev`, việc này đã bao gồm bước
report) rồi reload trang.

## 4. Build/serve

- `npm run dev` = `npm run report && vite` — build report rồi mở dev server có
  hot reload.
- `npm run build` = `npm run report && vite build` — build bản tĩnh (dùng khi
  muốn deploy dashboard như 1 site tĩnh, ví dụ publish artifact CI).
- `npm run preview` — serve bản đã build.

## Xem thêm

- Chạy toàn bộ test + mở dashboard bằng 1 lệnh:
  [docs/run-dashboard.md](./run-dashboard.md).
- Lệnh Maven chạy từng feature riêng lẻ: [docs/run-commands.md](./run-commands.md).
