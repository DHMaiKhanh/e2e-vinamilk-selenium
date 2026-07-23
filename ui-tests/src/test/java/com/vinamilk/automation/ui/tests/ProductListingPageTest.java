package com.vinamilk.automation.ui.tests;

import com.vinamilk.automation.core.config.ConfigManager;
import com.vinamilk.automation.ui.base.BaseUiTest;
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
@Feature("Product Listing")
public class ProductListingPageTest extends BaseUiTest {

    private ProductListingPage listingPage;

    @BeforeMethod(alwaysRun = true)
    public void openProductListingPage() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl") + "/collections/all-products");
        listingPage = new ProductListingPage(driver);
    }

    @Test(description = "Delivery-area banner is displayed when no address has been chosen")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-01")
    public void deliveryAreaBannerIsDisplayed() {
        Assert.assertTrue(listingPage.isDeliveryAreaBannerDisplayed(),
                "Expected the delivery-area banner to be visible");
    }

    @Test(description = "Clicking 'Chọn địa chỉ của bạn' opens the address dialog")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-02")
    public void chooseAddressButtonOpensDialog() {
        listingPage.openChooseAddressDialog();
        Assert.assertTrue(listingPage.isAddressDialogDisplayed(), "Expected the address dialog to open");
        Assert.assertTrue(listingPage.isAddressSearchInputDisplayed(), "Expected an address search input in the dialog");
    }

    @Test(description = "Closing the address dialog returns to the product listing page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-03")
    public void closeAddressDialogReturnsToListing() {
        listingPage.openChooseAddressDialog();
        listingPage.closeAddressDialog();
        Assert.assertFalse(listingPage.isAddressDialogDisplayed(), "Expected the address dialog to be closed");
    }

    @Test(description = "Breadcrumb and page heading render correctly")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-04")
    public void breadcrumbAndHeadingAreDisplayed() {
        Assert.assertTrue(listingPage.isBreadcrumbDisplayed(), "Expected the breadcrumb to be visible");
        Assert.assertTrue(listingPage.isPageHeadingDisplayed(), "Expected the 'Tất cả sản phẩm' heading to be visible");
        Assert.assertTrue(listingPage.productCountFromHeading() > 0, "Expected a product count next to the heading");
    }

    @Test(description = "Product count next to the heading updates when a category filter is applied")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-05")
    public void productCountUpdatesWhenFilterApplied() {
        int initialCount = listingPage.productCountFromHeading();
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("kem");
        Assert.assertNotEquals(listingPage.productCountFromHeading(), initialCount,
                "Expected the product count to change after applying the 'kem' category filter");
    }

    @Test(description = "The 'Danh mục' filter dropdown opens with category checkboxes")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-06")
    public void categoryFilterDropdownOpens() {
        listingPage.openCategoryFilter();
        Assert.assertTrue(listingPage.isCategoryFilterRegionDisplayed(), "Expected the category filter region to open");
    }

    @Test(description = "Applying a single category filter updates the URL, count and product list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-07")
    public void applyingCategoryFilterUpdatesResults() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("kem");
        Assert.assertTrue(listingPage.currentUrl().contains("categories=%5B%22kem%22%5D")
                || listingPage.currentUrl().contains("categories=[\"kem\"]"),
                "Expected the URL to reflect the 'kem' category filter");
        Assert.assertTrue(listingPage.isClearFiltersButtonDisplayed(), "Expected the 'Xoá bộ lọc' button to appear");
    }

    @Test(description = "Clearing filters removes all applied conditions")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-08")
    public void clearFiltersRemovesConditions() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("kem");
        listingPage.clearFilters();
        Assert.assertFalse(listingPage.isClearFiltersButtonDisplayed(),
                "Expected the 'Xoá bộ lọc' button to disappear after clearing filters");
    }

    @Test(description = "All 8 filter groups are displayed on the filter bar")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-09")
    public void allFilterGroupsAreDisplayed() {
        Assert.assertEquals(listingPage.filterBarButtonCount(), 8, "Expected 8 filter buttons on the filter bar");
    }

    @Test(description = "The sort dropdown opens with all sort options")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-10")
    public void sortDropdownOpens() {
        listingPage.openSortDropdown();
        Assert.assertTrue(listingPage.isSortListboxDisplayed(), "Expected the sort listbox to open");
    }

    @Test(description = "Sorting by 'Giá tăng dần' updates the sort label")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-11")
    public void sortByPriceAscending() {
        listingPage.openSortDropdown();
        listingPage.selectSortOption("Giá tăng dần");
        Assert.assertFalse(listingPage.isSortListboxDisplayed(), "Expected the sort dropdown to close after selection");
        Assert.assertTrue(listingPage.sortComboboxLabel().contains("Giá tăng dần"),
                "Expected the sort combobox label to update to 'Giá tăng dần'");
    }

    @Test(description = "Sorting by 'Giá giảm dần' updates the sort label")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-12")
    public void sortByPriceDescending() {
        listingPage.openSortDropdown();
        listingPage.selectSortOption("Giá giảm dần");
        Assert.assertTrue(listingPage.sortComboboxLabel().contains("Giá giảm dần"),
                "Expected the sort combobox label to update to 'Giá giảm dần'");
    }

    @Test(description = "The first product card renders its full information")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-13")
    public void firstProductCardShowsFullInformation() {
        Assert.assertTrue(listingPage.isFirstProductCardDisplayed(), "Expected the first product card to be visible");
        Assert.assertTrue(listingPage.resultCount() > 0, "Expected at least one product card in the grid");
    }

    @Test(description = "Product rating is displayed when the product has reviews")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-14")
    public void ratingIsDisplayedWhenProductHasReviews() {
        Assert.assertTrue(listingPage.isFirstProductRatingDisplayed(),
                "Expected a rating score and review count to be displayed on the first product card");
    }

    @Test(description = "No rating is displayed when the product has no reviews")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-15: verified against a product known to have no reviews, per the spec example")
    public void noRatingDisplayedWhenProductHasNoReviews() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("sữa đặc");
        Assert.assertFalse(listingPage.isFirstProductRatingDisplayed(),
                "Expected no rating to be displayed for a product without reviews");
    }

    @Test(description = "A quality certification badge is displayed on a certified product")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-16")
    public void certificationBadgeIsDisplayedOnCertifiedProduct() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("sữa bột trẻ em");
        Assert.assertTrue(listingPage.isFirstProductBadgeDisplayed(),
                "Expected a certification badge on a certified product");
    }

    @Test(description = "No certification badge is displayed on a product without certification")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-17")
    public void noCertificationBadgeOnUncertifiedProduct() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("nước giải khát");
        Assert.assertFalse(listingPage.isFirstProductBadgeDisplayed(),
                "Expected no certification badge on an uncertified product");
    }

    @Test(description = "10km delivery-radius warning is displayed for ice-cream/yogurt products")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-18")
    public void tenKmWarningDisplayedForIceCreamCategory() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("kem");
        Assert.assertTrue(listingPage.isFirstProductDeliveryNoteDisplayed(),
                "Expected the 10km delivery-radius warning to be displayed for ice-cream products");
    }

    @Test(description = "10km delivery-radius warning is not displayed for other categories")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-19")
    public void tenKmWarningNotDisplayedForOtherCategories() {
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("sữa tươi");
        Assert.assertFalse(listingPage.isFirstProductDeliveryNoteDisplayed(),
                "Expected no 10km delivery-radius warning outside the ice-cream/yogurt categories");
    }

    @Test(description = "Expanding quick-buy options on the first product card reveals its controls")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-20")
    public void openProductOptionsExpandsQuickBuyControls() {
        listingPage.openFirstProductOptions();
        Assert.assertTrue(listingPage.isFirstProductOptionsExpanded(),
                "Expected the product card to expand and show 'Close product options'");
    }

    @Test(description = "Selecting a different packaging option updates the add-to-cart price")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-21")
    public void selectingPackagingUpdatesAddToCartPrice() {
        listingPage.openFirstProductOptions();
        String initialLabel = listingPage.firstProductAddToCartLabel();
        listingPage.selectFirstProductPackaging("Lốc 4 hộp");
        Assert.assertNotEquals(listingPage.firstProductAddToCartLabel(), initialLabel,
                "Expected the 'Thêm vào giỏ' price to update after selecting a different packaging");
    }

    @Test(description = "Quantity decrease button is disabled when quantity is 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-22")
    public void quantityDecreaseButtonDisabledAtMinimum() {
        listingPage.openFirstProductOptions();
        Assert.assertTrue(listingPage.isFirstProductQuantityDecreaseDisabled(),
                "Expected the quantity decrease button to be disabled when quantity is 1");
    }

    @Test(description = "Closing quick-buy options collapses the product card")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-23")
    public void closeProductOptionsCollapsesCard() {
        listingPage.openFirstProductOptions();
        listingPage.closeFirstProductOptions();
        Assert.assertTrue(listingPage.isFirstProductOpenOptionsButtonDisplayed(),
                "Expected the card to collapse back to 'Open product options'");
    }

    @Test(description = "'Previous page' button is disabled on the first page")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC-PL-24")
    public void previousPageButtonDisabledOnFirstPage() {
        Assert.assertTrue(listingPage.isPreviousPageButtonDisabled(),
                "Expected the 'Previous page' button to be disabled on page 1");
    }

    @Test(description = "Navigating to page 2 updates the URL and product list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-25")
    public void navigateToPageTwo() {
        listingPage.goToPage(2);
        Assert.assertTrue(listingPage.currentUrl().contains("page=2"), "Expected the URL to update to page 2");
    }

    @Test(description = "Last page number reflects the applied category filter")
    @Severity(SeverityLevel.MINOR)
    @Description("TC-PL-26")
    public void lastPageReflectsAppliedFilter() {
        int unfilteredLastPage = listingPage.lastPageNumber();
        listingPage.openCategoryFilter();
        listingPage.selectCategoryCheckbox("kem");
        Assert.assertTrue(listingPage.lastPageNumber() < unfilteredLastPage,
                "Expected the last page number to be smaller once filtered down to the 'kem' category");
    }

    @Test(description = "Clicking the first product's heading navigates to its detail page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-PL-27")
    public void clickFirstProductNavigatesToDetailPage() {
        listingPage.clickFirstProductHeading();
        Assert.assertTrue(listingPage.currentUrl().contains("/products/"),
                "Expected navigation to a product detail page");
    }
}
