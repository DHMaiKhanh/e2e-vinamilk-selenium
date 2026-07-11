package com.vinamilk.automation.ui.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductListingPage extends BasePage {

    private static final By PRODUCT_CARDS = By.cssSelector(".product-card");

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
}
