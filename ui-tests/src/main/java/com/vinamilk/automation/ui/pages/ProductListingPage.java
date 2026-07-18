package com.vinamilk.automation.ui.pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductListingPage extends BasePage {

    private static final By PRODUCT_CARDS = By.cssSelector(".product-card");
    private static final String FIRST_CARD_XPATH = "(//*[contains(@class, 'product-card')])[1]";

    private static final By DELIVERY_AREA_BANNER = By.xpath(
            "//*[contains(text(), 'Chọn khu vực giao hàng')]");
    private static final By CHOOSE_ADDRESS_BUTTON = By.xpath(
            "//button[contains(., 'Chọn địa chỉ của bạn')]");
    private static final By ADDRESS_DIALOG = By.xpath(
            "//div[@role='dialog'][.//*[contains(text(), 'Chọn địa chỉ của bạn')]]");
    private static final By ADDRESS_DIALOG_CLOSE_BUTTON = By.xpath(
            "//div[@role='dialog'][.//*[contains(text(), 'Chọn địa chỉ của bạn')]]//button");
    private static final By ADDRESS_SEARCH_INPUT = By.xpath(
            "//div[@role='dialog'][.//*[contains(text(), 'Chọn địa chỉ của bạn')]]//input");

    private static final By BREADCRUMB = By.xpath("//nav[contains(@aria-label, 'readcrumb')]");
    private static final By PAGE_HEADING = By.xpath("//h1[contains(., 'Tất cả sản phẩm')]");
    private static final By PAGE_HEADING_CONTAINER = By.xpath("//h1[contains(., 'Tất cả sản phẩm')]/..");

    private static final By CATEGORY_FILTER_BUTTON = By.xpath("//button[contains(., 'Danh mục')]");
    private static final By CLEAR_FILTERS_BUTTON = By.xpath("//button[contains(., 'Xoá bộ lọc')]");
    private static final By FILTER_BAR_BUTTONS = By.xpath(
            "//button[contains(., 'Danh mục') or contains(., 'Dòng sản phẩm') or contains(., 'Thương hiệu')"
                    + " or contains(., 'Hương vị') or contains(., 'Thể tích') or contains(., 'Nhu cầu dinh dưỡng')"
                    + " or contains(., 'Mức đường') or contains(., 'Phương thức giao hàng')]");

    private static final By SORT_COMBOBOX = By.xpath("//*[@role='combobox'][contains(., 'Xếp theo')]");
    private static final By SORT_LISTBOX = By.xpath("//*[@role='listbox']");

    private static final By FIRST_PRODUCT_HEADING = By.xpath(FIRST_CARD_XPATH + "//*[self::h2 or self::h3]");
    private static final By FIRST_PRODUCT_RATING = By.xpath(
            FIRST_CARD_XPATH + "//*[contains(text(), '(') and contains(text(), ')')]");
    private static final By FIRST_PRODUCT_BADGE = By.xpath(
            FIRST_CARD_XPATH + "//*[contains(text(), 'Award') or contains(text(), 'Organic')"
                    + " or contains(text(), 'Superior Taste')]");
    private static final By FIRST_PRODUCT_NOTE = By.xpath(FIRST_CARD_XPATH + "//*[contains(text(), 'LƯU Ý')]");

    private static final By FIRST_PRODUCT_OPEN_OPTIONS_BUTTON = By.xpath(
            FIRST_CARD_XPATH + "//button[contains(., 'Open product options')]");
    private static final By FIRST_PRODUCT_CLOSE_OPTIONS_BUTTON = By.xpath(
            FIRST_CARD_XPATH + "//button[contains(., 'Close product options')]");
    private static final By FIRST_PRODUCT_QUANTITY_DECREASE_BUTTON = By.xpath(
            FIRST_CARD_XPATH + "//*[@role='spinbutton']/preceding-sibling::button[1]");
    private static final By FIRST_PRODUCT_QUANTITY_INCREASE_BUTTON = By.xpath(
            FIRST_CARD_XPATH + "//*[@role='spinbutton']/following-sibling::button[1]");
    private static final By FIRST_PRODUCT_ADD_TO_CART_BUTTON = By.xpath(
            FIRST_CARD_XPATH + "//button[contains(., 'Thêm vào giỏ')]");

    private static final By PAGINATION_NAV = By.xpath("//nav[contains(@aria-label, 'Pagination')]");
    private static final By PREVIOUS_PAGE_BUTTON = By.xpath(
            "//nav[contains(@aria-label, 'Pagination')]//button[contains(@aria-label, 'Previous page')]"
                    + " | //nav[contains(@aria-label, 'Pagination')]//a[contains(@aria-label, 'Previous page')]");
    private static final By NEXT_PAGE_BUTTON = By.xpath(
            "//nav[contains(@aria-label, 'Pagination')]//button[contains(@aria-label, 'Next page')]"
                    + " | //nav[contains(@aria-label, 'Pagination')]//a[contains(@aria-label, 'Next page')]");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

    public ProductListingPage(WebDriver driver) {
        super(driver);
    }

    public int resultCount() {
        return driver.findElements(PRODUCT_CARDS).size();
    }

    public ProductDetailPage openFirstProduct() {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        if (cards.isEmpty()) {
            throw new IllegalStateException("No products found in listing");
        }
        cards.get(0).click();
        return new ProductDetailPage(driver);
    }

    public ProductDetailPage clickFirstProductHeading() {
        click(FIRST_PRODUCT_HEADING);
        return new ProductDetailPage(driver);
    }

    public boolean isDeliveryAreaBannerDisplayed() {
        return isDisplayed(DELIVERY_AREA_BANNER);
    }

    public void openChooseAddressDialog() {
        click(CHOOSE_ADDRESS_BUTTON);
    }

    public boolean isAddressDialogDisplayed() {
        return isDisplayed(ADDRESS_DIALOG);
    }

    public boolean isAddressSearchInputDisplayed() {
        return isDisplayed(ADDRESS_SEARCH_INPUT);
    }

    public void closeAddressDialog() {
        click(ADDRESS_DIALOG_CLOSE_BUTTON);
    }

    public boolean isBreadcrumbDisplayed() {
        return isDisplayed(BREADCRUMB);
    }

    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    public int productCountFromHeading() {
        String text = textOf(PAGE_HEADING_CONTAINER);
        Matcher matcher = DIGITS_PATTERN.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        throw new IllegalStateException("No product count found next to the page heading");
    }

    public void openCategoryFilter() {
        click(CATEGORY_FILTER_BUTTON);
    }

    public boolean isCategoryFilterRegionDisplayed() {
        return isDisplayed(By.xpath("//*[@role='region' or @role='group'][contains(., 'kem')]"));
    }

    public void selectCategoryCheckbox(String labelText) {
        click(By.xpath("//*[self::label or self::div or self::li][contains(., '" + labelText
                + "')]//input[@type='checkbox']"));
    }

    public boolean isClearFiltersButtonDisplayed() {
        return isDisplayed(CLEAR_FILTERS_BUTTON);
    }

    public void clearFilters() {
        click(CLEAR_FILTERS_BUTTON);
    }

    public int filterBarButtonCount() {
        return driver.findElements(FILTER_BAR_BUTTONS).size();
    }

    public void openSortDropdown() {
        click(SORT_COMBOBOX);
    }

    public boolean isSortListboxDisplayed() {
        return isDisplayed(SORT_LISTBOX);
    }

    public String sortComboboxLabel() {
        return textOf(SORT_COMBOBOX);
    }

    public void selectSortOption(String optionText) {
        click(By.xpath("//*[@role='option'][contains(., '" + optionText + "')]"));
    }

    public boolean isFirstProductCardDisplayed() {
        return isDisplayed(By.xpath(FIRST_CARD_XPATH));
    }

    public boolean isFirstProductRatingDisplayed() {
        return isDisplayed(FIRST_PRODUCT_RATING);
    }

    public boolean isFirstProductBadgeDisplayed() {
        return isDisplayed(FIRST_PRODUCT_BADGE);
    }

    public boolean isFirstProductDeliveryNoteDisplayed() {
        return isDisplayed(FIRST_PRODUCT_NOTE);
    }

    public void openFirstProductOptions() {
        click(FIRST_PRODUCT_OPEN_OPTIONS_BUTTON);
    }

    public void closeFirstProductOptions() {
        click(FIRST_PRODUCT_CLOSE_OPTIONS_BUTTON);
    }

    public boolean isFirstProductOptionsExpanded() {
        return isDisplayed(FIRST_PRODUCT_CLOSE_OPTIONS_BUTTON);
    }

    public boolean isFirstProductOpenOptionsButtonDisplayed() {
        return isDisplayed(FIRST_PRODUCT_OPEN_OPTIONS_BUTTON);
    }

    public void selectFirstProductPackaging(String packagingText) {
        click(By.xpath(FIRST_CARD_XPATH + "//*[@role='radio'][contains(., '" + packagingText + "')]"));
    }

    public String firstProductAddToCartLabel() {
        return textOf(FIRST_PRODUCT_ADD_TO_CART_BUTTON);
    }

    public boolean isFirstProductQuantityDecreaseDisabled() {
        WebElement decreaseButton = driver.findElement(FIRST_PRODUCT_QUANTITY_DECREASE_BUTTON);
        return !decreaseButton.isEnabled();
    }

    public void increaseFirstProductQuantity() {
        click(FIRST_PRODUCT_QUANTITY_INCREASE_BUTTON);
    }

    public boolean isPreviousPageButtonDisabled() {
        WebElement previousButton = driver.findElement(PREVIOUS_PAGE_BUTTON);
        return !previousButton.isEnabled();
    }

    public void goToPage(int pageNumber) {
        click(By.xpath("//nav[contains(@aria-label, 'Pagination')]//a[contains(@aria-label, 'Page " + pageNumber
                + "')]"));
    }

    public void goToNextPage() {
        click(NEXT_PAGE_BUTTON);
    }

    public boolean isPaginationDisplayed() {
        return isDisplayed(PAGINATION_NAV);
    }

    public int lastPageNumber() {
        List<WebElement> pageLinks = driver.findElements(By.xpath(
                "//nav[contains(@aria-label, 'Pagination')]//a[contains(@aria-label, 'Page ')]"));
        int lastPage = 0;
        for (WebElement link : pageLinks) {
            Matcher matcher = DIGITS_PATTERN.matcher(link.getAttribute("aria-label"));
            if (matcher.find()) {
                lastPage = Math.max(lastPage, Integer.parseInt(matcher.group()));
            }
        }
        return lastPage;
    }
}
