package nazeel.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class responsible for capturing and saving screenshots when tests fail.
 * This helps with debugging and identifying UI issues by preserving the visual state of the browser.
 */
public class ScreenshotUtil {

    /**
     * Captures a screenshot of the current browser window and saves it as a PNG file
     * under the "failed-screenshots" directory with a unique timestamp-based filename.
     *
     * @param driver   The WebDriver instance used for capturing the screenshot.
     * @param testName The name of the test case, used in the generated file name.
     */
    public static void captureScreenshot(WebDriver driver, String testName) {
        // Capture screenshot as a file
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Generate a timestamp for unique naming
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Build the full file path for the screenshot
        String fileName = "failed-screenshots/" + testName + "_" + timestamp + ".png";

        // Try saving the file to disk
        try {
            FileUtils.copyFile(srcFile, new File(fileName));
            System.out.println("✅ ScreenshotUtil saved: " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to save screenshot: " + e.getMessage());
        }
    }
}
