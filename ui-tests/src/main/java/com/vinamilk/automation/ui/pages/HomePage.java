package com.vinamilk.automation.ui.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {

    private static final By ACCOUNT_ICON = By.cssSelector("header nav button:nth-of-type(1)");
    private static final By SEARCH_ICON = By.cssSelector("header nav button:nth-of-type(2)");
    private static final By CART_ICON = By.cssSelector("header nav button[aria-label*='cart'], header nav >* button:last-of-type");
    private static final By CART_BADGE_COUNT = By.cssSelector("header nav button:last-of-type");

    private static final By SEARCH_DIALOG = By.cssSelector("[role='dialog']");
    private static final By SEARCH_INPUT = By.xpath("//div[@role='dialog']//input[@type='text' or @type='search']");
    private static final By SEARCH_CLOSE_BUTTON = By.xpath("//div[@role='dialog']//button[contains(., 'Đóng')]");
    private static final By SEARCH_TRENDING_LINKS = By.xpath("//div[@role='dialog']//a[contains(@href, '/search?q=')]");
    private static final By SEARCH_SUGGESTED_PRODUCTS = By.xpath("//div[@role='dialog']//a[contains(@href, '/products/')]");

    private static final By MAIN_NAV = By.cssSelector("nav[aria-label='Main']");
    private static final By MAIN_NAV_ITEMS = By.cssSelector("nav[aria-label='Main'] li");
    private static final By TOP_UTILITY_LINKS = By.cssSelector("header a");

    private static final By HERO_SLIDER = By.cssSelector("main section:first-of-type [role='group'], main section:first-of-type button img");
    private static final By HERO_SLIDE_ITEMS = By.cssSelector("main section:first-of-type [role='group']");
    private static final By HERO_SLIDER_PREV = By.cssSelector("main section:first-of-type button:first-of-type");
    private static final By HERO_SLIDER_NEXT = By.cssSelector("main section:first-of-type button:last-of-type");

    private static final By AWARD_BADGES = By.cssSelector("main img[src*='award']");
    private static final By FEATURED_PRODUCTS_HEADING = By.xpath("//h2[contains(., 'sắm sửa')]");
    private static final By FOOTER = By.cssSelector("footer, [class*=footer]");
    private static final By FOOTER_SOCIAL_LINKS = By.cssSelector("footer a[href*='facebook.com'], footer a[href*='instagram.com'], footer a[href*='tiktok.com'], footer a[href*='youtube.com'], footer a[href*='linkedin.com']");
    private static final By FOOTER_LANGUAGE_COMBOBOX = By.cssSelector("footer select, footer [role='combobox']");
    private static final By FOOTER_TERMS_LINK = By.cssSelector("footer a[href*='/support/terms-of-use']");
    private static final By FOOTER_POLICY_LINKS = By.cssSelector(
            "footer a[href*='/support/terms-of-use'], footer a[href*='/support/privacy'], "
                    + "footer a[href*='quy-che'], footer a[href*='khieu-nai']");

    private static final By TOP_BAR_SHIPPING_BANNER = By.xpath("//header//*[contains(text(), 'Miễn phí vận chuyển')]");
    private static final By TOP_BAR_LINK_ALWAYS_VINAMILK = By.cssSelector("header a[href*='/about-us']");
    private static final By TOP_BAR_LINK_ALWAYS_INNOVATIVE = By.cssSelector("header a[href*='/technology']");
    private static final By TOP_BAR_LINK_STORE = By.cssSelector("header a[href*='/store-list']");

    private static final By NAV_LINK_BLOGS = By.cssSelector("nav[aria-label='Main'] a[href*='/blogs']");
    private static final By NAV_LINK_REWARDS = By.cssSelector("nav[aria-label='Main'] a[href*='/vinamilk-rewards']");
    private static final By NAV_LINK_CARE = By.cssSelector("nav[aria-label='Main'] a[href*='/care']");
    private static final By LOGO_LINK = By.cssSelector("header a[href='/']");

    private static final By PRODUCT_MENU_BUTTON = By.xpath("//header//button[contains(., 'Sản phẩm')]");
    private static final By MEGA_MENU_PANEL = By.cssSelector("[class*='mega-menu'], [class*='megamenu']");
    private static final By MEGA_MENU_CATEGORY_LINKS = By.cssSelector("a[href*='/collections/']");
    private static final By MEGA_MENU_BRAND_LINKS = By.cssSelector("a[href*='src=ALL_vendors']");
    private static final By MEGA_MENU_QUICK_LINK_BEST_SELLING = By.cssSelector("a[href*='/best-selling']");
    private static final By MEGA_MENU_QUICK_LINK_FLASH_SALE = By.cssSelector("a[href*='/flash-sales']");

    private static final By CART_EMPTY_MESSAGE = By.xpath("//*[contains(text(), 'Chưa có sản phẩm trong giỏ hàng')]");
    private static final By CART_VIEW_PRODUCTS_CTA = By.xpath("//a[contains(., 'Xem sản phẩm giá tốt')]");
    private static final By CART_CLOSE_BUTTON = By.xpath("//div[@role='dialog']//button[contains(@aria-label, 'lose') or contains(@aria-label, 'óng')]");

    private static final By FEATURED_NEW_PRODUCT_BANNER = By.xpath("//a[contains(@href, '/products/') and .//*[contains(text(), 'Mới')]]");

    private static final By TECHNOLOGY_SECTION_CTA = By.xpath(
            "//section[.//*[contains(text(), 'Cầu tiến là bí quyết')]]//a[contains(., 'Tìm hiểu thêm')]");
    private static final By SUSTAINABILITY_SECTION_CTA = By.xpath(
            "//section[.//*[contains(text(), 'Để tâm hành động')]]//a[contains(., 'Tìm hiểu thêm')]");
    private static final By SUSTAINABILITY_ARTICLES = By.xpath(
            "//section[.//*[contains(text(), 'Để tâm hành động')]]//article");
    private static final By RECRUITMENT_APPLY_CTA = By.xpath("//a[contains(@href, '/recruitment/career-opportunities')]");
    private static final By RECRUITMENT_LIFE_LINK = By.cssSelector("a[href*='facebook.com/LifeAtVinamilk']");

    private static final By LIVE_CHAT_WIDGET = By.xpath("//*[contains(text(), 'Vinamilk luôn sẵn sàng hỗ trợ')]");
    private static final By LIVE_CHAT_TOGGLE_BUTTON = By.cssSelector("[class*='chat'] button, [id*='chat'] button");

    public HomePage(WebDriver driver) {
        super(driver);
    }

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

    public List<WebElement> searchTrendingSuggestions() {
        return driver.findElements(SEARCH_TRENDING_LINKS);
    }

    public List<WebElement> searchSuggestedProducts() {
        return driver.findElements(SEARCH_SUGGESTED_PRODUCTS);
    }

    public void closeSearchDialog() {
        click(SEARCH_CLOSE_BUTTON);
    }

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

    public void goToNextHeroSlide() {
        click(HERO_SLIDER_NEXT);
    }

    public void goToPreviousHeroSlide() {
        click(HERO_SLIDER_PREV);
    }

    public boolean isFeaturedProductsSectionDisplayed() {
        return isDisplayed(FEATURED_PRODUCTS_HEADING);
    }

    public int awardBadgeCount() {
        return driver.findElements(AWARD_BADGES).size();
    }

    public boolean isFooterDisplayed() {
        return isDisplayed(FOOTER);
    }

    public int footerSocialLinkCount() {
        return driver.findElements(FOOTER_SOCIAL_LINKS).size();
    }

    public boolean isFooterLanguageComboboxDisplayed() {
        return isDisplayed(FOOTER_LANGUAGE_COMBOBOX);
    }

    public int footerPolicyLinkCount() {
        return driver.findElements(FOOTER_POLICY_LINKS).size();
    }

    public void clickFooterTermsOfUse() {
        click(FOOTER_TERMS_LINK);
    }

    public boolean isTopUtilityBannerDisplayed() {
        return isDisplayed(TOP_BAR_SHIPPING_BANNER);
    }

    public int topUtilityLinkCount() {
        return driver.findElements(TOP_UTILITY_LINKS).size();
    }

    public void clickAlwaysVinamilkLink() {
        click(TOP_BAR_LINK_ALWAYS_VINAMILK);
    }

    public void clickAlwaysInnovativeLink() {
        click(TOP_BAR_LINK_ALWAYS_INNOVATIVE);
    }

    public void clickStoreLink() {
        click(TOP_BAR_LINK_STORE);
    }

    public void clickBlogsNavLink() {
        click(NAV_LINK_BLOGS);
    }

    public void clickRewardsNavLink() {
        click(NAV_LINK_REWARDS);
    }

    public void clickCareNavLink() {
        click(NAV_LINK_CARE);
    }

    public HomePage clickLogo() {
        click(LOGO_LINK);
        return new HomePage(driver);
    }

    public void openProductMegaMenu() {
        click(PRODUCT_MENU_BUTTON);
    }

    public boolean isProductMegaMenuDisplayed() {
        return isDisplayed(MEGA_MENU_PANEL);
    }

    public int megaMenuCategoryLinkCount() {
        return driver.findElements(MEGA_MENU_CATEGORY_LINKS).size();
    }

    public int megaMenuBrandLinkCount() {
        return driver.findElements(MEGA_MENU_BRAND_LINKS).size();
    }

    public void clickMegaMenuCategory(String categoryText) {
        driver.findElement(By.xpath(
                "//a[contains(@href, '/collections/') and contains(., '" + categoryText + "')]")).click();
    }

    public void clickMegaMenuBrand(String brandText) {
        driver.findElement(By.xpath(
                "//a[contains(@href, 'src=ALL_vendors') and contains(., '" + brandText + "')]")).click();
    }

    public void clickMegaMenuBestSelling() {
        click(MEGA_MENU_QUICK_LINK_BEST_SELLING);
    }

    public void clickMegaMenuFlashSale() {
        click(MEGA_MENU_QUICK_LINK_FLASH_SALE);
    }

    public void clickSearchTrendingKeyword(String keyword) {
        driver.findElement(By.xpath(
                "//div[@role='dialog']//a[contains(@href, '/search?q=') and contains(., '" + keyword + "')]")).click();
    }

    public ProductDetailPage openFirstSuggestedProduct() {
        driver.findElements(SEARCH_SUGGESTED_PRODUCTS).get(0).click();
        return new ProductDetailPage(driver);
    }

    public boolean isCartEmptyMessageDisplayed() {
        return isDisplayed(CART_EMPTY_MESSAGE);
    }

    public ProductListingPage clickCartViewProductsCta() {
        click(CART_VIEW_PRODUCTS_CTA);
        return new ProductListingPage(driver);
    }

    public void closeCartDrawer() {
        click(CART_CLOSE_BUTTON);
    }

    public ProductDetailPage clickFeaturedNewProductBanner() {
        click(FEATURED_NEW_PRODUCT_BANNER);
        return new ProductDetailPage(driver);
    }

    public void clickTechnologySectionCta() {
        click(TECHNOLOGY_SECTION_CTA);
    }

    public void clickSustainabilitySectionCta() {
        click(SUSTAINABILITY_SECTION_CTA);
    }

    public int sustainabilityArticleCount() {
        return driver.findElements(SUSTAINABILITY_ARTICLES).size();
    }

    public void clickRecruitmentApplyCta() {
        click(RECRUITMENT_APPLY_CTA);
    }

    public void clickRecruitmentLifeLink() {
        click(RECRUITMENT_LIFE_LINK);
    }

    public boolean isLiveChatWidgetDisplayed() {
        return isDisplayed(LIVE_CHAT_WIDGET);
    }

    public void toggleLiveChatWidget() {
        click(LIVE_CHAT_TOGGLE_BUTTON);
    }
}
