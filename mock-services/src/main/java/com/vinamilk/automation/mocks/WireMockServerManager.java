package com.vinamilk.automation.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.vinamilk.automation.core.config.ConfigManager;

/**
 * Starts a single embedded WireMock instance per JVM, serving stubs/fixtures
 * from {@code src/main/resources/wiremock}. Tests point their service client
 * at this server's port instead of a live upstream microservice.
 */
public final class WireMockServerManager {

    private static WireMockServer server;

    private WireMockServerManager() {
    }

    public static synchronized WireMockServer start() {
        if (server == null || !server.isRunning()) {
            int port = ConfigManager.getInstance().getInt("wiremock.port", 8089);
            server = new WireMockServer(WireMockConfiguration.options()
                    .port(port)
                    .usingFilesUnderClasspath("wiremock"));
            server.start();
        }
        return server;
    }

    public static synchronized void stop() {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    public static WireMockServer getServer() {
        if (server == null) {
            throw new IllegalStateException("WireMock server has not been started");
        }
        return server;
    }
}
