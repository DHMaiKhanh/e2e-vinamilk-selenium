package com.vinamilk.automation.mocks.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.vinamilk.automation.mocks.WireMockServerManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates validating a consumer against a stubbed product-service
 * contract, without depending on a live deployment of that microservice.
 */
public class OrderServiceIsolationTest {

    @BeforeClass
    public void startMockServer() {
        WireMockServerManager.start();
    }

    @AfterClass
    public void stopMockServer() {
        WireMockServerManager.stop();
    }

    @Test(description = "order-service's product lookup can be validated against a stubbed fixture")
    public void stubbedProductLookupReturnsFixtureResponse() {
        given()
                .baseUri("http://localhost:" + WireMockServerManager.getServer().port())
                .when()
                .get("/products/SKU-VNM-STV-100")
                .then()
                .statusCode(200)
                .body("sku", equalTo("SKU-VNM-STV-100"));
    }
}
