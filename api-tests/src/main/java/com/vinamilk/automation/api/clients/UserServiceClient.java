package com.vinamilk.automation.api.clients;

import static io.restassured.RestAssured.given;

import com.vinamilk.automation.api.config.ApiConfig;
import com.vinamilk.automation.api.models.response.UserResponse;
import com.vinamilk.automation.api.specs.RequestSpecFactory;
import io.restassured.response.Response;

public class UserServiceClient {

    public Response getUserById(String userId) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.USER))
                .pathParam("userId", userId)
                .when()
                .get("/users/{userId}");
    }

    public UserResponse getUser(String userId) {
        return getUserById(userId).then().extract().as(UserResponse.class);
    }

    public Response createUser(Object createUserRequest) {
        return given()
                .spec(RequestSpecFactory.forService(ApiConfig.ServiceName.USER))
                .body(createUserRequest)
                .when()
                .post("/users");
    }
}
