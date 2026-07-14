# Home Page — Phân tích code đã generate

- Thời điểm phân tích: 2026-07-12
- File Page Object: `ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java` (300 dòng)
- File Test: `ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java` (365 dòng)
- File dùng chung (tham khảo): `ui-tests/src/main/java/com/vinamilk/automation/ui/pages/BasePage.java` (45 dòng)
- Các test khác có import `HomePage` nhưng chỉ dùng như điểm khởi đầu điều hướng (không test tính năng của Home Page): `AddToCartTest.java`, `ProductSearchTest.java`, `LoginTest.java` — không thuộc phạm vi phân tích này.

## Tổng quan

`HomePage` là Page Object đại diện cho trang chủ Vinamilk, kế thừa `BasePage` để tái sử dụng các thao tác Selenium chung (click có chờ, gõ text, đọc text, kiểm tra hiển thị). File khai báo **48 locator** (`By`) bao phủ toàn bộ các khối trên trang: top utility bar, icon tài khoản/tìm kiếm/giỏ hàng, search dialog, main nav, mega-menu sản phẩm, hero slider, award badges, khối nội dung (featured products, technology, sustainability, recruitment), footer và live-chat widget. Có **47 phương thức public** (getter trạng thái, hành động click, điều hướng trả về Page Object khác). `HomePageTest` chứa **29 test method** (`@Test`), tổ chức dưới `@Epic("Storefront")` / `@Feature("Home Page")`, mỗi test map tới một hoặc nhiều `TC-HOME-xx` trong spec `docs/features/home-page.md`.

## Locators

| Tên | Chiến lược `By` / selector | Mục tiêu |
|---|---|---|
| `ACCOUNT_ICON` | CSS `header nav button:nth-of-type(1)` | Icon tài khoản (nút đầu tiên trong nav) |
| `SEARCH_ICON` | CSS `header nav button:nth-of-type(2)` | Icon tìm kiếm |
| `CART_ICON` | CSS `header nav button[aria-label*='cart'], header nav >* button:last-of-type` | Icon giỏ hàng |
| `CART_BADGE_COUNT` | CSS `header nav button:last-of-type` | Badge số lượng trên icon giỏ hàng |
| `SEARCH_DIALOG` | CSS `[role='dialog']` | Modal tìm kiếm |
| `SEARCH_INPUT` | XPath input trong dialog | Ô nhập từ khoá tìm kiếm |
| `SEARCH_CLOSE_BUTTON` | XPath button chứa "Đóng" trong dialog | Nút đóng search dialog |
| `SEARCH_TRENDING_LINKS` | XPath `a[href*='/search?q=']` trong dialog | Link từ khoá "Xu hướng" |
| `SEARCH_SUGGESTED_PRODUCTS` | XPath `a[href*='/products/']` trong dialog | Sản phẩm gợi ý "Dành cho bạn" |
| `MAIN_NAV` | CSS `nav[aria-label='Main']` | Thanh điều hướng chính |
| `MAIN_NAV_ITEMS` | CSS `nav[aria-label='Main'] li` | Từng mục trong nav chính |
| `TOP_UTILITY_LINKS` | CSS `header a` | Toàn bộ link trong header (đếm số lượng) |
| `HERO_SLIDER` | CSS group/img trong section đầu tiên | Vùng hero slider |
| `HERO_SLIDE_ITEMS` | CSS `[role='group']` trong section đầu | Từng slide |
| `HERO_SLIDER_PREV` / `HERO_SLIDER_NEXT` | CSS `button:first-of-type` / `button:last-of-type` trong section đầu | Nút Previous / Next của slider |
| `AWARD_BADGES` | CSS `main img[src*='award']` | Ảnh huy chương chất lượng |
| `FEATURED_PRODUCTS_HEADING` | XPath `h2` chứa "sắm sửa" | Heading khối "Mời bạn sắm sửa" |
| `FOOTER` | CSS `footer, [class*=footer]` | Toàn bộ footer |
| `FOOTER_SOCIAL_LINKS` | CSS link footer chứa domain facebook/instagram/tiktok/youtube/linkedin | Icon mạng xã hội |
| `FOOTER_LANGUAGE_COMBOBOX` | CSS `footer select, footer [role='combobox']` | Combobox chọn ngôn ngữ |
| `FOOTER_TERMS_LINK` | CSS `footer a[href*='/support/terms-of-use']` | Link "Điều khoản sử dụng" |
| `FOOTER_POLICY_LINKS` | CSS gộp 4 pattern href (terms-of-use, privacy, quy-che, khieu-nai) | 4 link chính sách footer |
| `TOP_BAR_SHIPPING_BANNER` | XPath text chứa "Miễn phí vận chuyển" | Banner miễn phí vận chuyển |
| `TOP_BAR_LINK_ALWAYS_VINAMILK` | CSS `header a[href*='/about-us']` | Link "Luôn là Vinamilk" |
| `TOP_BAR_LINK_ALWAYS_INNOVATIVE` | CSS `header a[href*='/technology']` | Link "Luôn cầu tiến" |
| `TOP_BAR_LINK_STORE` | CSS `header a[href*='/store-list']` | Link "Cửa hàng" |
| `NAV_LINK_BLOGS` | CSS nav chính `a[href*='/blogs']` | Link "Luôn vui khoẻ" |
| `NAV_LINK_REWARDS` | CSS nav chính `a[href*='/vinamilk-rewards']` | Link "Nhận quà" |
| `NAV_LINK_CARE` | CSS nav chính `a[href*='/care']` | Link "Vinamilk Care" |
| `LOGO_LINK` | CSS `header a[href='/']` | Logo Vinamilk |
| `PRODUCT_MENU_BUTTON` | XPath button chứa "Sản phẩm" | Nút mở mega-menu |
| `MEGA_MENU_PANEL` | CSS `[class*='mega-menu'], [class*='megamenu']` | Panel mega-menu |
| `MEGA_MENU_CATEGORY_LINKS` | CSS `a[href*='/collections/']` | Link ngành hàng trong mega-menu |
| `MEGA_MENU_BRAND_LINKS` | CSS `a[href*='src=ALL_vendors']` | Link thương hiệu trong mega-menu |
| `MEGA_MENU_QUICK_LINK_BEST_SELLING` | CSS `a[href*='/best-selling']` | Quick link "Bán chạy" |
| `MEGA_MENU_QUICK_LINK_FLASH_SALE` | CSS `a[href*='/flash-sales']` | Quick link "Flash Sale" |
| `CART_EMPTY_MESSAGE` | XPath text chứa "Chưa có sản phẩm trong giỏ hàng" | Trạng thái giỏ hàng rỗng |
| `CART_VIEW_PRODUCTS_CTA` | XPath text chứa "Xem sản phẩm giá tốt" | CTA xem sản phẩm từ giỏ trống |
| `CART_CLOSE_BUTTON` | XPath button `aria-label` chứa "lose"/"óng" | Nút đóng drawer giỏ hàng |
| `FEATURED_NEW_PRODUCT_BANNER` | XPath `a[href*='/products/']` chứa text "Mới" | Banner sản phẩm mới nổi bật |
| `TECHNOLOGY_SECTION_CTA` | XPath section chứa "Cầu tiến là bí quyết" → link "Tìm hiểu thêm" | CTA khối công nghệ |
| `SUSTAINABILITY_SECTION_CTA` | XPath section chứa "Để tâm hành động" → link "Tìm hiểu thêm" | CTA khối bền vững |
| `SUSTAINABILITY_ARTICLES` | XPath `article` trong section "Để tâm hành động" | 3 bài viết đánh số |
| `RECRUITMENT_APPLY_CTA` | XPath `a[href*='/recruitment/career-opportunities']` | CTA "Ứng tuyển ngay" |
| `RECRUITMENT_LIFE_LINK` | CSS `a[href*='facebook.com/LifeAtVinamilk']` | Story-link "Nhịp sống Vinamilk" |
| `LIVE_CHAT_WIDGET` | XPath text chứa "Vinamilk luôn sẵn sàng hỗ trợ" | Widget live-chat |
| `LIVE_CHAT_TOGGLE_BUTTON` | CSS `[class*='chat'] button, [id*='chat'] button` | Nút mở/đóng live-chat |

