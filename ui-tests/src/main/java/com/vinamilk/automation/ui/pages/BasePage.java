package com.vinamilk.automation.ui.pages;

import com.vinamilk.automation.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisible(driver, locator).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
