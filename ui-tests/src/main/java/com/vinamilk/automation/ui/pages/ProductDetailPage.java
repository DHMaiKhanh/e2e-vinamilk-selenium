package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    // NOTE: verified live on a product detail page - "h1.product-title" and "button.add-to-cart"
    // do not exist. The page has exactly one <h1> (plain Tailwind utility classes, no semantic
    // hook) holding the product name, and the add-to-cart control is a <button> whose text
    // combines the price with "Them vao gio" (no dedicated class/id).
    private static final By PRODUCT_TITLE = By.cssSelector("h1");
    private static final By ADD_TO_CART_BUTTON = By.xpath("//button[contains(., 'Thêm vào giỏ')]");

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
