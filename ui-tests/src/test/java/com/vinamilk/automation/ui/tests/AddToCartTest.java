package com.vinamilk.automation.ui.tests;

import com.vinamilk.automation.core.config.ConfigManager;
import com.vinamilk.automation.ui.base.BaseUiTest;
import com.vinamilk.automation.ui.pages.CartPage;
import com.vinamilk.automation.ui.pages.HomePage;
import com.vinamilk.automation.ui.pages.ProductDetailPage;
import com.vinamilk.automation.ui.pages.ProductListingPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Storefront")
@Feature("Shopping Cart")
public class AddToCartTest extends BaseUiTest {

    @Test(description = "Customer can add a searched product to the cart")
    public void addFirstSearchResultToCart() {
        driver.get(ConfigManager.getInstance().get("ui.baseUrl"));
        HomePage homePage = new HomePage(driver);
        ProductListingPage listing = homePage.searchProduct("Sua Chua Vinamilk");
        ProductDetailPage detail = listing.openFirstProduct();
        CartPage cart = detail.addToCart();
        Assert.assertTrue(cart.itemCount() > 0, "Cart should contain the added product");
    }
}
