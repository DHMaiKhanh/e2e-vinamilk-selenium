package com.vinamilk.automation.core.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WaitUtils {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    private WaitUtils() {
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisible(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    private static WebDriverWait newWait(WebDriver driver) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }
}
