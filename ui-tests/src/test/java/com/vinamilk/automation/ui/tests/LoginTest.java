package com.vinamilk.automation.ui.tests;

import com.vinamilk.automation.core.config.ConfigManager;
import com.vinamilk.automation.ui.base.BaseUiTest;
import com.vinamilk.automation.ui.pages.HomePage;
import com.vinamilk.automation.ui.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Customer Portal")
@Feature("Authentication")
public class LoginTest extends BaseUiTest {

    // NOTE ON TEST PREMISE: verified live on https://www.vinamilk.com.vn/login that the
    // storefront authenticates with a phone number + password (input#phoneNumber), not an
    // email address as this test originally assumed (input#email does not exist at all).
    // There is also no test/QA account available for this suite, and the site has no visible
    // self-service way to provision one without a real phone number capable of receiving an
    // OTP/verification step. As a result, "loginWithValidCredentials" cannot be made to pass
    // legitimately without fabricating an assertion: we have no known-valid phone/password
    // pair to log in with, and inventing one would only produce a false green/red result.
    // This test is disabled until real QA credentials (or an OTP-bypass test account) are
    // provisioned; the locators below have been corrected to match the live form so the test
    // is ready to run as soon as credentials exist.
    @Test(description = "Registered customer can log in with valid credentials", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies a customer can authenticate and land on the home page. "
            + "DISABLED: no valid phone/password test account is available; see comment above.")
    public void loginWithValidCredentials() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl") + "/login");
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginAs("0900000000", "P@ssw0rd123");
        Assert.assertNotNull(homePage, "Login should redirect to the home page");
    }

    @Test(description = "Login is rejected for invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithInvalidCredentialsShowsError() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl") + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("0912345678", "wrong-password");
        String error = loginPage.getErrorMessage().toLowerCase();
        Assert.assertTrue(error.contains("sai") || error.contains("không") || error.contains("invalid"),
                "Expected an invalid credentials error message, got: " + error);
    }
}
