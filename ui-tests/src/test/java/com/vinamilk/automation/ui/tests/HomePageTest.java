package com.vinamilk.automation.ui.tests;

import com.vinamilk.automation.core.config.ConfigManager;
import com.vinamilk.automation.ui.base.BaseUiTest;
import com.vinamilk.automation.ui.pages.HomePage;
import com.vinamilk.automation.ui.pages.ProductListingPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Storefront")
@Feature("Home Page")
public class HomePageTest extends BaseUiTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void openHomePage() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl"));
        homePage = new HomePage(driver);
    }

    @Test(description = "Main navigation menu renders all top-level entries")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-HOME-03: Verifies the header navigation (San pham, Tre nho, Thieu nien, Nguoi lon, Thuong hieu, "
            + "Luon vui khoe, Nhan qua, Vinamilk Care) is displayed on load")
    public void mainNavigationIsDisplayed() {
        Assert.assertTrue(homePage.isMainNavDisplayed(), "Main navigation should be visible on the home page");
        Assert.assertEquals(homePage.mainNavItemCount(), 8, "Expected 8 top-level navigation entries");
    }

    @Test(description = "Top utility bar renders shipping banner, links and icons")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-01: Verifies the free-shipping banner and top-bar links are visible on load")
    public void topUtilityBarIsDisplayed() {
        Assert.assertTrue(homePage.isTopUtilityBannerDisplayed(), "Free-shipping banner should be visible");
        Assert.assertTrue(homePage.topUtilityLinkCount() > 0, "Expected top utility bar links");
    }

    @Test(description = "Clicking 'Luon la Vinamilk' navigates to the about-us page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-01b")
    public void clickAlwaysVinamilkNavigatesToAboutUs() {
        homePage.clickAlwaysVinamilkLink();
        Assert.assertTrue(homePage.currentUrl().contains("/about-us"), "Expected navigation to /about-us");
    }

    @Test(description = "Clicking 'Luon cau tien' navigates to the technology page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-01c")
    public void clickAlwaysInnovativeNavigatesToTechnology() {
        homePage.clickAlwaysInnovativeLink();
        Assert.assertTrue(homePage.currentUrl().contains("/technology"), "Expected navigation to /technology");
    }

    @Test(description = "Account icon navigates straight to the login page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-HOME-02")
    public void accountIconNavigatesToLoginPage() {
        homePage.openAccount();
        Assert.assertTrue(homePage.currentUrl().contains("/login"), "Expected navigation to /login");
        Assert.assertTrue(homePage.pageTitle().contains("Đăng nhập"), "Expected login page title");
    }

    @Test(description = "Clicking 'Luon vui khoe' navigates to the blogs page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-03b")
    public void clickBlogsNavLinkNavigatesToBlogs() {
        homePage.clickBlogsNavLink();
        Assert.assertTrue(homePage.currentUrl().contains("/blogs"), "Expected navigation to /blogs");
    }

    @Test(description = "Clicking 'Nhan qua' navigates to the rewards page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-03c")
    public void clickRewardsNavLinkNavigatesToRewards() {
        homePage.clickRewardsNavLink();
        Assert.assertTrue(homePage.currentUrl().contains("/vinamilk-rewards"), "Expected navigation to /vinamilk-rewards");
    }

    @Test(description = "Clicking 'Vinamilk Care' navigates to the care page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-03d")
    public void clickCareNavLinkNavigatesToCare() {
        homePage.clickCareNavLink();
        Assert.assertTrue(homePage.currentUrl().contains("/care"), "Expected navigation to /care");
    }

    @Test(description = "Clicking the logo navigates back to the home page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-03e")
    public void clickLogoNavigatesToHome() {
        homePage.clickBlogsNavLink();
        homePage = homePage.clickLogo();
        Assert.assertEquals(homePage.currentUrl(), ConfigManager.getInstance().get("ui.baseUrl"),
                "Expected navigation back to the home page");
    }

    @Test(description = "Product mega-menu opens and renders category/brand link groups")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-04")
    public void productMegaMenuShowsContentGroups() {
        homePage.openProductMegaMenu();
        Assert.assertTrue(homePage.isProductMegaMenuDisplayed(), "Product mega-menu panel should be visible");
        Assert.assertTrue(homePage.megaMenuCategoryLinkCount() > 0, "Expected category links in the mega-menu");
        Assert.assertTrue(homePage.megaMenuBrandLinkCount() > 0, "Expected brand links in the mega-menu");
    }

    @Test(description = "Clicking a category in the product mega-menu navigates to the matching collection")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-05")
    public void clickMegaMenuCategoryNavigatesToCollection() {
        homePage.openProductMegaMenu();
        homePage.clickMegaMenuCategory("sữa tươi");
        Assert.assertTrue(homePage.currentUrl().contains("/collections/sua-tuoi"),
                "Expected navigation to the sua-tuoi collection");
    }

    @Test(description = "Clicking the 'Ban chay' quick link navigates to the best-selling page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-05b")
    public void clickMegaMenuBestSellingNavigates() {
        homePage.openProductMegaMenu();
        homePage.clickMegaMenuBestSelling();
        Assert.assertTrue(homePage.currentUrl().contains("/best-selling"), "Expected navigation to /best-selling");
    }

    @Test(description = "Clicking the 'Flash Sale' quick link navigates to the flash-sales page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-05c")
    public void clickMegaMenuFlashSaleNavigates() {
        homePage.openProductMegaMenu();
        homePage.clickMegaMenuFlashSale();
        Assert.assertTrue(homePage.currentUrl().contains("/flash-sales"), "Expected navigation to /flash-sales");
    }

    @Test(description = "Clicking a brand in the product mega-menu navigates to the matching vendor collection")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-05d")
    public void clickMegaMenuBrandNavigates() {
        homePage.openProductMegaMenu();
        homePage.clickMegaMenuBrand("probi");
        Assert.assertTrue(homePage.currentUrl().contains("ALL_vendors:probi"),
                "Expected navigation to the probi vendor collection");
    }

    @Test(description = "Hero slider renders the 3 value-proposition slides and supports next/previous navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-13 / TC-HOME-14")
    public void heroSliderIsDisplayedAndNavigable() {
        Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should be visible on load");
        int initialSlideCount = homePage.heroSlideCount();
        Assert.assertTrue(initialSlideCount > 0, "Expected at least one hero slide");

        homePage.goToNextHeroSlide();
        Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should remain visible after Next");

        homePage.goToPreviousHeroSlide();
        Assert.assertTrue(homePage.isHeroSliderDisplayed(), "Hero slider should remain visible after Previous");
    }

    @Test(description = "Search dialog opens from the header icon and shows trending + suggested products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-HOME-06: Verifies the search flyout (Tim trong Vinamilk) exposes trending keyword links and "
            + "'Danh cho ban' product suggestions before any keyword is typed")
    public void searchDialogShowsTrendingAndSuggestions() {
        homePage.openSearchDialog();
        Assert.assertTrue(homePage.isSearchDialogDisplayed(), "Search dialog should open when the search icon is clicked");
        Assert.assertTrue(homePage.searchTrendingSuggestions().size() > 0, "Expected trending keyword suggestions");
        Assert.assertTrue(homePage.searchSuggestedProducts().size() > 0, "Expected suggested products in the search dialog");
    }

    @Test(description = "Closing the search dialog returns to the home page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-07")
    public void closeSearchDialogReturnsToHomePage() {
        homePage.openSearchDialog();
        homePage.closeSearchDialog();
        Assert.assertFalse(homePage.isSearchDialogDisplayed(), "Search dialog should be closed");
    }

    @Test(description = "Clicking a trending keyword suggestion navigates to the matching search results page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-08")
    public void clickTrendingKeywordNavigatesToSearchResults() {
        homePage.openSearchDialog();
        homePage.clickSearchTrendingKeyword("Organic");
        Assert.assertTrue(homePage.currentUrl().contains("/search?q=Organic"),
                "Expected navigation to the Organic search results page");
    }

    @Test(description = "Clicking a suggested product in the search dialog navigates to its detail page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-08b")
    public void clickSuggestedProductNavigatesToDetailPage() {
        homePage.openSearchDialog();
        homePage.openFirstSuggestedProduct();
        Assert.assertTrue(homePage.currentUrl().contains("/products/"), "Expected navigation to a product detail page");
    }

    @Test(description = "Searching for an existing keyword returns matching products")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC-HOME-09")
    public void searchByKeywordReturnsResults() {
        ProductListingPage results = homePage.searchProduct("Sua tuoi");
        Assert.assertTrue(results.resultCount() > 0, "Expected at least one product in the search results");
    }

    @Test(description = "Pressing Enter with an empty search keyword does not navigate away")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-10")
    public void searchWithEmptyKeywordDoesNotNavigate() {
        String beforeUrl = homePage.currentUrl();
        homePage.searchProduct("");
        Assert.assertFalse(homePage.currentUrl().contains("/search?q="),
                "Expected no navigation to the search results page for an empty keyword");
        Assert.assertEquals(homePage.currentUrl(), beforeUrl, "Expected to stay on the home page");
    }

    @Test(description = "Searching for a nonsense keyword shows zero results")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-10b")
    public void searchWithNonsenseKeywordShowsNoResults() {
        ProductListingPage results = homePage.searchProduct("xyzxyz123");
        Assert.assertEquals(results.resultCount(), 0, "Expected no products for a nonsense keyword");
    }

    @Test(description = "Cart icon shows an item badge and opens the cart drawer with the empty state")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-HOME-11")
    public void cartIconOpensCartPage() {
        Assert.assertNotNull(homePage.cartItemCount(), "Cart badge should display an item count, even when zero");
        homePage.openCart();
        Assert.assertTrue(homePage.isCartEmptyMessageDisplayed(), "Expected the empty-cart message to be displayed");
    }

    @Test(description = "'Xem san pham gia tot' CTA navigates to the all-products collection from an empty cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-12")
    public void cartViewProductsCtaNavigatesToAllProducts() {
        homePage.openCart();
        homePage.clickCartViewProductsCta();
        Assert.assertTrue(homePage.currentUrl().contains("/collections/all-products"),
                "Expected navigation to /collections/all-products");
    }

    @Test(description = "Closing the cart drawer returns to the home page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-12b")
    public void closeCartDrawerReturnsToHomePage() {
        homePage.openCart();
        homePage.closeCartDrawer();
        Assert.assertFalse(homePage.isCartEmptyMessageDisplayed(), "Expected the cart drawer to be closed");
    }

    @Test(description = "Quality award badges are displayed on the home page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-15")
    public void awardBadgesAreDisplayed() {
        Assert.assertTrue(homePage.awardBadgeCount() > 0, "Expected at least one award badge image");
    }

    @Test(description = "Featured new-product banner navigates to its product detail page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-16")
    public void clickFeaturedNewProductBannerNavigatesToDetailPage() {
        homePage.clickFeaturedNewProductBanner();
        Assert.assertTrue(homePage.currentUrl().contains("/products/"), "Expected navigation to a product detail page");
    }

    @Test(description = "Featured products section ('Moi ban sam sua') is displayed on the home page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-17")
    public void featuredProductsSectionIsDisplayed() {
        Assert.assertTrue(homePage.isFeaturedProductsSectionDisplayed(),
                "Expected the featured-products carousel section to be visible");
    }

    @Test(description = "'Tim hieu them' CTA in the technology block navigates to the technology page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-18")
    public void technologySectionCtaNavigatesToTechnology() {
        homePage.clickTechnologySectionCta();
        Assert.assertTrue(homePage.currentUrl().contains("/technology"), "Expected navigation to /technology");
    }

    @Test(description = "'Tim hieu them' CTA in the sustainability block navigates to the sustainability page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-19")
    public void sustainabilitySectionCtaNavigatesToSustainability() {
        homePage.clickSustainabilitySectionCta();
        Assert.assertTrue(homePage.currentUrl().contains("/sustainability"), "Expected navigation to /sustainability");
    }

    @Test(description = "Sustainability block displays 3 numbered articles")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-19b")
    public void sustainabilitySectionShowsThreeArticles() {
        Assert.assertEquals(homePage.sustainabilityArticleCount(), 3, "Expected 3 numbered sustainability articles");
    }

    @Test(description = "'Ung tuyen ngay' CTA navigates to the career opportunities page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-20")
    public void recruitmentApplyCtaNavigatesToCareerOpportunities() {
        homePage.clickRecruitmentApplyCta();
        Assert.assertTrue(homePage.currentUrl().contains("/recruitment/career-opportunities"),
                "Expected navigation to /recruitment/career-opportunities");
    }

    @Test(description = "'Nhip song Vinamilk' story-link opens the LifeAtVinamilk Facebook page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-20b")
    public void recruitmentLifeLinkOpensFacebookPage() {
        String originalWindow = driver.getWindowHandle();
        homePage.clickRecruitmentLifeLink();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
            }
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook.com/LifeAtVinamilk"),
                "Expected the LifeAtVinamilk Facebook page to open");
    }

    @Test(description = "Footer renders link groups and social media links")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-21")
    public void footerIsDisplayedWithSocialLinks() {
        Assert.assertTrue(homePage.isFooterDisplayed(), "Footer should be visible on the home page");
        Assert.assertTrue(homePage.footerSocialLinkCount() > 0, "Expected social media links in the footer");
        Assert.assertTrue(homePage.isFooterLanguageComboboxDisplayed(), "Expected a language selector in the footer");
        Assert.assertTrue(homePage.footerPolicyLinkCount() > 0, "Expected policy links in the footer");
    }

    @Test(description = "Clicking the 'Dieu khoan su dung' footer link navigates to the terms-of-use page")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-21b")
    public void footerTermsOfUseLinkNavigates() {
        homePage.clickFooterTermsOfUse();
        Assert.assertTrue(homePage.currentUrl().contains("/support/terms-of-use"),
                "Expected navigation to /support/terms-of-use");
    }

    @Test(description = "Live-chat widget is displayed and can be toggled")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-HOME-22")
    public void liveChatWidgetIsDisplayedAndToggleable() {
        Assert.assertTrue(homePage.isLiveChatWidgetDisplayed(), "Expected the live-chat widget to be visible");
        homePage.toggleLiveChatWidget();
    }

    @Test(description = "'Cua hang' link navigates to the store locator page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-HOME-23")
    public void storeLinkNavigatesToStoreLocator() {
        homePage.clickStoreLink();
        Assert.assertTrue(homePage.pageTitle().contains("Danh sách cửa hàng"), "Expected the store locator page title");
    }
}
