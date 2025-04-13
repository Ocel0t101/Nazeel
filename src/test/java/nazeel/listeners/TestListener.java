package nazeel.listeners;

import nazeel.base.TestBase;
import nazeel.utils.ScreenshotUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ScreenshotUtil.captureScreenshot(TestBase.getRootDriver(), testName);
    }
}