## Methods (Page Object)

| Method | Signature/return | Chức năng | Locator dùng |
|---|---|---|---|
| `openSearchDialog()` | `HomePage` | Click icon tìm kiếm | `SEARCH_ICON` |
| `searchProduct(String)` | `ProductListingPage` | Mở dialog, gõ từ khoá, Enter | `SEARCH_ICON`, `SEARCH_INPUT` |
| `isSearchDialogDisplayed()` | `boolean` | Kiểm tra dialog hiển thị | `SEARCH_DIALOG` |
| `searchTrendingSuggestions()` | `List<WebElement>` | Lấy list link "Xu hướng" | `SEARCH_TRENDING_LINKS` |
| `searchSuggestedProducts()` | `List<WebElement>` | Lấy list sản phẩm gợi ý | `SEARCH_SUGGESTED_PRODUCTS` |
| `closeSearchDialog()` | `void` | Đóng dialog | `SEARCH_CLOSE_BUTTON` |
| `openAccount()` | `LoginPage` | Click icon tài khoản | `ACCOUNT_ICON` |
| `openCart()` | `CartPage` | Click icon giỏ hàng | `CART_ICON` |
| `cartItemCount()` | `String` | Đọc badge số lượng giỏ hàng | `CART_BADGE_COUNT` |
| `isMainNavDisplayed()` | `boolean` | Kiểm tra nav chính hiển thị | `MAIN_NAV` |
| `mainNavItemCount()` | `int` | Đếm số mục nav chính | `MAIN_NAV_ITEMS` |
| `isHeroSliderDisplayed()` | `boolean` | Kiểm tra hero slider hiển thị | `HERO_SLIDER` |
| `heroSlideCount()` | `int` | Đếm số slide | `HERO_SLIDE_ITEMS` |
| `goToNextHeroSlide()` | `void` | Click nút Next | `HERO_SLIDER_NEXT` |
| `goToPreviousHeroSlide()` | `void` | Click nút Previous | `HERO_SLIDER_PREV` |
| `isFeaturedProductsSectionDisplayed()` | `boolean` | Kiểm tra heading "Mời bạn sắm sửa" | `FEATURED_PRODUCTS_HEADING` |
| `awardBadgeCount()` | `int` | Đếm ảnh huy chương | `AWARD_BADGES` |
| `isFooterDisplayed()` | `boolean` | Kiểm tra footer hiển thị | `FOOTER` |
| `footerSocialLinkCount()` | `int` | Đếm icon mạng xã hội | `FOOTER_SOCIAL_LINKS` |
| `isFooterLanguageComboboxDisplayed()` | `boolean` | Kiểm tra combobox ngôn ngữ | `FOOTER_LANGUAGE_COMBOBOX` |
| `footerPolicyLinkCount()` | `int` | Đếm link chính sách | `FOOTER_POLICY_LINKS` |
| `clickFooterTermsOfUse()` | `void` | Click "Điều khoản sử dụng" | `FOOTER_TERMS_LINK` |
| `isTopUtilityBannerDisplayed()` | `boolean` | Kiểm tra banner miễn phí vận chuyển | `TOP_BAR_SHIPPING_BANNER` |
| `topUtilityLinkCount()` | `int` | Đếm link trong header | `TOP_UTILITY_LINKS` |
| `clickAlwaysVinamilkLink()` | `void` | Click "Luôn là Vinamilk" | `TOP_BAR_LINK_ALWAYS_VINAMILK` |
| `clickAlwaysInnovativeLink()` | `void` | Click "Luôn cầu tiến" | `TOP_BAR_LINK_ALWAYS_INNOVATIVE` |
| `clickStoreLink()` | `void` | Click "Cửa hàng" | `TOP_BAR_LINK_STORE` |
| `clickBlogsNavLink()` | `void` | Click "Luôn vui khoẻ" | `NAV_LINK_BLOGS` |
| `clickRewardsNavLink()` | `void` | Click "Nhận quà" | `NAV_LINK_REWARDS` |
| `clickCareNavLink()` | `void` | Click "Vinamilk Care" | `NAV_LINK_CARE` |
| `clickLogo()` | `HomePage` | Click logo, trả về instance mới | `LOGO_LINK` |
| `openProductMegaMenu()` | `void` | Click nút "Sản phẩm" | `PRODUCT_MENU_BUTTON` |
| `isProductMegaMenuDisplayed()` | `boolean` | Kiểm tra panel mega-menu | `MEGA_MENU_PANEL` |
| `megaMenuCategoryLinkCount()` | `int` | Đếm link ngành hàng | `MEGA_MENU_CATEGORY_LINKS` |
| `megaMenuBrandLinkCount()` | `int` | Đếm link thương hiệu | `MEGA_MENU_BRAND_LINKS` |
| `clickMegaMenuCategory(String)` | `void` | Click ngành hàng theo text (XPath động) | inline XPath dùng `MEGA_MENU_CATEGORY_LINKS` pattern |
| `clickMegaMenuBrand(String)` | `void` | Click thương hiệu theo text (XPath động) | inline XPath dùng `MEGA_MENU_BRAND_LINKS` pattern |
| `clickMegaMenuBestSelling()` | `void` | Click "Bán chạy" | `MEGA_MENU_QUICK_LINK_BEST_SELLING` |
| `clickMegaMenuFlashSale()` | `void` | Click "Flash Sale" | `MEGA_MENU_QUICK_LINK_FLASH_SALE` |
| `clickSearchTrendingKeyword(String)` | `void` | Click từ khoá xu hướng theo text (XPath động) | inline XPath dùng `SEARCH_TRENDING_LINKS` pattern |
| `openFirstSuggestedProduct()` | `ProductDetailPage` | Click sản phẩm gợi ý đầu tiên | `SEARCH_SUGGESTED_PRODUCTS` |
| `isCartEmptyMessageDisplayed()` | `boolean` | Kiểm tra thông báo giỏ trống | `CART_EMPTY_MESSAGE` |
| `clickCartViewProductsCta()` | `ProductListingPage` | Click CTA "Xem sản phẩm giá tốt" | `CART_VIEW_PRODUCTS_CTA` |
| `closeCartDrawer()` | `void` | Đóng drawer giỏ hàng | `CART_CLOSE_BUTTON` |
| `clickFeaturedNewProductBanner()` | `ProductDetailPage` | Click banner sản phẩm mới | `FEATURED_NEW_PRODUCT_BANNER` |
| `clickTechnologySectionCta()` | `void` | Click "Tìm hiểu thêm" khối công nghệ | `TECHNOLOGY_SECTION_CTA` |
| `clickSustainabilitySectionCta()` | `void` | Click "Tìm hiểu thêm" khối bền vững | `SUSTAINABILITY_SECTION_CTA` |
| `sustainabilityArticleCount()` | `int` | Đếm bài viết đánh số | `SUSTAINABILITY_ARTICLES` |
| `clickRecruitmentApplyCta()` | `void` | Click "Ứng tuyển ngay" | `RECRUITMENT_APPLY_CTA` |
| `clickRecruitmentLifeLink()` | `void` | Click "Nhịp sống Vinamilk" | `RECRUITMENT_LIFE_LINK` |
| `isLiveChatWidgetDisplayed()` | `boolean` | Kiểm tra widget live-chat | `LIVE_CHAT_WIDGET` |
| `toggleLiveChatWidget()` | `void` | Click nút mở/đóng chat | `LIVE_CHAT_TOGGLE_BUTTON` |

