package nazeel.listeners;

import nazeel.base.TestBase;
import nazeel.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Global TestNG listener to handle test lifecycle events.
 * Captures screenshots on failure, logs test results, and tracks suite-level execution.
 */
public class TestListener implements ITestListener {

    /**
     * Invoked when a test case fails.
     * Logs the failure and captures a screenshot using the shared WebDriver instance.
     *
     * @param result Contains information about the failed test.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("❌ Test failed: " + testName);
        ScreenshotUtil.captureScreenshot(TestBase.getRootDriver(), testName);
    }

    /**
     * Invoked when a test case passes successfully.
     * Logs the test pass result.
     *
     * @param result Contains information about the passed test.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test passed: " + result.getMethod().getMethodName());
    }

    /**
     * Invoked before any test in the current test context starts.
     * Useful for logging or initializing test data/resources.
     *
     * @param context The test context containing metadata for the suite/class execution.
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("🔷 Test Suite started: " + context.getName());
    }

    /**
     * Invoked after all tests in the test context have completed.
     * Useful for final reporting or cleanup operations.
     *
     * @param context The test context for the suite/class.
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🔶 Test Suite finished: " + context.getName());
    }
}
