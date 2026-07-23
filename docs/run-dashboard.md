# Chạy toàn bộ test và xem dashboard bằng 1 lệnh

## Lệnh duy nhất

**Windows (PowerShell)** — từ thư mục gốc repo:

```powershell
.\ci\scripts\run-dashboard.ps1
```

**Git Bash / macOS / Linux:**

```bash
./ci/scripts/run-dashboard.sh
```

Lệnh này sẽ tự động, theo thứ tự:

1. `mvn clean install -DskipTests` — build toàn bộ module (kể cả
   `automation-core`) nhưng bỏ qua test, vì `automation-core` không có suite
   test riêng và sẽ làm build fail nếu không skip.
2. Chạy toàn bộ suite `ui-tests` (`testng-ui.xml`: HomePageTest, LoginTest,
   ProductSearchTest, AddToCartTest) với `mvn -pl ui-tests test`, dùng
   `-Dmaven.test.failure.ignore=true` để test fail không làm dừng script —
   test fail chính là thứ dashboard cần hiển thị.
3. `cd dashboard` và `npm install` nếu đây là lần chạy đầu tiên (chưa có
   `node_modules`).
4. Chạy `npm run dev`, tự tổng hợp `ui-tests/target/allure-results` thành
   `dashboard/public/report.json` rồi mở Vite dev server (mặc định
   http://localhost:5173) hiển thị dashboard.

Mở link Vite in ra ở cuối log (thường là `http://localhost:5173/`) để xem
dashboard.

## Tham số tuỳ chọn

| Script | Tham số 1 | Tham số 2 |
|---|---|---|
| `run-dashboard.sh` | `env` (`dev`\|`staging`\|`prod`, mặc định `prod`) | `headless` (`true`\|`false`, mặc định `false`) |
| `run-dashboard.ps1` | `-TestEnv` (mặc định `prod`) | `-Headless` (mặc định `false`) |

Ví dụ chạy trên môi trường staging, ẩn trình duyệt:

```bash
./ci/scripts/run-dashboard.sh staging true
```

```powershell
.\ci\scripts\run-dashboard.ps1 -TestEnv staging -Headless true
```

## Chỉ muốn xem lại dashboard mà không chạy test lại

Nếu `ui-tests/target/allure-results` đã tồn tại từ lần chạy trước, chỉ cần:

```bash
cd dashboard
npm run dev
```

## Chạy Maven trực tiếp (không qua script, không tự mở dashboard)

Dùng khi chỉ muốn chạy test suite bằng Maven thuần, tự kiểm soát từng bước
(không build report/dashboard tự động như script).

**Windows (PowerShell)** — từ thư mục gốc repo:

```powershell
mvn -B -ntp clean install -DskipTests
mvn -B -ntp -pl ui-tests test `
  "-Dmaven.test.failure.ignore=true" `
  "-Denv=prod" `
  "-Dselenium.grid.enabled=false" `
  "-Dbrowser.headless=false"
```

- `-Denv=prod`: đổi thành `staging` hoặc `dev` để chạy môi trường khác.
- `-Dbrowser.headless=false`: mở Chrome để quan sát trực tiếp; đổi thành `true`
  để chạy ẩn.

Sau khi chạy xong, muốn xem dashboard riêng (không chạy lại test):

```bash
cd dashboard
npm run dev
```

## Chạy dạng chạy ngầm (background)

Dùng khi muốn khởi động test + dashboard mà không giữ terminal, hoặc để tiếp
tục làm việc khác trong lúc test chạy.

**Windows (PowerShell) — `Start-Job`:**

```powershell
$job = Start-Job -ScriptBlock { & .\ci\scripts\run-dashboard.ps1 -TestEnv prod }
# Xem log:      Receive-Job $job -Keep
# Kiểm tra trạng thái: Get-Job $job
# Dừng job:      Stop-Job $job; Remove-Job $job
```

**Windows (PowerShell) — `Start-Process` (chạy ẩn cửa sổ, ghi log ra file):**

```powershell
Start-Process powershell -ArgumentList "-NoProfile", "-File", ".\ci\scripts\run-dashboard.ps1", "-TestEnv", "prod" `
  -WindowStyle Hidden -RedirectStandardOutput ".\run-dashboard-prod.log" -RedirectStandardError ".\run-dashboard-prod.err.log"
# Xem log:   Get-Content .\run-dashboard-prod.log -Wait
```

**Git Bash / macOS / Linux:**

```bash
./ci/scripts/run-dashboard.sh prod > run-dashboard-prod.log 2>&1 &
# Xem log:      tail -f run-dashboard-prod.log
# Kiểm tra job:  jobs
# Đưa lại foreground: fg
```

Log của lần chạy được ghi vào file vì tiến trình không còn gắn với terminal
hiện tại. Dashboard Vite dev server chỉ start sau khi bước `mvn test` hoàn
tất — kiểm tra log để biết khi nào link `http://localhost:5173` sẵn sàng.

## Ghi chú

- Muốn chạy 1 class test cụ thể rồi tự build report thủ công, xem lệnh Maven
  từng feature tại [docs/run-commands.md](./run-commands.md).
- Chi tiết cách dashboard đọc/hiển thị dữ liệu: xem
  [docs/dashboard-architecture.md](./dashboard-architecture.md).
- Yêu cầu môi trường: đã cài Maven + JDK 17, Node.js (để chạy `npm`), và Chrome
  (Selenium mở trình duyệt thật khi `headless=false`).
- Mặc định cả hai script đều chạy trên môi trường **prod** (base URL production
  của Vinamilk) — không cần truyền thêm tham số nếu muốn test trên prod.

## Troubleshooting

- **`Could not find the selected project in the reactor`**: script gọi
  `mvn -pl ui-tests` sau khi đã `cd` vào thư mục gốc repo. Nếu bạn tự gõ lệnh
  `mvn` thủ công (không qua script), phải đứng đúng ở thư mục gốc repo trước
  khi chạy, không truyền đường dẫn tuyệt đối cho `-pl`.
- **Dashboard hiển thị dữ liệu cũ**: nếu bước `mvn test` fail trước khi chạy
  xong (ví dụ lỗi cấu hình ở trên), bước build report vẫn đọc
  `ui-tests/target/allure-results` của **lần chạy trước đó**. Kiểm tra log của
  bước 1 để chắc chắn test đã chạy thành công trước khi tin vào số liệu trên
  dashboard.
- **Tất cả test đều Broken/Skipped, 0 Pass (`WebDriverException: net::ERR_NAME_NOT_RESOLVED`)**:
  thường do `-Denv=staging` trỏ tới `staging.vinamilk.com.vn` — hostname này
  không resolve được từ máy đang chạy (không phải domain public thật). Chạy
  lại với `-TestEnv prod` (base URL `www.vinamilk.com.vn`, domain thật) để
  xác nhận.
- **Trang production trả về 403 khi curl/kiểm tra thủ công**: `www.vinamilk.com.vn`
  có WAF/bot-detection chặn theo User-Agent — request không có User-Agent
  giống trình duyệt thật (`curl` mặc định) sẽ bị 403, nhưng ChromeDriver gửi
  User-Agent Chrome thật nên không bị ảnh hưởng. Không cần chỉnh gì thêm cho
  Selenium vì lý do này.