## Test Cases (đã generate)

| Test method | File | `@Feature`/`@Severity` | Spec TC | Tóm tắt assertion |
|---|---|---|---|---|
| `mainNavigationIsDisplayed` | HomePageTest | Home Page / CRITICAL | TC-HOME-03 | Nav chính hiển thị, đủ 8 mục |
| `topUtilityBarIsDisplayed` | HomePageTest | Home Page / NORMAL | TC-HOME-01 | Banner miễn phí vận chuyển hiển thị, có link |
| `clickAlwaysVinamilkNavigatesToAboutUs` | HomePageTest | Home Page / NORMAL | TC-HOME-01b | URL chứa `/about-us` |
| `clickAlwaysInnovativeNavigatesToTechnology` | HomePageTest | Home Page / NORMAL | TC-HOME-01c | URL chứa `/technology` |
| `accountIconNavigatesToLoginPage` | HomePageTest | Home Page / CRITICAL | TC-HOME-02 | URL chứa `/login`, title chứa "Đăng nhập" |
| `clickBlogsNavLinkNavigatesToBlogs` | HomePageTest | Home Page / NORMAL | TC-HOME-03b | URL chứa `/blogs` |
| `clickRewardsNavLinkNavigatesToRewards` | HomePageTest | Home Page / NORMAL | TC-HOME-03c | URL chứa `/vinamilk-rewards` |
| `clickCareNavLinkNavigatesToCare` | HomePageTest | Home Page / NORMAL | TC-HOME-03d | URL chứa `/care` |
| `clickLogoNavigatesToHome` | HomePageTest | Home Page / MINOR | TC-HOME-03e | URL == baseUrl sau khi click logo |
| `productMegaMenuShowsContentGroups` | HomePageTest | Home Page / NORMAL | TC-HOME-04 | Panel hiển thị, có category link và brand link |
| `clickMegaMenuCategoryNavigatesToCollection` | HomePageTest | Home Page / NORMAL | TC-HOME-05 | URL chứa `/collections/sua-tuoi` |
| `clickMegaMenuBestSellingNavigates` | HomePageTest | Home Page / NORMAL | TC-HOME-05b | URL chứa `/best-selling` |
| `clickMegaMenuFlashSaleNavigates` | HomePageTest | Home Page / NORMAL | TC-HOME-05c | URL chứa `/flash-sales` |
| `clickMegaMenuBrandNavigates` | HomePageTest | Home Page / MINOR | TC-HOME-05d | URL chứa `ALL_vendors:probi` |
| `heroSliderIsDisplayedAndNavigable` | HomePageTest | Home Page / NORMAL | TC-HOME-13 / TC-HOME-14 | Slider hiển thị, còn hiển thị sau Next/Previous |
| `searchDialogShowsTrendingAndSuggestions` | HomePageTest | Home Page / CRITICAL | TC-HOME-06 | Dialog mở, có trending + suggested products |
| `closeSearchDialogReturnsToHomePage` | HomePageTest | Home Page / NORMAL | TC-HOME-07 | Dialog đóng (`isSearchDialogDisplayed` false) |
| `clickTrendingKeywordNavigatesToSearchResults` | HomePageTest | Home Page / NORMAL | TC-HOME-08 | URL chứa `/search?q=Organic` |
| `clickSuggestedProductNavigatesToDetailPage` | HomePageTest | Home Page / MINOR | TC-HOME-08b | URL chứa `/products/` |
| `searchByKeywordReturnsResults` | HomePageTest | Home Page / BLOCKER | TC-HOME-09 | `resultCount() > 0` trên trang kết quả |
| `searchWithEmptyKeywordDoesNotNavigate` | HomePageTest | Home Page / MINOR | TC-HOME-10 | URL không đổi, không chứa `/search?q=` |
| `searchWithNonsenseKeywordShowsNoResults` | HomePageTest | Home Page / MINOR | TC-HOME-10b | `resultCount() == 0` |
| `cartIconOpensCartPage` | HomePageTest | Home Page / CRITICAL | TC-HOME-11 | Badge không null, thông báo giỏ trống hiển thị |
| `cartViewProductsCtaNavigatesToAllProducts` | HomePageTest | Home Page / NORMAL | TC-HOME-12 | URL chứa `/collections/all-products` |
| `closeCartDrawerReturnsToHomePage` | HomePageTest | Home Page / MINOR | TC-HOME-12b | Thông báo giỏ trống không còn hiển thị |
| `awardBadgesAreDisplayed` | HomePageTest | Home Page / MINOR | TC-HOME-15 | `awardBadgeCount() > 0` |
| `clickFeaturedNewProductBannerNavigatesToDetailPage` | HomePageTest | Home Page / NORMAL | TC-HOME-16 | URL chứa `/products/` |
| `featuredProductsSectionIsDisplayed` | HomePageTest | Home Page / NORMAL | TC-HOME-17 | Heading "sắm sửa" hiển thị |
| `technologySectionCtaNavigatesToTechnology` | HomePageTest | Home Page / NORMAL | TC-HOME-18 | URL chứa `/technology` |
| `sustainabilitySectionCtaNavigatesToSustainability` | HomePageTest | Home Page / NORMAL | TC-HOME-19 | URL chứa `/sustainability` |
| `sustainabilitySectionShowsThreeArticles` | HomePageTest | Home Page / MINOR | TC-HOME-19b | `sustainabilityArticleCount() == 3` |
| `recruitmentApplyCtaNavigatesToCareerOpportunities` | HomePageTest | Home Page / MINOR | TC-HOME-20 | URL chứa `/recruitment/career-opportunities` |
| `recruitmentLifeLinkOpensFacebookPage` | HomePageTest | Home Page / MINOR | TC-HOME-20b | URL tab mới chứa `facebook.com/LifeAtVinamilk` |
| `footerIsDisplayedWithSocialLinks` | HomePageTest | Home Page / NORMAL | TC-HOME-21 | Footer hiển thị, social/policy link, combobox ngôn ngữ |
| `footerTermsOfUseLinkNavigates` | HomePageTest | Home Page / MINOR | TC-HOME-21b | URL chứa `/support/terms-of-use` |
| `liveChatWidgetIsDisplayedAndToggleable` | HomePageTest | Home Page / MINOR | TC-HOME-22 | Widget hiển thị, toggle không lỗi |
| `storeLinkNavigatesToStoreLocator` | HomePageTest | Home Page / NORMAL | TC-HOME-23 | Title chứa "Danh sách cửa hàng" |

