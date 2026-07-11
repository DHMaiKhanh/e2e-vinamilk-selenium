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

    @Test(description = "Registered customer can log in with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies a customer can authenticate and land on the home page")
    public void loginWithValidCredentials() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl") + "/login");
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginAs("qa.automation@vinamilk.com.vn", "P@ssw0rd123");
        Assert.assertNotNull(homePage, "Login should redirect to the home page");
    }

    @Test(description = "Login is rejected for invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithInvalidCredentialsShowsError() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl") + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("invalid.user@vinamilk.com.vn", "wrong-password");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("invalid"),
                "Expected an invalid credentials error message");
    }
}
