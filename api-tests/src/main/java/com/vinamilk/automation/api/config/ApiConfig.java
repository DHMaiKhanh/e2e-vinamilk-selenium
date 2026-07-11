package com.vinamilk.automation.api.config;

import com.vinamilk.automation.core.config.ConfigManager;

public final class ApiConfig {

    private ApiConfig() {
    }

    public static String baseUrl(ServiceName service) {
        return ConfigManager.getInstance().get("api." + service.getKey() + ".baseUrl");
    }

    public static String authToken() {
        return ConfigManager.getInstance().get("api.auth.token", "");
    }

    /** One entry per microservice fronted by this framework's API layer. */
    public enum ServiceName {
        USER("user-service"),
        PRODUCT("product-service"),
        ORDER("order-service"),
        PAYMENT("payment-service");

        private final String key;

        ServiceName(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