## Coverage vs. spec

Spec (`docs/features/home-page.md`) liệt kê **31 TC-HOME-xx** (bao gồm cả các mã phụ `-b`, `-c`, `-d`). Đối chiếu:

- Tất cả 31 mã trong spec đều có test tương ứng: TC-HOME-01, 01b, 01c, 02, 03, 03b, 03c, 03d, 03e, 04, 05, 05b, 05c, 05d, 06, 07, 08, 08b, 09, 10, 10b, 11, 12, 12b, 13, 14, 15, 16, 17, 18, 19, 19b, 20, 20b, 21, 21b, 22, 23.
- Không có test method nào thừa (không map vào spec) — toàn bộ 29 test method (một số gộp 2 TC như `heroSliderIsDisplayedAndNavigable` phủ cả TC-HOME-13 và 14) đều bám sát mục lục spec.
- **Kết luận: 31/31 TC trong spec đã được generate code, không thiếu mã nào.**

## Giải thích chi tiết từng đoạn code

### `HomePage.java`

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:9-75`
```java
public class HomePage extends BasePage {

    private static final By ACCOUNT_ICON = By.cssSelector("header nav button:nth-of-type(1)");
    ...
    public HomePage(WebDriver driver) {
        super(driver);
    }
```
Khối đầu file khai báo toàn bộ 48 locator dưới dạng `private static final By`. Việc gom locator thành hằng số static giúp tách biệt "tìm phần tử ở đâu" khỏi "làm gì với phần tử", để khi UI đổi selector chỉ cần sửa một chỗ. Constructor chỉ gọi `super(driver)` để lưu `WebDriver` dùng chung qua `BasePage`.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:80-94`
```java
public HomePage openSearchDialog() {
    click(SEARCH_ICON);
    return this;
}

public ProductListingPage searchProduct(String keyword) {
    openSearchDialog();
    type(SEARCH_INPUT, keyword);
    driver.findElement(SEARCH_INPUT).sendKeys(Keys.ENTER);
    return new ProductListingPage(driver);
}

public boolean isSearchDialogDisplayed() {
    return isDisplayed(SEARCH_DIALOG);
}
```
`openSearchDialog()` click icon tìm kiếm (dùng `click()` kế thừa từ `BasePage`, có `WebDriverWait` chờ phần tử clickable trước khi bấm, tránh lỗi click vào phần tử chưa kịp render). `searchProduct()` tái sử dụng `openSearchDialog()`, gõ từ khoá qua `type()` (chờ visible + `clear()` trước khi `sendKeys`), rồi gửi thêm phím `Enter` trực tiếp qua `driver.findElement` vì hành động submit bằng phím không nằm trong các helper chung của `BasePage`. Trả về `ProductListingPage` mới vì sau khi Enter, trình duyệt điều hướng sang trang kết quả tìm kiếm — đây là bước điều hướng dùng trong TC-HOME-09/10/10b. `isSearchDialogDisplayed()` dùng `isDisplayed()` của `BasePage`, tự bắt `TimeoutException` và trả `false` thay vì ném lỗi — phù hợp để assert "dialog đã đóng" (TC-HOME-07) mà không cần try/catch ở tầng test.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:96-106`
```java
public List<WebElement> searchTrendingSuggestions() {
    return driver.findElements(SEARCH_TRENDING_LINKS);
}

public List<WebElement> searchSuggestedProducts() {
    return driver.findElements(SEARCH_SUGGESTED_PRODUCTS);
}

public void closeSearchDialog() {
    click(SEARCH_CLOSE_BUTTON);
}
```
Hai method đầu dùng `findElements` (số nhiều, không chờ) để lấy danh sách link — vì mục đích chỉ là đếm số lượng (`size() > 0` trong TC-HOME-06), không cần thao tác từng phần tử nên không cần `WaitUtils`. `closeSearchDialog()` click nút Đóng, dùng trong TC-HOME-07.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:108-120`
```java
public LoginPage openAccount() {
    click(ACCOUNT_ICON);
    return new LoginPage(driver);
}

public CartPage openCart() {
    click(CART_ICON);
    return new CartPage(driver);
}

public String cartItemCount() {
    return textOf(CART_BADGE_COUNT);
}
```
`openAccount()` và `openCart()` là các method điều hướng: click icon rồi trả về Page Object của trang/khu vực tiếp theo (TC-HOME-02, TC-HOME-11). `cartItemCount()` đọc text badge số lượng qua `textOf()` (kế thừa từ `BasePage`, chờ visible rồi `getText()`), dùng để assert badge luôn hiển thị số (kể cả "0") mà không quan tâm giá trị cụ thể.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:122-136`
```java
public boolean isMainNavDisplayed() {
    return isDisplayed(MAIN_NAV);
}

public int mainNavItemCount() {
    return driver.findElements(MAIN_NAV_ITEMS).size();
}

public boolean isHeroSliderDisplayed() {
    return isDisplayed(HERO_SLIDER);
}

public int heroSlideCount() {
    return driver.findElements(HERO_SLIDE_ITEMS).size();
}
```
Cặp method lặp lại một khuôn mẫu xuyên suốt file: một method `isXDisplayed()` kiểm tra hiển thị và một method `xCount()` đếm số phần tử con, phục vụ các assertion dạng "khối X hiển thị và có đúng N mục" (TC-HOME-03, TC-HOME-13).

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:138-144`
```java
public void goToNextHeroSlide() {
    click(HERO_SLIDER_NEXT);
}

public void goToPreviousHeroSlide() {
    click(HERO_SLIDER_PREV);
}
```
Hai action điều khiển carousel, dùng trong TC-HOME-13/14 để xác nhận slider vẫn hiển thị sau khi chuyển slide.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:146-168`
```java
public boolean isFeaturedProductsSectionDisplayed() { ... }
public int awardBadgeCount() { ... }
public boolean isFooterDisplayed() { ... }
public int footerSocialLinkCount() { ... }
public boolean isFooterLanguageComboboxDisplayed() { ... }
public int footerPolicyLinkCount() { ... }
public void clickFooterTermsOfUse() { ... }
```
Nhóm getter/action cho khối "Mời bạn sắm sửa", award badges và footer — theo cùng khuôn mẫu isDisplayed/count/click đã nêu, phục vụ TC-HOME-15, 17, 21, 21b.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:174-204`
```java
public boolean isTopUtilityBannerDisplayed() { ... }
public int topUtilityLinkCount() { ... }
public void clickAlwaysVinamilkLink() { ... }
public void clickAlwaysInnovativeLink() { ... }
public void clickStoreLink() { ... }
public void clickBlogsNavLink() { ... }
public void clickRewardsNavLink() { ... }
public void clickCareNavLink() { ... }
```
Nhóm method cho top utility bar và các link điều hướng trong nav chính (TC-HOME-01, 01b, 01c, 03b, 03c, 03d) — mỗi method chỉ click một locator cố định rồi để test assert URL ở tầng gọi.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:206-209`
```java
public HomePage clickLogo() {
    click(LOGO_LINK);
    return new HomePage(driver);
}
```
Click logo và trả về **instance `HomePage` mới** thay vì `this` — vì sau khi điều hướng về `/`, DOM được load lại hoàn toàn nên các tham chiếu WebElement cũ (nếu Selenium cache) không còn hợp lệ; tạo instance mới đảm bảo các lần gọi tiếp theo tìm lại phần tử trên DOM hiện tại. Dùng trong TC-HOME-03e.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:211-243`
```java
public void openProductMegaMenu() { ... }
public boolean isProductMegaMenuDisplayed() { ... }
public int megaMenuCategoryLinkCount() { ... }
public int megaMenuBrandLinkCount() { ... }
public void clickMegaMenuCategory(String categoryText) {
    driver.findElement(By.xpath(
            "//a[contains(@href, '/collections/') and contains(., '" + categoryText + "')]")).click();
}

public void clickMegaMenuBrand(String brandText) {
    driver.findElement(By.xpath(
            "//a[contains(@href, 'src=ALL_vendors') and contains(., '" + brandText + "')]")).click();
}

public void clickMegaMenuBestSelling() { ... }
public void clickMegaMenuFlashSale() { ... }
```
Nhóm method cho mega-menu "Sản phẩm" (TC-HOME-04, 05, 05b, 05c, 05d). Điểm đáng chú ý: `clickMegaMenuCategory` và `clickMegaMenuBrand` không dùng locator hằng số mà **build XPath động** bằng cách nối chuỗi `categoryText`/`brandText` vào biểu thức `contains(., '...')`, vì mega-menu có nhiều link cùng dạng href và cần chọn đúng link theo text hiển thị (ví dụ "sữa tươi" hay "probi") — điều mà một locator tĩnh duy nhất không biểu diễn được.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:245-253`
```java
public void clickSearchTrendingKeyword(String keyword) {
    driver.findElement(By.xpath(
            "//div[@role='dialog']//a[contains(@href, '/search?q=') and contains(., '" + keyword + "')]")).click();
}

public ProductDetailPage openFirstSuggestedProduct() {
    driver.findElements(SEARCH_SUGGESTED_PRODUCTS).get(0).click();
    return new ProductDetailPage(driver);
}
```
Cùng kỹ thuật XPath động như trên để click đúng từ khoá xu hướng theo text (TC-HOME-08). `openFirstSuggestedProduct()` lấy phần tử đầu tiên trong danh sách gợi ý (`get(0)`) — chấp nhận test luôn nhắm vào sản phẩm gợi ý đầu tiên bất kể nội dung cụ thể, dùng cho TC-HOME-08b.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:255-271`
```java
public boolean isCartEmptyMessageDisplayed() { ... }
public ProductListingPage clickCartViewProductsCta() { ... }
public void closeCartDrawer() { ... }
public ProductDetailPage clickFeaturedNewProductBanner() { ... }
```
Nhóm method cho drawer giỏ hàng rỗng (TC-HOME-11, 12, 12b) và banner sản phẩm mới (TC-HOME-16), theo mẫu getter/click/điều hướng đã mô tả.

`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/HomePage.java:273-299`
```java
public void clickTechnologySectionCta() { ... }
public void clickSustainabilitySectionCta() { ... }
public int sustainabilityArticleCount() { ... }
public void clickRecruitmentApplyCta() { ... }
public void clickRecruitmentLifeLink() { ... }
public boolean isLiveChatWidgetDisplayed() { ... }
public void toggleLiveChatWidget() { ... }
```
Phần cuối file phủ các khối nội dung còn lại: CTA công nghệ/bền vững, đếm bài viết bền vững, CTA tuyển dụng, story-link Facebook, và widget live-chat (TC-HOME-18, 19, 19b, 20, 20b, 22). Không có logic đặc biệt — tất cả đều gọi `click()`/`isDisplayed()`/`findElements().size()` kế thừa từ `BasePage`.

### `HomePageTest.java`

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:16-26`
```java
@Epic("Storefront")
@Feature("Home Page")
public class HomePageTest extends BaseUiTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void openHomePage() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl"));
        homePage = new HomePage(driver);
    }
