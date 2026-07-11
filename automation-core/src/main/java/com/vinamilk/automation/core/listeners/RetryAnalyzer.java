package com.vinamilk.automation.core.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed {@code @Test} up to {@link #MAX_RETRY_COUNT} times before
 * letting the failure stand - absorbs the flakiness inherent to browser/UI
 * timing without masking a genuinely broken test.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}
