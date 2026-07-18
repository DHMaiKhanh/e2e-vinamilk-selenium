# Vinamilk Website – Feature Inventory for E2E Automation

Source: https://www.vinamilk.com.vn/ (khảo sát thủ công qua WebFetch, đối chiếu với page objects hiện có trong `ui-tests/src/main/java/.../pages`)

> Mục tiêu: liệt kê các tính năng cần viết test case tự động (Selenium). Đánh dấu tính năng nào đã có Page Object/Test sẵn.

## 1. Trang chủ (Home Page)

> Quét trực tiếp bằng MCP Playwright ngày 2026-07-11 (`browser_navigate` + `browser_snapshot`, có tương tác click icon tài khoản/tìm kiếm/giỏ hàng/menu "Sản phẩm" trên https://www.vinamilk.com.vn/). Chi tiết từng tính năng (1 file .md riêng/tính năng): [docs/features/home-page/](features/home-page/).

- [x] Top utility bar: banner "Miễn phí vận chuyển cho đơn từ 300K", link Luôn là Vinamilk / Luôn cầu tiến / Cửa hàng, icon tài khoản (→ `/login`), icon tìm kiếm, icon giỏ hàng (badge số lượng) — `HeaderComponent`, `HomePage.openAccount()`, `HomePage.cartItemCount()`
- [x] Icon tài khoản mở trang đăng nhập `/login` (không phải dropdown) — xác nhận qua click thật
- [x] Header: logo, menu điều hướng chính gồm 8 mục (Sản phẩm, Trẻ nhỏ, Thiếu niên, Người lớn, Thương hiệu, Luôn vui khoẻ, Nhận quà, Vinamilk Care) — `HomePage.isMainNavDisplayed()`, `HomePage.mainNavItemCount()`
- [x] Mega-menu "Sản phẩm" (hover/click mở list xổ xuống): sản phẩm mới nổi bật + quick link (Bán chạy/BEST, Ưu đãi/PROMO, Vinamilk care/SUBS, Flash Sale/HOT), nhóm "Ngành hàng" (13 danh mục kèm số lượng, vd. sữa tươi 59), nhóm "Thương hiệu" (10 nhãn hàng: 100%, green farm, probi, optimum, dielac, ông thọ, ngôi sao phương nam, sure, susu, mộc châu creamery), nhóm "Đối tượng đặc biệt" (5 mục), nhóm "Công thức chuyên biệt" (7 mục) — mới phát hiện qua click thật vào nút "Sản phẩm"
- [x] Search flyout dialog (icon kính lúp → mở modal "Tìm trong Vinamilk"): ô nhập textbox, nút Đóng, danh sách "Xu hướng" (trending keyword links), danh sách "Dành cho bạn" (gợi ý sản phẩm) hiển thị ngay cả khi chưa gõ từ khoá — `HomePage.openSearchDialog()`, `HomePage.searchTrendingSuggestions()`, `HomePage.searchSuggestedProducts()`
- [x] Tìm kiếm sản phẩm theo từ khoá (gõ + Enter trong dialog) chuyển sang `/search?q=<keyword>` — `HomePage.searchProduct()`
- [x] Icon giỏ hàng mở drawer "Giỏ hàng"; trạng thái rỗng hiển thị "Chưa có sản phẩm trong giỏ hàng" + CTA "Xem sản phẩm giá tốt" → `/collections/all-products` — `HomePage.openCart()` (mới xác nhận trạng thái rỗng)
- [x] Banner/hero slider: 3 thông điệp giá trị (Luôn vắt tươi ngon từ 14 trang trại xanh, Luôn sạch tinh khiết từ 14 nhà máy công nghệ, Luôn đủ lựa chọn cho cả gia đình) + 2 banner khuyến mãi đứng đầu carousel, nút Prev/Next — `HomePage.isHeroSliderDisplayed()`, `HomePage.heroSlideCount()`, `HomePage.goToNextHeroSlide()` / `goToPreviousHeroSlide()`
- [x] Dải huy chương chất lượng (award badges - 5 hình) ngay dưới hero — `HomePage.awardBadgeCount()`
- [x] Sản phẩm mới nổi bật (banner "Mới! Mới! Mới!" — vd. Kem Vinamilk Gelato) với link sang trang chi tiết sản phẩm
- [x] Khu vực "Mời bạn sắm sửa": carousel before/after image lớn (~22 sản phẩm) — `HomePage.isFeaturedProductsSectionDisplayed()`
- [x] Khối nội dung "Cầu tiến là bí quyết" (công nghệ) + CTA "Tìm hiểu thêm" → `/technology`
- [x] Khối nội dung "Để tâm hành động" (phát triển bền vững/Net Zero), 3 bài viết dạng số thứ tự (01/02/03) + CTA → `/sustainability`
- [x] Khối "Mời vào đội" (tuyển dụng): 3 story link + CTA "Ứng tuyển ngay" → `/recruitment/career-opportunities`
- [x] Footer: thông tin công ty (địa chỉ, người đại diện, hotline, email, MST), 6 nhóm link (Sắm sửa, Luôn để tâm, Luôn là Vinamilk, Luôn vui khoẻ, Luôn cùng bạn, Luôn hỗ trợ), mạng xã hội (Facebook/Instagram/LinkedIn/TikTok/YouTube), chọn ngôn ngữ, chứng nhận Bộ Công Thương, bản quyền + điều khoản/chính sách — `HomePage.isFooterDisplayed()`, `HomePage.footerSocialLinkCount()`
- [x] Live-chat widget nổi góc dưới màn hình ("Chào bạn! Vinamilk luôn sẵn sàng hỗ trợ")
- [x] Link "Cửa hàng" trong top bar mở đúng trang `/store-list` ("Danh sách cửa hàng | Vinamilk") — xác nhận qua điều hướng trực tiếp

Test case tự động: `HomePageTest` (`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java`) — main nav, hero slider next/previous, search dialog trending + suggestions, search theo từ khoá, cart badge/mở giỏ hàng, khu vực sản phẩm nổi bật, award badges, footer + social links.

## 2. Trang danh sách sản phẩm (Product Listing)

> Quét trực tiếp bằng MCP Playwright ngày 2026-07-14 (`browser_navigate` + `browser_snapshot`, có tương tác click filter "Danh mục", checkbox lọc, dropdown "Xếp theo:", nút "Chọn địa chỉ của bạn", nút "Open product options" trên https://www.vinamilk.com.vn/collections/all-products). Chi tiết từng tính năng: [docs/features/product-listing.md](features/product-listing.md).

- [x] Hiển thị danh sách sản phẩm, đếm số lượng kết quả — `ProductListingPage.resultCount()`
- [x] Click vào sản phẩm đầu tiên để xem chi tiết — `ProductListingPage.openFirstProduct()`
- [x] Banner "Chọn khu vực giao hàng" + dialog "Chọn địa chỉ của bạn" (tìm kiếm địa chỉ giao hàng) — xác nhận qua click thật
- [x] Bộ lọc sản phẩm: Danh mục, Dòng sản phẩm, Thương hiệu, Hương vị, Thể tích/Khối lượng, Nhu cầu dinh dưỡng, Mức đường, Phương thức giao hàng — dạng dropdown checkbox kèm số lượng, cập nhật URL `?categories=[...]` và số đếm tổng, có nút "Xoá bộ lọc"
- [x] Sắp xếp sản phẩm (dropdown "Xếp theo:"): Giá tăng dần, Giá giảm dần, Liên quan (mặc định), Bán chạy
- [x] Phân trang (danh sách 363 sản phẩm, ~31 trang mặc định, URL `?page=<n>`, số trang thay đổi theo bộ lọc đang áp dụng)
- [x] Hiển thị thông tin sản phẩm trên thẻ: ảnh, tên nhóm sản phẩm, thương hiệu/biến thể, mô tả, quy cách đóng gói, % giảm giá, giá, đánh giá sao + số lượt đánh giá (nếu có)
- [x] Badge chứng nhận chất lượng (Purity Award, Superior Taste, EU Organic) — hiển thị tuỳ sản phẩm
- [x] Cảnh báo giới hạn giao hàng bán kính 10km cho kem/sữa chua ăn/sữa chua uống (đoạn "LƯU Ý" trong mô tả)
- [x] Mua nhanh từ danh sách: nút "Open product options" mở tablist biến thể + radiogroup quy cách đóng gói + bộ chỉnh số lượng + nút "Thêm vào giỏ" ngay trên thẻ sản phẩm

## 3. Trang chi tiết sản phẩm (Product Detail)
- [ ] Hiển thị thông tin chi tiết (mô tả, thành phần dinh dưỡng, hình ảnh)
- [ ] Chọn số lượng
- [ ] Thêm vào giỏ hàng — liên quan `ProductDetailPage`, `AddToCartTest`
- [ ] Mua ngay
- [ ] Đánh giá / review sản phẩm
- [ ] Sản phẩm liên quan / gợi ý

## 4. Giỏ hàng (Cart)
- [x] Đếm số lượng item trong giỏ — `CartPage.itemCount()`
- [x] Tiến hành thanh toán — `CartPage.proceedToCheckout()`
- [ ] Cập nhật số lượng sản phẩm trong giỏ
- [ ] Xoá sản phẩm khỏi giỏ
- [ ] Áp dụng mã giảm giá/voucher
- [ ] Tính tổng tiền, phí vận chuyển

## 5. Đăng nhập / Tài khoản (Login/Account)
- [x] Đăng nhập bằng email/password — `LoginPage.loginAs()`
- [x] Hiển thị lỗi đăng nhập — `LoginPage.getErrorMessage()`
- [ ] Đăng ký tài khoản mới
- [ ] Quên mật khẩu
- [ ] Đăng xuất
- [ ] Trang thông tin tài khoản, lịch sử đơn hàng

## 6. Tìm kiếm (Search)
- [x] Tìm kiếm theo từ khoá — `ProductSearchTest`
- [ ] Gợi ý tìm kiếm (autocomplete)
- [ ] Xử lý trường hợp không có kết quả

## 7. Chương trình / Nội dung khác (chưa có test)
- [ ] Trang "Luôn vui khoẻ" (Health Blog) – đọc bài viết
- [ ] Trang "Nhận quà" (Rewards Program) – tích điểm, đổi quà
- [ ] Trang "Vinamilk Care" – liên hệ CSKH, form gửi yêu cầu
- [ ] Trang "Thương hiệu" – danh sách các nhãn hàng (Green Farm, Optimum, Probi...)
- [ ] Store locator – tìm cửa hàng theo khu vực

## Ghi chú
- Các mục có `[x]` đã có Page Object + test case sơ khởi trong repo (`ui-tests`), nhưng selector hiện tại là placeholder (`.product-card`, `.cart-item-row`, `input[name='q']`...) — cần cập nhật lại theo DOM thực tế của trang khi viết test thật.
- Chưa thể dùng MCP Playwright để quét trực tiếp bằng trình duyệt (server chưa được kết nối trong session này) — dữ liệu tính năng ở trên được tổng hợp qua WebFetch (đọc nội dung trang), nên có thể thiếu các phần tử JS-render động (modal, dropdown, autocomplete...). Khuyến nghị mở trực tiếp trang bằng Selenium để lấy chính xác locator trước khi viết test.