```
`@Epic`/`@Feature` là annotation của Allure dùng để phân loại report theo cây Epic → Feature, giúp báo cáo test hiển thị "Storefront > Home Page". `@BeforeMethod(alwaysRun = true)` là hook TestNG chạy trước **mỗi** test method (kể cả khi test trước đó fail, nhờ `alwaysRun = true`) — mở lại trang chủ từ `ui.baseUrl` (đọc qua `ConfigManager`, singleton đọc config môi trường) và khởi tạo `homePage` mới, đảm bảo mỗi test bắt đầu từ trạng thái sạch, không phụ thuộc test trước.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:28-43`
```java
@Test(description = "Main navigation menu renders all top-level entries")
@Severity(SeverityLevel.CRITICAL)
@Description("TC-HOME-03: ...")
public void mainNavigationIsDisplayed() {
    Assert.assertTrue(homePage.isMainNavDisplayed(), "Main navigation should be visible on the home page");
    Assert.assertEquals(homePage.mainNavItemCount(), 8, "Expected 8 top-level navigation entries");
}

@Test(description = "Top utility bar renders shipping banner, links and icons")
@Severity(SeverityLevel.NORMAL)
@Description("TC-HOME-01: ...")
public void topUtilityBarIsDisplayed() {
    Assert.assertTrue(homePage.isTopUtilityBannerDisplayed(), "Free-shipping banner should be visible");
    Assert.assertTrue(homePage.topUtilityLinkCount() > 0, "Expected top utility bar links");
}
```
`@Test(description = ...)` gắn mô tả hiển thị trong report; `@Severity` (Allure) đánh dấu mức độ nghiêm trọng nếu test fail — dùng để ưu tiên điều tra khi có nhiều test fail cùng lúc. `@Description` nhúng mã TC từ spec để truy vết ngược. `mainNavigationIsDisplayed` assert cứng số lượng mục nav là 8 — khớp đúng số mục liệt kê trong spec mục 3. `topUtilityBarIsDisplayed` chỉ assert `> 0` cho số link (không đếm chính xác số lượng) vì header có nhiều link (bao gồm cả icon) khó chốt một con số cố định.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:45-59`
```java
public void clickAlwaysVinamilkNavigatesToAboutUs() {
    homePage.clickAlwaysVinamilkLink();
    Assert.assertTrue(homePage.currentUrl().contains("/about-us"), "Expected navigation to /about-us");
}

