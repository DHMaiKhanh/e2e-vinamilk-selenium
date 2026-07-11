package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final By SEARCH_INPUT = By.cssSelector("input[name='q']");
    private static final By SEARCH_BUTTON = By.cssSelector("button.search-submit");
    private static final By CART_ICON = By.cssSelector("a.header-cart-icon");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public ProductListingPage searchProduct(String keyword) {
        type(SEARCH_INPUT, keyword);
        click(SEARCH_BUTTON);
        return new ProductListingPage(driver);
    }

    public CartPage openCart() {
        click(CART_ICON);
        return new CartPage(driver);
    }
}
