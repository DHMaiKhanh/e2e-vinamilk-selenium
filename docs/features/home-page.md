# Home Page — Tổng hợp tính năng & Test Cases

- Trang: Trang chủ — https://www.vinamilk.com.vn/
- Ngày quét: 2026-07-11 — MCP Playwright (`browser_navigate`, `browser_snapshot`, `browser_click`, `browser_type`)
- Tài liệu này gộp toàn bộ các phần đã quét riêng lẻ của trang chủ (top bar, nav, mega-menu, search, cart, các block nội dung, footer, widget...) thành một file duy nhất.

## Mục lục

1. [Top utility bar](#1-top-utility-bar)
2. [Icon tài khoản → Đăng nhập](#2-icon-tài-khoản--đăng-nhập)
3. [Menu điều hướng chính (Main Nav)](#3-menu-điều-hướng-chính-main-nav)
4. [Mega-menu "Sản phẩm"](#4-mega-menu-sản-phẩm)
5. [Search flyout dialog](#5-search-flyout-dialog)
6. [Tìm kiếm sản phẩm theo từ khoá](#6-tìm-kiếm-sản-phẩm-theo-từ-khoá)
7. [Giỏ hàng (cart drawer)](#7-giỏ-hàng-cart-drawer)
8. [Hero slider (banner chính)](#8-hero-slider-banner-chính)
9. [Dải huy chương chất lượng (award badges)](#9-dải-huy-chương-chất-lượng-award-badges)
10. [Sản phẩm mới nổi bật](#10-sản-phẩm-mới-nổi-bật)
11. [Khu vực "Mời bạn sắm sửa"](#11-khu-vực-mời-bạn-sắm-sửa)
12. [Khối "Cầu tiến là bí quyết"](#12-khối-cầu-tiến-là-bí-quyết)
13. [Khối "Để tâm hành động"](#13-khối-để-tâm-hành-động)
14. [Khối "Mời vào đội" (tuyển dụng)](#14-khối-mời-vào-đội-tuyển-dụng)
15. [Footer](#15-footer)
16. [Live-chat widget](#16-live-chat-widget)
17. [Link "Cửa hàng" (store locator)](#17-link-cửa-hàng-store-locator)

---

## 1. Top utility bar

### Mô tả tính năng

Thanh trên cùng của header, gồm:
- Banner text: "Miễn phí vận chuyển cho đơn từ 300K".
- 3 link text: "Luôn là Vinamilk" (→ `/about-us`), "Luôn cầu tiến" (→ `/technology`), "Cửa hàng" (→ `/store-list`).
- 3 icon bên phải: tài khoản, tìm kiếm, giỏ hàng (có badge số lượng, mặc định "0").

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-01 | Hiển thị top utility bar đầy đủ | Mở trang chủ | Quan sát banner miễn phí vận chuyển, 3 link, 3 icon | Tất cả thành phần hiển thị đúng text/thứ tự | High |
| TC-HOME-01b | Click "Luôn là Vinamilk" | Mở trang chủ | Click link "Luôn là Vinamilk" | Điều hướng sang `/about-us` | Medium |
| TC-HOME-01c | Click "Luôn cầu tiến" | Mở trang chủ | Click link "Luôn cầu tiến" | Điều hướng sang `/technology` | Medium |

---

## 2. Icon tài khoản → Đăng nhập

### Mô tả tính năng

Icon đầu tiên trong nhóm icon của top utility bar. Click điều hướng thẳng sang trang đăng nhập `/login` (tiêu đề "Đăng nhập | Vinamilk") — không mở dropdown tài khoản.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-02 | Click icon tài khoản điều hướng sang trang đăng nhập | Ở trang chủ, chưa đăng nhập | Click icon tài khoản (đầu tiên trong nhóm icon) | Điều hướng tới `/login`, tiêu đề "Đăng nhập \| Vinamilk" | Critical |

---

## 3. Menu điều hướng chính (Main Nav)

### Mô tả tính năng

Nav ngang trong header, gồm 8 mục: Sản phẩm (button, mở mega-menu), Trẻ nhỏ (button), Thiếu niên (button), Người lớn (button), Thương hiệu (button), Luôn vui khoẻ (link → `/blogs`), Nhận quà (link → `/vinamilk-rewards`), Vinamilk Care (link → `/care`). Logo Vinamilk nằm cạnh nav, link về `/`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-03 | Hiển thị đủ 8 mục menu điều hướng chính | Mở trang chủ | Đếm số mục trong nav chính | Đúng 8 mục theo thứ tự: Sản phẩm, Trẻ nhỏ, Thiếu niên, Người lớn, Thương hiệu, Luôn vui khoẻ, Nhận quà, Vinamilk Care | High |
| TC-HOME-03b | Click "Luôn vui khoẻ" | Mở trang chủ | Click link "Luôn vui khoẻ" | Điều hướng sang `/blogs` | Medium |
| TC-HOME-03c | Click "Nhận quà" | Mở trang chủ | Click link "Nhận quà" | Điều hướng sang `/vinamilk-rewards` | Medium |
| TC-HOME-03d | Click "Vinamilk Care" | Mở trang chủ | Click link "Vinamilk Care" | Điều hướng sang `/care` | Medium |
| TC-HOME-03e | Click logo | Mở trang chủ (đang ở trang khác) | Click logo Vinamilk | Điều hướng về `/` | Low |

---

## 4. Mega-menu "Sản phẩm"

### Mô tả tính năng

Kích hoạt: click button "Sản phẩm" trên nav chính → xổ ra panel lớn bên dưới. Nội dung panel gồm các cột:
- Banner sản phẩm mới (badge "MỚI/NEW") kèm ảnh, link sang trang chi tiết sản phẩm.
- Quick links dạng thẻ: "Bán chạy" (BEST) → `/best-selling`, "Ưu đãi" (PROMO) → `/promotions`, "Vinamilk care" (SUBS) → `/care`, "Flash Sale" (HOT) → `/flash-sales`.
- Nhóm "Ngành hàng": 13 danh mục kèm số lượng sản phẩm, vd. "sữa bột trẻ em 55", "sữa tươi 59", "kem 55"... mỗi mục là link → `/collections/<slug>?src=ALL`.
- Nhóm "Thương hiệu": 10 nhãn hàng (100%, green farm, probi, optimum, dielac, ông thọ, ngôi sao phương nam, sure, susu, mộc châu creamery) kèm số lượng, link → `/collections/all-products?src=ALL_vendors:<brand>`.
- Nhóm "Đối tượng đặc biệt": 5 mục (trẻ suy dinh dưỡng, mẹ mang thai, người bị dị ứng, người bệnh tiểu đường, người điều trị y tế đặc biệt).
- Nhóm "Công thức chuyên biệt": 7 mục (hữu cơ châu âu, cao đạm, cao canxi, ít đường, ít béo, dị ứng đạm đậu nành, dị ứng đạm bò, dị ứng lactose).

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-04 | Mở mega-menu "Sản phẩm" hiển thị đủ nhóm nội dung | Mở trang chủ | Click button "Sản phẩm" | Panel xổ xuống hiển thị: quick links (Bán chạy/Ưu đãi/Vinamilk care/Flash Sale), nhóm Ngành hàng (13 mục), Thương hiệu (10 mục), Đối tượng đặc biệt (5 mục), Công thức chuyên biệt (7 mục) | High |
| TC-HOME-05 | Click một mục "Ngành hàng" trong mega-menu điều hướng đúng collection | Mega-menu "Sản phẩm" đang mở | Click "sữa tươi" | Điều hướng sang `/collections/sua-tuoi?src=ALL` | Medium |
| TC-HOME-05b | Click quick link "Bán chạy" | Mega-menu "Sản phẩm" đang mở | Click thẻ "Bán chạy" | Điều hướng sang `/best-selling` | Medium |
| TC-HOME-05c | Click quick link "Flash Sale" | Mega-menu "Sản phẩm" đang mở | Click thẻ "Flash Sale" | Điều hướng sang `/flash-sales` | Medium |
| TC-HOME-05d | Click một thương hiệu trong mega-menu | Mega-menu "Sản phẩm" đang mở | Click "probi" | Điều hướng sang `/collections/all-products?src=ALL_vendors:probi` | Low |

---

## 5. Search flyout dialog

### Mô tả tính năng

Kích hoạt: click icon tìm kiếm (kính lúp) trên top bar → mở modal "Tìm trong Vinamilk". Thành phần: textbox nhập từ khoá (focus sẵn), nút "Đóng". Ngay khi mở (chưa gõ gì) đã hiển thị 2 danh sách gợi ý:
- "Xu hướng": các từ khoá thịnh hành dạng link (vd. Cao đạm, Organic, Phát triển toàn diện, Sữa chua uống thanh trùng, Sữa tươi giảm béo vị chuối) → `/search?q=<keyword>`.
- "Dành cho bạn": danh sách sản phẩm gợi ý (ảnh, tên, quy cách đóng gói) → link chi tiết sản phẩm.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-06 | Mở search dialog hiển thị gợi ý mặc định | Mở trang chủ | Click icon tìm kiếm | Modal "Tìm trong Vinamilk" mở, hiển thị textbox, nút Đóng, danh sách "Xu hướng" và "Dành cho bạn" dù chưa nhập gì | Critical |
| TC-HOME-07 | Đóng search dialog | Search dialog đang mở | Click nút "Đóng" | Modal đóng lại, quay về trang chủ | Medium |
| TC-HOME-08 | Click từ khoá gợi ý "Xu hướng" | Search dialog đang mở | Click một link trong "Xu hướng" (vd. "Organic") | Điều hướng sang `/search?q=Organic`, trang kết quả tìm kiếm hiển thị | High |
| TC-HOME-08b | Click sản phẩm gợi ý "Dành cho bạn" | Search dialog đang mở | Click một sản phẩm trong "Dành cho bạn" | Điều hướng sang trang chi tiết sản phẩm tương ứng | Low |

---

## 6. Tìm kiếm sản phẩm theo từ khoá

### Mô tả tính năng

Trong search dialog, gõ từ khoá vào textbox rồi nhấn Enter. Kết quả: điều hướng sang `/search?q=<keyword-encoded>`, tiêu đề trang "Kết quả tìm kiếm | Vinamilk". Đã xác nhận thực tế với từ khoá "sữa tươi" → `/search?q=s%E1%BB%AFa+t%C6%B0%C6%A1i`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-09 | Tìm kiếm sản phẩm theo từ khoá tự nhập | Search dialog đang mở | Gõ từ khoá (vd. "sữa tươi") vào textbox, nhấn Enter | Điều hướng sang `/search?q=sữa+tươi` (đã encode), tiêu đề "Kết quả tìm kiếm \| Vinamilk" | Critical |
| TC-HOME-10 | Tìm kiếm với từ khoá rỗng | Search dialog đang mở, textbox trống | Nhấn Enter khi chưa nhập gì | Không điều hướng hoặc hiển thị thông báo hợp lệ (không lỗi JS) — cần xác minh thêm hành vi thực tế | Low |
| TC-HOME-10b | Tìm kiếm với từ khoá không có kết quả | Search dialog đang mở | Gõ từ khoá vô nghĩa (vd. "xyzxyz123"), nhấn Enter | Trang `/search?q=xyzxyz123` hiển thị trạng thái "không có kết quả" phù hợp — cần xác minh thêm hành vi thực tế | Low |

---

## 7. Giỏ hàng (cart drawer)

### Mô tả tính năng

Kích hoạt: click icon giỏ hàng (badge số "0") trên top bar. Mở drawer/dialog "Giỏ hàng" bên phải màn hình, không đổi URL. Trạng thái rỗng: hình minh hoạ giỏ hàng + text "Chưa có sản phẩm trong giỏ hàng" + nút CTA "Xem sản phẩm giá tốt" → `/collections/all-products`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-11 | Mở giỏ hàng khi giỏ trống | Giỏ hàng chưa có sản phẩm (badge "0") | Click icon giỏ hàng | Drawer "Giỏ hàng" mở, hiển thị "Chưa có sản phẩm trong giỏ hàng" + nút "Xem sản phẩm giá tốt" | Critical |
| TC-HOME-12 | CTA "Xem sản phẩm giá tốt" từ giỏ hàng trống | Drawer giỏ hàng đang mở, trạng thái rỗng | Click "Xem sản phẩm giá tốt" | Điều hướng sang `/collections/all-products` | Medium |
| TC-HOME-12b | Đóng drawer giỏ hàng | Drawer giỏ hàng đang mở | Click nút đóng (X) trên drawer | Drawer đóng lại, quay về trang chủ | Low |

---

## 8. Hero slider (banner chính)

### Mô tả tính năng

Carousel gồm 5 slide: 2 banner ảnh khuyến mãi đứng đầu + 3 slide thông điệp giá trị thương hiệu:
1. "Luôn vắt tươi ngon" — từ 14 trang trại xanh trải dài toàn quốc.
2. "Luôn sạch tinh khiết" — từ 14 nhà máy công nghệ hàng đầu thế giới.
3. "Luôn đủ lựa chọn" — cho cả gia đình.

Có nút điều hướng Prev/Next (2 button cạnh carousel).

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-13 | Hero slider hiển thị đủ 5 slide và chuyển slide bằng nút Next | Mở trang chủ | Đếm số slide; click nút Next nhiều lần | Có 5 slide (2 banner + 3 thông điệp); slide tiếp theo hiển thị đúng sau mỗi click Next | High |
| TC-HOME-14 | Hero slider quay lại slide trước bằng nút Previous | Đang ở slide bất kỳ khác slide đầu | Click nút Previous | Hiển thị đúng slide trước đó | Medium |

---

## 9. Dải huy chương chất lượng (award badges)

### Mô tả tính năng

Ngay dưới hero slider: 5 hình ảnh SVG huy chương/chứng nhận xếp hàng ngang.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-15 | Hiển thị dải 5 award badge | Mở trang chủ | Cuộn tới khu vực dưới hero | 5 hình ảnh huy chương hiển thị đầy đủ | Low |

---

## 10. Sản phẩm mới nổi bật

### Mô tả tính năng

Banner "Mới! Mới! Mới!" kèm ảnh sản phẩm (vd. Kem Vinamilk Gelato) — link sang `/products/kem-gelato-matcha-vinamilk?...`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-16 | Click banner sản phẩm mới nổi bật | Mở trang chủ | Click banner "Mới! Mới! Mới!" | Điều hướng sang trang chi tiết sản phẩm tương ứng (vd. Kem Gelato) | Medium |

---

## 11. Khu vực "Mời bạn sắm sửa"

### Mô tả tính năng

Heading "Mời bạn sắm sửa" + carousel lớn dạng before/after image (~22 sản phẩm), mỗi item có ảnh "before" và "after" khi hover/chuyển slide.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-17 | Hiển thị khu vực "Mời bạn sắm sửa" | Mở trang chủ | Cuộn tới khu vực "Mời bạn sắm sửa" | Heading hiển thị đúng, carousel before/after với nhiều item load được | Medium |

---

## 12. Khối "Cầu tiến là bí quyết"

### Mô tả tính năng

Nội dung giới thiệu công nghệ sản xuất + ảnh minh hoạ + nút CTA "Tìm hiểu thêm" → `/technology`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-18 | CTA "Tìm hiểu thêm" khối Công nghệ | Mở trang chủ | Click "Tìm hiểu thêm" trong khối "Cầu tiến là bí quyết" | Điều hướng sang `/technology` | Medium |

---

## 13. Khối "Để tâm hành động"

### Mô tả tính năng

Nội dung phát triển bền vững/Net Zero, 3 bài viết đánh số thứ tự 01/02/03 (mỗi bài có ảnh, tiêu đề, mô tả ngắn) + nút CTA "Tìm hiểu thêm" → `/sustainability`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-19 | CTA "Tìm hiểu thêm" khối Bền vững | Mở trang chủ | Click "Tìm hiểu thêm" trong khối "Để tâm hành động" | Điều hướng sang `/sustainability` | Medium |
| TC-HOME-19b | Hiển thị đủ 3 bài viết đánh số | Mở trang chủ | Cuộn tới khối "Để tâm hành động" | 3 bài viết 01/02/03 hiển thị đủ ảnh, tiêu đề, mô tả | Low |

---

## 14. Khối "Mời vào đội" (tuyển dụng)

### Mô tả tính năng

3 story-link: "Job phấp phới, chờ đồng đội mới!" (→ `/recruitment/vi-sao-chon-vinamilk#jobs`), "Chất Vinamilk" (→ `/recruitment/vi-sao-chon-vinamilk#gens`), "Nhịp sống Vinamilk" (→ facebook.com/LifeAtVinamilk) + nút CTA "Ứng tuyển ngay" → `/recruitment/career-opportunities`.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-20 | CTA "Ứng tuyển ngay" khối tuyển dụng | Mở trang chủ | Click "Ứng tuyển ngay" | Điều hướng sang `/recruitment/career-opportunities` | Low |
| TC-HOME-20b | Click story-link "Nhịp sống Vinamilk" | Mở trang chủ | Click "Nhịp sống Vinamilk" | Mở tab/điều hướng sang facebook.com/LifeAtVinamilk | Low |

---

## 15. Footer

### Mô tả tính năng

- Thông tin công ty: tên, địa chỉ, đại diện pháp luật, hotline, fax, email, MST.
- 6 nhóm link: SẮM SỬA, LUÔN ĐỂ TÂM, LUÔN LÀ VINAMILK, LUÔN VUI KHỎE, LUÔN CÙNG BẠN, LUÔN HỖ TRỢ.
- Mạng xã hội: Facebook, Instagram, LinkedIn, TikTok, YouTube (icon link).
- Combobox chọn ngôn ngữ (mặc định "Tiếng Việt"), 2 logo chứng nhận Bộ Công Thương (link online.gov.vn).
- Dòng bản quyền + 4 link chính sách (Điều khoản sử dụng, Chính sách bảo mật, Quy chế hoạt động [PDF], Quy trình giải quyết khiếu nại).

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-21 | Hiển thị footer đầy đủ 6 nhóm link + social | Mở trang chủ | Cuộn xuống footer | Đủ 6 nhóm, 5 icon mạng xã hội, combobox ngôn ngữ, 2 chứng nhận, dòng bản quyền + 4 link chính sách | High |
| TC-HOME-21b | Click link chính sách "Điều khoản sử dụng" | Mở trang chủ, cuộn tới footer | Click "Điều khoản sử dụng" | Điều hướng sang `/support/terms-of-use` | Low |

---

## 16. Live-chat widget

### Mô tả tính năng

Widget nổi góc dưới màn hình, hiển thị text mời chat "Chào bạn! Vinamilk luôn sẵn sàng hỗ trợ" + nút mở/đóng.

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-22 | Live-chat widget hiển thị và có thể mở/đóng | Mở trang chủ | Quan sát góc dưới màn hình, click nút mở/đóng | Widget hiển thị text mời chat; click nút thay đổi trạng thái hiển thị | Low |

---

## 17. Link "Cửa hàng" (store locator)

### Mô tả tính năng

Link trong top utility bar và footer, trỏ tới `/store-list`, trang thực tế có tiêu đề "Danh sách cửa hàng | Vinamilk".

### Test Cases

| ID | Tiêu đề | Tiền điều kiện | Các bước | Kết quả mong đợi | Mức độ |
|---|---|---|---|---|---|
| TC-HOME-23 | Link "Cửa hàng" điều hướng đúng trang store locator | Mở trang chủ | Click link "Cửa hàng" ở top bar (hoặc điều hướng trực tiếp `/store-list`) | Trang "Danh sách cửa hàng \| Vinamilk" hiển thị | High |