public void clickAlwaysInnovativeNavigatesToTechnology() {
    homePage.clickAlwaysInnovativeLink();
    Assert.assertTrue(homePage.currentUrl().contains("/technology"), "Expected navigation to /technology");
}
```
Hai test điều hướng đơn giản: click link rồi assert URL hiện tại (`currentUrl()` từ `BasePage`, gọi `driver.getCurrentUrl()`) chứa đường dẫn mong đợi. Dùng `contains` thay vì so khớp tuyệt đối để không phụ thuộc domain/query string cụ thể.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:61-68`
```java
public void accountIconNavigatesToLoginPage() {
    homePage.openAccount();
    Assert.assertTrue(homePage.currentUrl().contains("/login"), "Expected navigation to /login");
    Assert.assertTrue(homePage.pageTitle().contains("Đăng nhập"), "Expected login page title");
}
```
`openAccount()` trả về `LoginPage` nhưng test không gán lại biến — vẫn gọi `homePage.currentUrl()`/`pageTitle()` vì các method này đọc trực tiếp từ `driver` (không phụ thuộc trạng thái riêng của `HomePage`), nên gọi trên instance cũ vẫn cho kết quả đúng của trang hiện tại. Assert kép: vừa kiểm tra URL vừa kiểm tra title trang đăng nhập, tăng độ tin cậy so với chỉ check URL.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:70-102`
```java
public void clickBlogsNavLinkNavigatesToBlogs() { ... }
public void clickRewardsNavLinkNavigatesToRewards() { ... }
public void clickCareNavLinkNavigatesToCare() { ... }

public void clickLogoNavigatesToHome() {
    homePage.clickBlogsNavLink();
    homePage = homePage.clickLogo();
    Assert.assertEquals(homePage.currentUrl(), ConfigManager.getInstance().get("ui.baseUrl"),
            "Expected navigation back to the home page");
}
```
Ba test đầu lặp lại khuôn mẫu click-link-rồi-assert-URL cho các mục nav còn lại (TC-HOME-03b/c/d). `clickLogoNavigatesToHome` cố ý điều hướng sang `/blogs` trước để đảm bảo test thực sự xác minh việc "quay lại" trang chủ (nếu test bắt đầu sẵn ở trang chủ thì click logo sẽ không kiểm chứng được gì); sau đó gán lại `homePage = homePage.clickLogo()` để cập nhật instance mới (theo đúng thiết kế trả về `HomePage` mới đã giải thích ở trên) và assert URL bằng đúng `ui.baseUrl` (so khớp tuyệt đối, không phải `contains`) vì đây là trang gốc.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:104-150`
```java
public void productMegaMenuShowsContentGroups() {
    homePage.openProductMegaMenu();
    Assert.assertTrue(homePage.isProductMegaMenuDisplayed(), "Product mega-menu panel should be visible");
    Assert.assertTrue(homePage.megaMenuCategoryLinkCount() > 0, "Expected category links in the mega-menu");
    Assert.assertTrue(homePage.megaMenuBrandLinkCount() > 0, "Expected brand links in the mega-menu");
}

public void clickMegaMenuCategoryNavigatesToCollection() {
    homePage.openProductMegaMenu();
    homePage.clickMegaMenuCategory("sữa tươi");
    Assert.assertTrue(homePage.currentUrl().contains("/collections/sua-tuoi"),
            "Expected navigation to the sua-tuoi collection");
}
```
`productMegaMenuShowsContentGroups` (TC-HOME-04) chỉ assert số lượng `> 0` cho category/brand links thay vì đúng 13/10 như spec mô tả — chấp nhận sai số vì nội dung mega-menu có thể thay đổi theo thời gian (thêm/bớt ngành hàng), assert lỏng hơn giúp test bền vững hơn nhưng đồng nghĩa không phát hiện được nếu một nhóm bị mất hoàn toàn số lượng dự kiến. `clickMegaMenuCategoryNavigatesToCollection` mở mega-menu rồi gọi `clickMegaMenuCategory("sữa tươi")` — text tiếng Việt được truyền trực tiếp khớp với XPath động đã nêu ở Page Object.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:124-150`
```java
public void clickMegaMenuBestSellingNavigates() { ... }
public void clickMegaMenuFlashSaleNavigates() { ... }

