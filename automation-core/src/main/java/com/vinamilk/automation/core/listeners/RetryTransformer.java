package com.vinamilk.automation.core.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Registered as a {@code <listener>} in each suite's testng.xml so every
 * {@code @Test} automatically gets {@link RetryAnalyzer}, without every test
 * class needing {@code retryAnalyzer = RetryAnalyzer.class} by hand.
 */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
