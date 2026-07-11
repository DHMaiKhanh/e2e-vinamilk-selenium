package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private static final By CART_ITEM_ROWS = By.cssSelector(".cart-item-row");
    private static final By CHECKOUT_BUTTON = By.cssSelector("button.checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int itemCount() {
        return driver.findElements(CART_ITEM_ROWS).size();
    }

    public void proceedToCheckout() {
        click(CHECKOUT_BUTTON);
    }
}
