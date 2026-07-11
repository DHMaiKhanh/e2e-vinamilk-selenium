package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private static final By PRODUCT_TITLE = By.cssSelector("h1.product-title");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("button.add-to-cart");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return textOf(PRODUCT_TITLE);
    }

    public CartPage addToCart() {
        click(ADD_TO_CART_BUTTON);
        return new CartPage(driver);
    }
}
