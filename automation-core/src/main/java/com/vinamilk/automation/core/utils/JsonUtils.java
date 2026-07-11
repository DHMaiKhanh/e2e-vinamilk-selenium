package com.vinamilk.automation.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private JsonUtils() {
    }

    public static <T> T fromClasspath(String classpathResource, Class<T> type) {
        URL resource = JsonUtils.class.getClassLoader().getResource(classpathResource);
        if (resource == null) {
            throw new IllegalStateException("JSON resource not found on classpath: " + classpathResource);
        }
        try {
            return MAPPER.readValue(new File(resource.getFile()), type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read JSON resource: " + classpathResource, e);
        }
    }

    public static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize object to JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize JSON", e);
        }
    }
}
