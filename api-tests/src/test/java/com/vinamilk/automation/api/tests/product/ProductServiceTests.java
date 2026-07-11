package com.vinamilk.automation.api.tests.product;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThan;

import com.vinamilk.automation.api.base.BaseApiTest;
import com.vinamilk.automation.api.clients.ProductServiceClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Epic("Microservices API")
@Feature("Product Service")
public class ProductServiceTests extends BaseApiTest {

    private final ProductServiceClient productServiceClient = new ProductServiceClient();

    @Test(description = "GET /products/{sku} response matches the published contract schema")
    public void getProductMatchesContractSchema() {
        productServiceClient.getProductBySku("SKU-VNM-STV-100")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-schema.json"));
    }

    @Test(description = "Product search returns results for a known keyword")
    public void searchProductsReturnsResults() {
        productServiceClient.searchProducts("sua tuoi")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}
