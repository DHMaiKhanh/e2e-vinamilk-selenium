package com.vinamilk.automation.core.listeners;

import com.vinamilk.automation.core.driver.DriverManager;
import com.vinamilk.automation.core.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Logs test lifecycle events and, on failure, attaches a screenshot of the
 * calling thread's WebDriver session to the Allure report (a no-op for
 * non-UI suites, since no driver is bound to the thread there).
 */
public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("Starting test: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        attachScreenshotIfDriverAvailable();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.info("Test suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("Test suite finished: {}", context.getName());
    }

    private void attachScreenshotIfDriverAvailable() {
        try {
            byte[] screenshot = ScreenshotUtils.captureAsBytes(DriverManager.getDriver());
            Allure.addAttachment("Failure screenshot", new ByteArrayInputStream(screenshot));
        } catch (IllegalStateException e) {
            LOG.debug("No active WebDriver for this thread, skipping screenshot attachment");
        }
    }
}
