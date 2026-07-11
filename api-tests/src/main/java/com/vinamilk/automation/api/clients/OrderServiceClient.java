package com.vinamilk.automation.api.clients;

import static io.restassured.RestAssured.given;

import com.vinamilk.automation.api.config.ApiConfig;
import com.vinamilk.automation.api.models.request.CreateOrderRequest;
import com.vinamilk.automation.api.models.response.OrderResponse;
import com.vinamilk.automation.api.specs.RequestSpecFactory;
import io.restassured.response.Response;

public class OrderServiceClient {

    public Response createOrder(CreateOrderRequest request) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.ORDER))
                .body(request)
                .when()
                .post("/orders");
    }

    public OrderResponse getOrder(String orderId) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.ORDER))
                .pathParam("orderId", orderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .extract().as(OrderResponse.class);
    }
}