public void clickMegaMenuBrandNavigates() {
    homePage.openProductMegaMenu();
    homePage.clickMegaMenuBrand("probi");
    Assert.assertTrue(homePage.currentUrl().contains("ALL_vendors:probi"),
            "Expected navigation to the probi vendor collection");
}
```
Tiếp tục nhóm test mega-menu cho quick link Bán chạy/Flash Sale (TC-HOME-05b/c) và thương hiệu "probi" (TC-HOME-05d) — cùng khuôn mẫu mở menu rồi click rồi assert URL.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:152-165`
```java
public void heroSliderIsDisplayedAndNavigable() {
    Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should be visible on load");
    int initialSlideCount = homePage.heroSlideCount();
    Assert.assertTrue(initialSlideCount > 0, "Expected at least one hero slide");

    homePage.goToNextHeroSlide();
    Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should remain visible after Next");

    homePage.goToPreviousHeroSlide();
    Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should remain visible after Previous");
}
```
Gộp TC-HOME-13 và TC-HOME-14 vào một test. Điểm đáng chú ý: test **không thực sự xác minh nội dung slide thay đổi** sau khi Next/Previous — chỉ assert slider "vẫn hiển thị", đây là smoke-check yếu hơn kỳ vọng của spec ("slide tiếp theo hiển thị đúng"). Biến `initialSlideCount` được lấy nhưng không dùng để so sánh gì sau đó ngoài việc assert `> 0`.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:167-231`
```java
public void searchDialogShowsTrendingAndSuggestions() {
    homePage.openSearchDialog();
    Assert.assertTrue(homePage.isSearchDialogDisplayed(), ...);
    Assert.assertTrue(homePage.searchTrendingSuggestions().size() > 0, ...);
    Assert.assertTrue(homePage.searchSuggestedProducts().size() > 0, ...);
}

public void closeSearchDialogReturnsToHomePage() {
    homePage.openSearchDialog();
    homePage.closeSearchDialog();
    Assert.assertFalse(homePage.isSearchDialogDisplayed(), "Search dialog should be closed");
}

public void clickTrendingKeywordNavigatesToSearchResults() {
    homePage.openSearchDialog();
    homePage.clickSearchTrendingKeyword("Organic");
    Assert.assertTrue(homePage.currentUrl().contains("/search?q=Organic"), ...);
}

public void clickSuggestedProductNavigatesToDetailPage() {
    homePage.openSearchDialog();
    homePage.openFirstSuggestedProduct();
    Assert.assertTrue(homePage.currentUrl().contains("/products/"), ...);
}

public void searchByKeywordReturnsResults() {
    ProductListingPage results = homePage.searchProduct("Sua tuoi");
    Assert.assertTrue(results.resultCount() > 0, ...);
}

public void searchWithEmptyKeywordDoesNotNavigate() {
    String beforeUrl = homePage.currentUrl();
    homePage.searchProduct("");
    Assert.assertFalse(homePage.currentUrl().contains("/search?q="), ...);
    Assert.assertEquals(homePage.currentUrl(), beforeUrl, "Expected to stay on the home page");
}

public void searchWithNonsenseKeywordShowsNoResults() {
    ProductListingPage results = homePage.searchProduct("xyzxyz123");
    Assert.assertEquals(results.resultCount(), 0, ...);
}
```
Toàn bộ nhóm test tìm kiếm (TC-HOME-06 đến 10b). `searchByKeywordReturnsResults` gọi `searchProduct("Sua tuoi")` không dấu (khác với spec ghi "sữa tươi" có dấu) — chấp nhận vì trang tìm kiếm khớp gần đúng (fuzzy) nên vẫn ra kết quả, nhưng đây là điểm lệch nhẹ so với ví dụ cụ thể trong spec. `searchWithEmptyKeywordDoesNotNavigate` lưu `beforeUrl` trước khi gọi `searchProduct("")` rồi assert URL không đổi — đây là cách duy nhất trong file kiểm tra "không có việc gì xảy ra" bằng so sánh trạng thái trước/sau thay vì chỉ check điều kiện dương.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:233-259`
```java
public void cartIconOpensCartPage() {
    Assert.assertNotNull(homePage.cartItemCount(), "Cart badge should display an item count, even when zero");
    homePage.openCart();
    Assert.assertTrue(homePage.isCartEmptyMessageDisplayed(), "Expected the empty-cart message to be displayed");
}

public void cartViewProductsCtaNavigatesToAllProducts() {
    homePage.openCart();
    homePage.clickCartViewProductsCta();
    Assert.assertTrue(homePage.currentUrl().contains("/collections/all-products"), ...);
}

public void closeCartDrawerReturnsToHomePage() {
    homePage.openCart();
    homePage.closeCartDrawer();
    Assert.assertFalse(homePage.isCartEmptyMessageDisplayed(), "Expected the cart drawer to be closed");
}
```
Nhóm test giỏ hàng (TC-HOME-11, 12, 12b). `cartIconOpensCartPage` chỉ assert badge `assertNotNull` (không kiểm tra giá trị cụ thể là "0") — vì mục tiêu chỉ là xác nhận badge luôn có text, không phải xác minh số lượng đúng.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:261-305`
```java
public void awardBadgesAreDisplayed() {
    Assert.assertTrue(homePage.awardBadgeCount() > 0, ...);
}

public void clickFeaturedNewProductBannerNavigatesToDetailPage() {
    homePage.clickFeaturedNewProductBanner();
    Assert.assertTrue(homePage.currentUrl().contains("/products/"), ...);
}

public void featuredProductsSectionIsDisplayed() {
    Assert.assertTrue(homePage.isFeaturedProductsSectionDisplayed(), ...);
}

public void technologySectionCtaNavigatesToTechnology() {
    homePage.clickTechnologySectionCta();
    Assert.assertTrue(homePage.currentUrl().contains("/technology"), ...);
}

public void sustainabilitySectionCtaNavigatesToSustainability() {
    homePage.clickSustainabilitySectionCta();
    Assert.assertTrue(homePage.currentUrl().contains("/sustainability"), ...);
}

