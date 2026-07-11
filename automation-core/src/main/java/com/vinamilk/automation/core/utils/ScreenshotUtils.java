package com.vinamilk.automation.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    private ScreenshotUtils() {
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static Path captureToFile(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path target = SCREENSHOT_DIR.resolve(testName + "-" + timestamp + ".png");
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), target);
            return target;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to capture screenshot for test: " + testName, e);
        }
    }
}
