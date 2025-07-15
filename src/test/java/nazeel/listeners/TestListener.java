package nazeel.listeners;

import nazeel.base.TestBase;
import nazeel.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Global TestNG listener to handle test lifecycle events.
 * Implements the ITestListener interface to:
 * - Capture screenshots on test failure
 * - Log test result outcomes to the console
 * - Track test suite execution start and end
 */
public class TestListener implements ITestListener {

    /**
     * Called when an individual test method fails.
     * Logs the test name and takes a screenshot using the shared WebDriver instance.
     *
     * @param result Metadata about the failed test, including name, class, and error.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("❌ Test failed: " + testName);
        ScreenshotUtil.captureScreenshot(TestBase.getRootDriver(), testName);
    }

    /**
     * Called when a test method passes successfully.
     * Simply logs the name of the passing test.
     *
     * @param result Metadata for the passed test method.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test passed: " + result.getMethod().getMethodName());
    }

    /**
     * Called before the start of the test suite or test class.
     * Good place for logging suite name or initializing global resources.
     *
     * @param context Metadata about the entire test context (suite or class).
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("🔷 Test Suite started: " + context.getName());
    }

    /**
     * Called after the test suite or test class finishes execution.
     * Useful for cleanup tasks, teardown actions, or logging final status.
     *
     * @param context Metadata for the finished suite/class.
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🔶 Test Suite finished: " + context.getName());
    }
}
