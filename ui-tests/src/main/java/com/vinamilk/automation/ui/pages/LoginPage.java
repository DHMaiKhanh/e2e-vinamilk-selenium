package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".login-error-message");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage loginAs(String email, String password) {
        type(EMAIL_INPUT, email);
        type(PASSWORD_INPUT, password);
        click(SUBMIT_BUTTON);
        return new HomePage(driver);
    }

    public String getErrorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
