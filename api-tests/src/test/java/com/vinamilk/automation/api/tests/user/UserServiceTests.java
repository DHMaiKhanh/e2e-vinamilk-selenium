package com.vinamilk.automation.api.tests.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.vinamilk.automation.api.base.BaseApiTest;
import com.vinamilk.automation.api.clients.UserServiceClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

@Epic("Microservices API")
@Feature("User Service")
public class UserServiceTests extends BaseApiTest {

    private final UserServiceClient userServiceClient = new UserServiceClient();

    @Test(description = "GET /users/{id} returns 200 with the expected contract")
    public void getUserByIdReturnsExpectedContract() {
        Response response = userServiceClient.getUserById("usr-1001");

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo("usr-1001"))
                .body("email", notNullValue());
    }

    @Test(description = "GET /users/{id} returns 404 for an unknown user")
    public void getUnknownUserReturnsNotFound() {
        userServiceClient.getUserById("does-not-exist")
                .then()
                .statusCode(404);
    }
}
