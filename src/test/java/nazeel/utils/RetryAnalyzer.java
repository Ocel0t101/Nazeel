package nazeel.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer is a custom implementation of TestNG's IRetryAnalyzer.
 * It automatically retries failed tests up to a specified maximum number of attempts.
 *
 * This is useful for handling flaky tests caused by transient issues like timing,
 * network delays, or UI rendering glitches.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    // Tracks the number of retry attempts for the current test
    private int retryCount = 0;

    // Defines the maximum number of retry attempts allowed
    private static final int maxRetry = 25;

    /**
     * This method is invoked by TestNG after a test fails.
     *
     * @param result The result of the test execution.
     * @return true if the test should be retried, false otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        // If retryCount is less than maxRetry, increment and return true to retry the test
        return retryCount++ < maxRetry;
    }
}
