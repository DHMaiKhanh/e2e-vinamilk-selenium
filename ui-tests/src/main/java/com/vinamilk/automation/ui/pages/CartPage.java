package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    // NOTE: ".cart-item-row" and "button.checkout" do not exist on the live site (0 matches
    // verified). Adding a product does not reliably surface a distinct cart drawer/dialog in
    // this environment - what is reliably observable is the header cart icon's badge
    // (id="cartBadge", verified live: its count text incremented from "0" to "1" right after
    // a successful add-to-cart). Falling back to that badge count for itemCount() so this
    // page object matches what could actually be confirmed against the live site; the real
    // cart drawer/page markup should be re-verified and this replaced with real row locators
    // once that UI can be observed reliably.
    private static final By CART_BADGE_COUNT = By.cssSelector("#cartBadge span");
    private static final By CHECKOUT_BUTTON = By.xpath("//button[contains(., 'Thanh toán')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int itemCount() {
        try {
            String text = textOf(CART_BADGE_COUNT);
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException | org.openqa.selenium.TimeoutException e) {
            return 0;
        }
    }

    public void proceedToCheckout() {
        click(CHECKOUT_BUTTON);
    }
}
