package com.vinamilk.automation.ui.tests;

import com.vinamilk.automation.core.config.ConfigManager;
import com.vinamilk.automation.ui.base.BaseUiTest;
import com.vinamilk.automation.ui.pages.HomePage;
import com.vinamilk.automation.ui.pages.ProductListingPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Storefront")
@Feature("Product Search")
public class ProductSearchTest extends BaseUiTest {

    @Test(description = "Searching for an existing SKU keyword returns matching products")
    public void searchReturnsResults() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl"));
        HomePage homePage = new HomePage(driver);
        ProductListingPage results = homePage.searchProduct("Sua Tuoi Vinamilk 100%");
        Assert.assertTrue(results.resultCount() > 0, "Expected at least one product in the search results");
    }
}
