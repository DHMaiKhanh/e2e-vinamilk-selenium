# Vinamilk E2E Test Dashboard

Dashboard React hiển thị kết quả chạy test Selenium (đọc từ `ui-tests/target/allure-results`).

## Cách dùng

1. Chạy test trước: `mvn -pl ui-tests test` (hoặc chạy 1 class cụ thể).
2. Vào thư mục `dashboard/`, chạy:
   ```
   npm install   # chỉ cần lần đầu
   npm run dev
   ```
3. Mở link Vite in ra (mặc định http://localhost:5173).

`npm run dev` tự động chạy `npm run report` trước khi mở Vite — script này đọc
`ui-tests/target/allure-results`, tổng hợp thành `public/report.json`, và copy
ảnh chụp màn hình lúc fail vào `public/screenshots/`.

## Dashboard hiển thị gì

- Tổng số test / Pass / Fail / Broken / Skipped + biểu đồ donut pass rate.
- Danh sách tính năng (feature), mỗi tính năng liệt kê toàn bộ test case (tên,
  mã test case, trạng thái, thời gian chạy).
- Click vào 1 test để xem chi tiết: vị trí fail trong code (file:line), chuỗi
  gọi hàm trong code dự án, thông điệp lỗi, ảnh chụp màn hình lúc fail, và
  stack trace đầy đủ.
- Bộ lọc theo trạng thái (Fail / Broken / Pass / Skipped).

## Ghi chú kỹ thuật

- Ảnh chụp màn hình được khớp với test fail bằng heuristic theo thời gian
  (thời điểm chụp gần nhất với lúc test kết thúc, trong cửa sổ 10s) — vì
  `TestListener` hiện tại gọi `Allure.addAttachment` nhưng không link attachment
  vào đúng test trong `allure-results` (file ảnh tồn tại nhưng orphan). Nếu sửa
  lại listener để link đúng, có thể bỏ heuristic này và đọc trực tiếp field
  `attachments` trong result JSON.
- Không có quay video — dự án hiện chỉ chụp ảnh màn hình khi fail.
