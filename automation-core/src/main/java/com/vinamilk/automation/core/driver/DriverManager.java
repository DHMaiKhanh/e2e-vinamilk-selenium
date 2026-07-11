package com.vinamilk.automation.core.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-scoped {@link WebDriver} holder so parallel TestNG execution
 * ({@code parallel="classes"}/{@code "methods"}) never shares a browser
 * session across threads.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized for this thread");
        }
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
