package com.vinamilk.automation.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads {@code environments/<env>.properties} from the classpath, selected via
 * {@code -Denv=<name>} (or the {@code TEST_ENV} environment variable), and
 * exposes it as a single, {@code -D}-overridable configuration source.
 */
public final class ConfigManager {

    private static final String DEFAULT_ENV = "dev";
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Properties properties = new Properties();

    private ConfigManager() {
        load();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private void load() {
        String env = System.getProperty("env", System.getenv().getOrDefault("TEST_ENV", DEFAULT_ENV));
        String resource = "environments/" + env + ".properties";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing configuration file on classpath: " + resource);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration: " + resource, e);
        }
    }

    public String get(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration key: " + key);
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        String override = System.getProperty(key);
        if (override != null) {
            return override;
        }
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }
}
