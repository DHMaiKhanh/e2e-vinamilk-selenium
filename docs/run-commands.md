# Run Commands

File này được duy trì tự động bởi skill `record-run-commands`, liệt kê lệnh Maven để chạy từng feature test trong `ui-tests`, cũng như lệnh chạy dashboard cho từng môi trường.

## Home Page (HomePageTest)

```bash
mvn -pl ui-tests test "-Dtest=HomePageTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Dtest=HomePageTest" "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

- `env=prod`: chạy trên môi trường production của Vinamilk.
- `env=staging`: chạy trên môi trường staging.
- `browser.headless=false`: mở trình duyệt có giao diện (không chạy ẩn).

## Authentication (LoginTest)

```bash
mvn -pl ui-tests test "-Dtest=LoginTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Dtest=LoginTest" "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Product Search (ProductSearchTest)

```bash
mvn -pl ui-tests test "-Dtest=ProductSearchTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Dtest=ProductSearchTest" "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Shopping Cart (AddToCartTest)

```bash
mvn -pl ui-tests test "-Dtest=AddToCartTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Dtest=AddToCartTest" "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Product Listing (ProductListingPageTest)

```bash
mvn -pl ui-tests test "-Dtest=ProductListingPageTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Dtest=ProductListingPageTest" "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Chạy toàn bộ test suite

```bash
mvn -pl ui-tests test "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

```bash
mvn -pl ui-tests test "-Denv=staging" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Chạy toàn bộ test + dashboard bằng 1 lệnh

Xem chi tiết đầy đủ tại [docs/run-dashboard.md](./run-dashboard.md). Tóm tắt:

**Windows (PowerShell):**

```powershell
# Production (mặc định, không cần truyền tham số)
.\ci\scripts\run-dashboard.ps1

# Production, tường minh
.\ci\scripts\run-dashboard.ps1 -TestEnv prod

# Staging
.\ci\scripts\run-dashboard.ps1 -TestEnv staging

# Staging, chạy ẩn trình duyệt (headless)
.\ci\scripts\run-dashboard.ps1 -TestEnv staging -Headless true

# Production, chạy ẩn trình duyệt (headless)
.\ci\scripts\run-dashboard.ps1 -TestEnv prod -Headless true
```

**Git Bash / macOS / Linux:**

```bash
# Production (mặc định, không cần tham số)
./ci/scripts/run-dashboard.sh

# Production, tường minh
./ci/scripts/run-dashboard.sh prod

# Staging
./ci/scripts/run-dashboard.sh staging

# Staging, chạy ẩn trình duyệt (headless)
./ci/scripts/run-dashboard.sh staging true

# Production, chạy ẩn trình duyệt (headless)
./ci/scripts/run-dashboard.sh prod true
```

Mỗi lệnh trên tự động: build (`mvn clean install -DskipTests`), chạy toàn bộ suite `ui-tests` trên
môi trường được chọn, rồi tổng hợp `ui-tests/target/allure-results` thành `dashboard/public/report.json`
và mở dashboard qua Vite dev server (mặc định http://localhost:5173).

## Chạy production dạng chạy ngầm (background)

Dùng khi muốn khởi động test + dashboard trên production mà không giữ terminal, hoặc để tiếp tục
làm việc khác trong lúc test chạy.

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

- Log của lần chạy được ghi vào file (`run-dashboard-prod.log`) vì tiến trình không còn gắn với
  terminal hiện tại — mở file này để theo dõi build/test/dashboard.
- Dashboard Vite dev server vẫn chỉ start sau khi bước `mvn test` hoàn tất; kiểm tra log để biết
  khi nào link `http://localhost:5173` sẵn sàng.

## Common flags

| Flag | Ý nghĩa |
|---|---|
| `-Dtest=<ClassName>` | Chỉ chạy class test này (bỏ qua để chạy tất cả). |
| `-Denv=<prod\|staging\|dev>` | Môi trường target, quyết định base URL. |
| `-Dselenium.grid.enabled=<true\|false>` | `true`: chạy qua Selenium Grid; `false`: chạy driver local. |
| `-Dbrowser.headless=<true\|false>` | `true`: chạy ẩn (không mở cửa sổ browser); `false`: mở browser để xem trực tiếp. |

## Common dashboard script params

| Script | Tham số 1 (env) | Tham số 2 (headless) |
|---|---|---|
| `ci/scripts/run-dashboard.sh` | `dev`\|`staging`\|`prod` (mặc định `prod`) | `true`\|`false` (mặc định `false`) |
| `ci/scripts/run-dashboard.ps1` | `-TestEnv` (mặc định `prod`) | `-Headless` (mặc định `false`) |
