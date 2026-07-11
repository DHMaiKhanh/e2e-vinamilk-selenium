package com.vinamilk.automation.ui.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Reusable component for markup shared across pages (header/nav), kept
 * separate from any single {@code Page} class so it can be composed into
 * whichever pages render it.
 */
public class HeaderComponent {

    private static final By LOGO = By.cssSelector("a.site-logo");

    private final WebDriver driver;

    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLogoDisplayed() {
        return !driver.findElements(LOGO).isEmpty();
    }
}