public void sustainabilitySectionShowsThreeArticles() {
    Assert.assertEquals(homePage.sustainabilityArticleCount(), 3, ...);
}
```
Nhóm test cho award badges (TC-HOME-15), banner sản phẩm mới (TC-HOME-16), khối "Mời bạn sắm sửa" (TC-HOME-17), CTA công nghệ/bền vững (TC-HOME-18/19) và số bài viết bền vững (TC-HOME-19b, assert đúng bằng 3 vì spec khẳng định cố định 3 bài).

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:307-329`
```java
public void recruitmentApplyCtaNavigatesToCareerOpportunities() {
    homePage.clickRecruitmentApplyCta();
    Assert.assertTrue(homePage.currentUrl().contains("/recruitment/career-opportunities"), ...);
}

public void recruitmentLifeLinkOpensFacebookPage() {
    String originalWindow = driver.getWindowHandle();
    homePage.clickRecruitmentLifeLink();
    for (String handle : driver.getWindowHandles()) {
        if (!handle.equals(originalWindow)) {
            driver.switchTo().window(handle);
        }
    }
    Assert.assertTrue(driver.getCurrentUrl().contains("facebook.com/LifeAtVinamilk"), ...);
}
```
`recruitmentApplyCtaNavigatesToCareerOpportunities` là test điều hướng thông thường (TC-HOME-20). `recruitmentLifeLinkOpensFacebookPage` (TC-HOME-20b) xử lý trường hợp link mở **tab mới**: lưu `originalWindow` (handle của tab hiện tại) trước khi click, sau đó lặp qua `driver.getWindowHandles()` (tất cả các tab đang mở) để tìm handle khác với tab gốc rồi `switchTo().window(handle)` chuyển driver sang tab mới — đây là kỹ thuật bắt buộc trong Selenium vì driver mặc định vẫn "nhìn" vào tab cũ cho tới khi được lệnh chuyển. Test này thao tác trực tiếp qua `driver` thay vì qua Page Object vì việc quản lý window handle là hành vi cấp trình duyệt, không thuộc trách nhiệm của `HomePage`.

`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/HomePageTest.java:331-364`
```java
public void footerIsDisplayedWithSocialLinks() {
    Assert.assertTrue(homePage.isFooterDisplayed(), ...);
    Assert.assertTrue(homePage.footerSocialLinkCount() > 0, ...);
    Assert.assertTrue(homePage.isFooterLanguageComboboxDisplayed(), ...);
    Assert.assertTrue(homePage.footerPolicyLinkCount() > 0, ...);
}

public void footerTermsOfUseLinkNavigates() {
    homePage.clickFooterTermsOfUse();
    Assert.assertTrue(homePage.currentUrl().contains("/support/terms-of-use"), ...);
}

public void liveChatWidgetIsDisplayedAndToggleable() {
    Assert.assertTrue(homePage.isLiveChatWidgetDisplayed(), ...);
    homePage.toggleLiveChatWidget();
}

public void storeLinkNavigatesToStoreLocator() {
    homePage.clickStoreLink();
    Assert.assertTrue(homePage.pageTitle().contains("Danh sách cửa hàng"), ...);
}
```
Nhóm test cuối file: footer (TC-HOME-21, gộp 4 assertion cho 1 TC vì spec mô tả nhiều thành phần cùng một "Test Case"), link điều khoản sử dụng (TC-HOME-21b), live-chat (TC-HOME-22 — chỉ gọi `toggleLiveChatWidget()` sau khi assert hiển thị nhưng **không assert lại trạng thái sau toggle**, nên test không thực sự xác minh việc mở/đóng có hoạt động), và link cửa hàng (TC-HOME-23, dùng `pageTitle()` thay vì URL vì URL đích `/store-list` không được assert trực tiếp — có thể do domain redirect).

## Nhận xét / rủi ro

- **Locator dựa vào cấu trúc DOM (`nth-of-type`, `:last-of-type`)**: `ACCOUNT_ICON`, `SEARCH_ICON`, `CART_ICON`, `CART_BADGE_COUNT`, `HERO_SLIDER_PREV`/`NEXT` dùng vị trí thứ tự trong `header nav` — nếu thứ tự icon trên UI thay đổi (thêm icon mới, đổi vị trí), các locator này sẽ trỏ sai phần tử mà không báo lỗi rõ ràng.
- **Locator XPath dựa vào text tiếng Việt cứng** (`contains(., 'sắm sửa')`, `'Cầu tiến là bí quyết'`, `'Để tâm hành động'`, `'Chưa có sản phẩm trong giỏ hàng'`, `'Vinamilk luôn sẵn sàng hỗ trợ'`...): dễ vỡ khi content team đổi câu chữ, và không hỗ trợ đa ngôn ngữ nếu trang thêm i18n.
- **`CART_ICON` có selector fallback kép** (`button[aria-label*='cart'], header nav >* button:last-of-type`) — dấu hiệu selector chính không ổn định, phải fallback bằng vị trí; cùng rủi ro giòn như trên.
- **`clickMegaMenuCategory`/`clickMegaMenuBrand`/`clickSearchTrendingKeyword` nối chuỗi trực tiếp text người dùng truyền vào XPath** — không phải lỗ hổng bảo mật (chỉ chạy nội bộ trong test, không nhận input từ người dùng thật), nhưng nếu `categoryText`/`brandText`/`keyword` chứa dấu nháy đơn (`'`) sẽ làm hỏng cú pháp XPath và ném exception khó hiểu.
- **`heroSliderIsDisplayedAndNavigable`, `liveChatWidgetIsDisplayedAndToggleable`** chỉ assert phần tử "vẫn hiển thị"/"không lỗi" sau hành động, không xác minh nội dung/trạng thái thực sự thay đổi (slide khác, widget mở/đóng) — rủi ro bỏ sót lỗi nếu nút Next/Previous hoặc toggle không hoạt động nhưng phần tử cha vẫn hiển thị.
- **Không có test phủ nhánh lỗi/edge-case ngoài tìm kiếm** (TC-HOME-10, 10b là các case âm duy nhất) — các khối còn lại (mega-menu, cart, footer...) chỉ có test đường đi thành công (happy path), chưa có case như mega-menu rỗng, giỏ hàng có sản phẩm, hay footer thiếu nhóm link.
- **`productMegaMenuShowsContentGroups` assert lỏng (`> 0`)** thay vì đúng số lượng category/brand nêu trong spec (13/10) — sẽ không phát hiện nếu một nhóm mất phần lớn mục nhưng vẫn còn ≥ 1.
