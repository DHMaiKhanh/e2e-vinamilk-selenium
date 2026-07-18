# Trang danh sách sản phẩm (Product Listing) — Tổng hợp tính năng & Test Cases

- URL: https://www.vinamilk.com.vn/collections/all-products
- Ngày quét: 2026-07-14
- Phương pháp: MCP Playwright (`browser_navigate` + `browser_snapshot`), có tương tác click filter "Danh mục", checkbox lọc, dropdown "Xếp theo:", nút "Chọn địa chỉ của bạn", nút "Open product options" trên sản phẩm đầu tiên.

## Mục lục

1. [Banner chọn khu vực giao hàng](#1-banner-chọn-khu-vực-giao-hàng)
2. [Breadcrumb & tiêu đề trang, đếm số lượng sản phẩm](#2-breadcrumb--tiêu-đề-trang-đếm-số-lượng-sản-phẩm)
3. [Bộ lọc sản phẩm (Danh mục, Dòng sản phẩm, Thương hiệu, Hương vị, Thể tích/Khối lượng, Nhu cầu dinh dưỡng, Mức đường, Phương thức giao hàng)](#3-bộ-lọc-sản-phẩm)
4. [Sắp xếp sản phẩm (dropdown "Xếp theo:")](#4-sắp-xếp-sản-phẩm-dropdown-xếp-theo)
5. [Lưới sản phẩm & thẻ thông tin sản phẩm](#5-lưới-sản-phẩm--thẻ-thông-tin-sản-phẩm)
6. [Badge chứng nhận chất lượng](#6-badge-chứng-nhận-chất-lượng)
7. [Cảnh báo giới hạn giao hàng bán kính 10km (kem/sữa chua)](#7-cảnh-báo-giới-hạn-giao-hàng-bán-kính-10km-kemsữa-chua)
8. [Mua nhanh từ danh sách (Open product options → chọn quy cách → Thêm vào giỏ)](#8-mua-nhanh-từ-danh-sách-open-product-options--chọn-quy-cách--thêm-vào-giỏ)
9. [Phân trang](#9-phân-trang)
10. [Click vào sản phẩm để xem chi tiết](#10-click-vào-sản-phẩm-để-xem-chi-tiết)

## 1. Banner chọn khu vực giao hàng

### Mô tả tính năng

Ngay đầu vùng nội dung chính (trên breadcrumb) hiển thị dải thông báo: "Chọn khu vực giao hàng để xem sản phẩm khả dụng và đảm bảo nhận hàng chính xác nhất", kèm nút "Chọn địa chỉ của bạn" và nút đóng (X) riêng cho dải banner. Click nút "Chọn địa chỉ của bạn" mở dialog "Chọn địa chỉ của bạn" (modal `role=dialog`) gồm: heading, nút đóng, nhãn "Địa chỉ giao hàng", textbox "Nhập số nhà + tên đường để tìm kiếm". Dialog đóng lại khi click nút X.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-01 | Hiển thị banner chọn khu vực giao hàng khi vào trang | Chưa chọn địa chỉ giao hàng trước đó | Truy cập `/collections/all-products` | Banner "Chọn khu vực giao hàng..." hiển thị cùng nút "Chọn địa chỉ của bạn" | Medium |
| TC-PL-02 | Mở dialog chọn địa chỉ | Trang danh sách sản phẩm đã tải | Click nút "Chọn địa chỉ của bạn" | Dialog "Chọn địa chỉ của bạn" mở ra với textbox tìm kiếm địa chỉ | High |
| TC-PL-03 | Đóng dialog chọn địa chỉ | Dialog "Chọn địa chỉ của bạn" đang mở | Click nút đóng (X) trên dialog | Dialog đóng lại, quay về trang danh sách sản phẩm | Medium |

## 2. Breadcrumb & tiêu đề trang, đếm số lượng sản phẩm

### Mô tả tính năng

Vùng breadcrumb hiển thị đường dẫn Trang chủ ("/") → "Sản phẩm". Bên dưới là heading `<h1>` "Tất cả sản phẩm" cùng số đếm tổng sản phẩm (mặc định "363") đặt ngay cạnh heading. Số đếm này cập nhật động theo bộ lọc đang áp dụng (ví dụ lọc theo danh mục "kem" số đếm đổi thành "58").

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-04 | Hiển thị đúng breadcrumb và tiêu đề trang | Truy cập trang danh sách sản phẩm | Quan sát vùng breadcrumb và heading | Breadcrumb hiển thị "/" → "Sản phẩm"; heading "Tất cả sản phẩm" hiển thị kèm số lượng sản phẩm (vd. 363) | Medium |
| TC-PL-05 | Số đếm sản phẩm cập nhật theo bộ lọc | Trang danh sách sản phẩm đã tải, số đếm ban đầu là 363 | Áp dụng bộ lọc danh mục "kem" | Số đếm cạnh heading đổi thành số lượng khớp với danh mục đã lọc (vd. 58) | High |

## 3. Bộ lọc sản phẩm

### Mô tả tính năng

Thanh filter ngang gồm 8 nút mở dropdown: "Danh mục", "Dòng sản phẩm", "Thương hiệu", "Hương vị", "Thể tích / Khối lượng", "Nhu cầu dinh dưỡng", "Mức đường", "Phương thức giao hàng". Click một nút mở `region` cùng tên chứa danh sách checkbox (mỗi checkbox có label kèm số lượng sản phẩm phù hợp trong ngoặc, vd. "kem (58)", "sữa bột trẻ em (54)"...). Danh mục quan sát được (16 mục): kem, sữa bột trẻ em, sữa tươi, sữa chua uống, nước giải khát, sữa chua ăn, sữa dinh dưỡng, sữa đặc, bột ăn dặm, sữa thực vật, sữa bột người lớn, sữa trái cây, thạch phô mai que, sữa chua sấy thăng hoa, đường & phô mai, đậu đỏ. Khi tick 1 checkbox: URL chuyển sang dạng `?categories=["kem"]`, số đếm tổng cập nhật, danh sách sản phẩm lọc lại theo đúng danh mục, và nút "Xoá bộ lọc" xuất hiện phía trên thanh filter kèm badge số lượng bộ lọc đang áp dụng trên nút "Danh mục" (vd. "Danh mục (1)"). Click "Xoá bộ lọc" gỡ toàn bộ điều kiện lọc.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-06 | Mở dropdown bộ lọc "Danh mục" | Trang danh sách sản phẩm đã tải | Click nút "Danh mục" | Dropdown "Danh mục" mở, hiển thị danh sách checkbox danh mục kèm số lượng | High |
| TC-PL-07 | Áp dụng bộ lọc theo 1 danh mục | Dropdown "Danh mục" đang mở | Tick checkbox "kem (58)" | URL cập nhật `categories=["kem"]`; số đếm tổng đổi thành 58; danh sách sản phẩm chỉ còn sản phẩm thuộc danh mục kem; nút "Xoá bộ lọc" xuất hiện | Critical |
| TC-PL-08 | Xoá bộ lọc đang áp dụng | Đã áp dụng bộ lọc "kem" | Click nút "Xoá bộ lọc" | Toàn bộ điều kiện lọc bị gỡ, danh sách trở về đầy đủ 363 sản phẩm, nút "Xoá bộ lọc" biến mất | High |
| TC-PL-09 | Hiển thị đủ các nhóm bộ lọc khác | Trang danh sách sản phẩm đã tải | Quan sát thanh filter | Hiển thị đủ 8 nút lọc: Danh mục, Dòng sản phẩm, Thương hiệu, Hương vị, Thể tích / Khối lượng, Nhu cầu dinh dưỡng, Mức đường, Phương thức giao hàng | Medium |

## 4. Sắp xếp sản phẩm (dropdown "Xếp theo:")

### Mô tả tính năng

Combobox "Xếp theo:" (mặc định hiển thị "Liên quan") nằm cạnh phải thanh filter, phía trên lưới sản phẩm. Click mở `listbox` gồm các option: "Giá tăng dần", "Giá giảm dần", "Liên quan" (đang chọn), "Bán chạy". Chọn 1 option sẽ sắp xếp lại lưới sản phẩm và đóng dropdown.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-10 | Mở dropdown sắp xếp | Trang danh sách sản phẩm đã tải | Click combobox "Xếp theo: Liên quan" | Dropdown mở hiển thị 4 lựa chọn: Giá tăng dần, Giá giảm dần, Liên quan, Bán chạy | Medium |
| TC-PL-11 | Sắp xếp theo giá tăng dần | Dropdown sắp xếp đang mở | Chọn "Giá tăng dần" | Dropdown đóng lại, danh sách sản phẩm sắp xếp lại theo giá tăng dần, label combobox đổi thành "Giá tăng dần" | High |
| TC-PL-12 | Sắp xếp theo giá giảm dần | Dropdown sắp xếp đang mở | Chọn "Giá giảm dần" | Danh sách sản phẩm sắp xếp lại theo giá giảm dần | High |

## 5. Lưới sản phẩm & thẻ thông tin sản phẩm

### Mô tả tính năng

Lưới hiển thị các thẻ sản phẩm (mỗi thẻ là 1 `button` bọc ngoài) gồm: ảnh sản phẩm (alt = tên quy cách đóng gói, vd. "Thùng 24 hộp"), badge chứng nhận (nếu có, vd. "Purity Award", "Superior Taste", "EU Organic"), tên nhóm sản phẩm (vd. "Sữa bột trẻ em pha sẵn"), heading tên sản phẩm dạng "Thương hiệu • Biến thể" (vd. "Optimum • A2 Pro+"), nút "Open product options", đoạn mô tả ngắn/lưu ý, dòng quy cách đóng gói (vd. "110ml, Thùng 24 hộp"), % giảm giá và giá hiển thị (vd. "-4% 293.622₫"), điểm đánh giá sao trung bình + số lượt đánh giá (vd. "5.0 (33)") — một số sản phẩm chưa có đánh giá thì không hiển thị mục này. Click vào heading/ảnh sản phẩm điều hướng sang trang chi tiết `/products/<slug>?pack=<size>&size=<packaging>`.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-13 | Hiển thị đầy đủ thông tin trên thẻ sản phẩm | Trang danh sách sản phẩm đã tải | Quan sát thẻ sản phẩm đầu tiên | Thẻ hiển thị ảnh, tên nhóm sản phẩm, tên thương hiệu/biến thể, mô tả, quy cách đóng gói, giá và % giảm giá | Critical |
| TC-PL-14 | Hiển thị đánh giá sao khi sản phẩm có review | Sản phẩm có review (vd. "Optimum • A2 Pro+" có 33 đánh giá) | Quan sát thẻ sản phẩm | Hiển thị điểm trung bình (vd. "5.0") và số lượt đánh giá trong ngoặc (vd. "(33)") | Low |
| TC-PL-15 | Không hiển thị đánh giá khi sản phẩm chưa có review | Sản phẩm chưa có review (vd. "Creamer đặc • Nhãn xanh lá" gói 1284g) | Quan sát thẻ sản phẩm | Không hiển thị điểm đánh giá/số lượt đánh giá | Low |

## 6. Badge chứng nhận chất lượng

### Mô tả tính năng

Một số thẻ sản phẩm có nhãn chứng nhận chất lượng hiển thị ở góc trên thẻ, ví dụ: "Purity Award", "Superior Taste", "EU Organic". Nhãn xuất hiện tuỳ sản phẩm, không phải sản phẩm nào cũng có.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-16 | Hiển thị badge chứng nhận trên sản phẩm đạt chuẩn | Sản phẩm có badge (vd. "Optimum • A2 Pro+" có "Purity Award") | Quan sát thẻ sản phẩm | Badge "Purity Award" hiển thị ở đầu thẻ | Low |
| TC-PL-17 | Sản phẩm không có chứng nhận thì không hiển thị badge | Sản phẩm không có badge (vd. "Vinamilk • Nước dừa tươi") | Quan sát thẻ sản phẩm | Không hiển thị badge chứng nhận nào | Low |

## 7. Cảnh báo giới hạn giao hàng bán kính 10km (kem/sữa chua)

### Mô tả tính năng

Với các sản phẩm thuộc nhóm kem, sữa chua ăn, sữa chua uống, phần mô tả thẻ sản phẩm hiển thị thêm đoạn "LƯU Ý:" liệt kê: không dùng cho trẻ dưới 1 tuổi (tuỳ sản phẩm) và cảnh báo "Để đảm bảo chất lượng sản phẩm tốt nhất, các đơn hàng bao gồm kem, sữa chua ăn và sữa chua uống sẽ chỉ được giao trong bán kính 10km từ cửa hàng Vinamilk gần nhất..." kèm hotline hỗ trợ 1900 636 979 (phím 1).

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-18 | Hiển thị cảnh báo bán kính 10km cho sản phẩm kem/sữa chua | Đã lọc danh mục "kem" hoặc "sữa chua ăn" | Quan sát mô tả thẻ sản phẩm | Đoạn "LƯU Ý" hiển thị cảnh báo giao hàng trong bán kính 10km kèm hotline | Medium |
| TC-PL-19 | Không hiển thị cảnh báo 10km cho sản phẩm ngoài nhóm kem/sữa chua | Sản phẩm thuộc danh mục khác (vd. "sữa tươi", "nước ép") | Quan sát mô tả thẻ sản phẩm | Không có đoạn cảnh báo bán kính 10km | Low |

## 8. Mua nhanh từ danh sách (Open product options → chọn quy cách → Thêm vào giỏ)

### Mô tả tính năng

Mỗi thẻ sản phẩm có nút "Open product options" cạnh tên sản phẩm. Click mở rộng thẻ tại chỗ (nút đổi thành "Close product options"), hiển thị: tablist chọn biến thể/dung tích (vd. tab "180ml", "110ml"), bên trong tabpanel là 1-2 radiogroup chọn quy cách đóng gói (vd. "Thùng 24 hộp" kèm % giảm và giá, "Lốc 4 hộp" kèm giá), bộ điều chỉnh số lượng dạng spinbutton (nút giảm bị disable khi số lượng = 1, nút tăng, ô nhập số lượng mặc định "1"), và nút "Thêm vào giỏ" hiển thị giá theo lựa chọn hiện tại (vd. "293.622₫ Thêm vào giỏ"). Click lại nút "Close product options" để thu gọn thẻ.

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-20 | Mở rộng tuỳ chọn mua nhanh trên thẻ sản phẩm | Trang danh sách sản phẩm đã tải | Click nút "Open product options" trên thẻ sản phẩm đầu tiên | Thẻ mở rộng hiển thị tablist biến thể, radiogroup quy cách đóng gói, bộ chỉnh số lượng và nút "Thêm vào giỏ"; nút đổi thành "Close product options" | Critical |
| TC-PL-21 | Chọn quy cách đóng gói khác trong tuỳ chọn mua nhanh | Thẻ sản phẩm đang mở rộng tuỳ chọn | Chọn radio quy cách khác (vd. "Lốc 4 hộp") | Giá hiển thị trên nút "Thêm vào giỏ" cập nhật theo quy cách vừa chọn | High |
| TC-PL-22 | Số lượng tối thiểu là 1, nút giảm bị vô hiệu hoá | Thẻ sản phẩm đang mở rộng tuỳ chọn, số lượng = 1 | Quan sát bộ điều chỉnh số lượng | Nút giảm số lượng ở trạng thái disabled khi số lượng = 1 | Medium |
| TC-PL-23 | Thu gọn tuỳ chọn mua nhanh | Thẻ sản phẩm đang mở rộng tuỳ chọn | Click nút "Close product options" | Thẻ sản phẩm thu gọn về trạng thái ban đầu, nút đổi lại thành "Open product options" | Medium |

## 9. Phân trang

### Mô tả tính năng

Cuối lưới sản phẩm là `navigation "Pagination"` gồm: nút "Previous page" (disabled ở trang 1), link trang hiện tại (vd. "Page 1 is your current page"), các link số trang kế tiếp (vd. "Page 2"), nút "Jump forward" (nhảy nhanh nhiều trang), link trang cuối (vd. "Page 31" khi không lọc — tổng 363 sản phẩm), nút "Next page". URL trang phân trang có dạng `?page=<n>` (kết hợp với filter hiện có, vd. `?categories=["kem"]&page=2`).

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-24 | Nút "Previous page" bị vô hiệu hoá ở trang đầu | Đang ở trang 1 | Quan sát thanh phân trang | Nút "Previous page" ở trạng thái disabled | Medium |
| TC-PL-25 | Chuyển sang trang 2 | Đang ở trang 1 danh sách sản phẩm | Click link "Page 2" | URL chuyển sang `?page=2`, danh sách hiển thị bộ sản phẩm khác (trang 2) | High |
| TC-PL-26 | Số trang cuối phản ánh đúng tổng số bộ lọc đang áp dụng | Đã lọc danh mục "kem" (58 sản phẩm) | Quan sát thanh phân trang | Trang cuối hiển thị là trang 5 (58 sản phẩm / trang, khác với trang 31 khi không lọc) | Low |

## 10. Click vào sản phẩm để xem chi tiết

### Mô tả tính năng

Click vào ảnh hoặc heading tên sản phẩm trên thẻ (không phải nút "Open product options") điều hướng sang trang chi tiết sản phẩm `/products/<slug>?pack=<dung-tích>&size=<quy-cách>` (vd. `/products/sudd-optimum-a2-pro?pack=110ml&size=Th%C3%B9ng+24+h%E1%BB%99p`).

### Test Cases

| ID | Tiêu đề | Precondition | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| TC-PL-27 | Click sản phẩm đầu tiên chuyển sang trang chi tiết | Trang danh sách sản phẩm đã tải | Click vào heading tên sản phẩm đầu tiên | Điều hướng sang URL `/products/<slug>?pack=...&size=...` đúng với sản phẩm đã click | Critical |
