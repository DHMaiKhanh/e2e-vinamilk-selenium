package com.vinamilk.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Verified live on https://www.vinamilk.com.vn/login: the form authenticates by phone
    // number, not email. There is no input#email on the real page (it does not exist at all),
    // which is why loginWithValidCredentials/loginWithInvalidCredentialsShowsError previously
    // failed to even type into the form. The real inputs are id="phoneNumber" (label
    // "So dien thoai*") and id="password".
    private static final By PHONE_INPUT = By.id("phoneNumber");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
    // NOTE: ".login-error-message" does not exist on the real page (0 matches verified live).
    // The site renders a global toast/notification region (seen live as
    // `region "Notifications alt+T"`) rather than an inline element with a dedicated class.
    // This locator targets that toast region as the best available approximation; it could
    // not be fully confirmed against a genuine failed-login response in this session (see
    // LoginTest for details) and should be re-verified once real test credentials exist.
    private static final By ERROR_MESSAGE = By.cssSelector("[aria-label='Notifications alt+T'] li, [role='status'], [role='alert']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage loginAs(String phoneNumber, String password) {
        type(PHONE_INPUT, phoneNumber);
        type(PASSWORD_INPUT, password);
        click(SUBMIT_BUTTON);
        return new HomePage(driver);
    }

    public String getErrorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
