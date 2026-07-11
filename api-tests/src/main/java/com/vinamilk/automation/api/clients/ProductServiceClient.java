package com.vinamilk.automation.api.clients;

import static io.restassured.RestAssured.given;

import com.vinamilk.automation.api.config.ApiConfig;
import com.vinamilk.automation.api.models.response.ProductResponse;
import com.vinamilk.automation.api.specs.RequestSpecFactory;
import io.restassured.response.Response;

public class ProductServiceClient {

    public Response getProductBySku(String sku) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.PRODUCT))
                .pathParam("sku", sku)
                .when()
                .get("/products/{sku}");
    }

    public ProductResponse getProduct(String sku) {
        return getProductBySku(sku).then().extract().as(ProductResponse.class);
    }

    public Response searchProducts(String keyword) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.PRODUCT))
                .queryParam("q", keyword)
                .when()
                .get("/products");
    }
}
