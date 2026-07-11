package com.vinamilk.automation.api.specs;

import com.vinamilk.automation.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

/**
 * One {@link RequestSpecification} per microservice - own base URI, shared
 * auth header and Allure/logging filters - so individual clients stay
 * one-liner HTTP calls instead of repeating setup.
 */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    public static RequestSpecification forService(ApiConfig.ServiceName service) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.baseUrl(service))
                .setContentType("application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.URI);

        String token = ApiConfig.authToken();
        if (token != null && !token.isBlank()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return builder.build();
    }
}
