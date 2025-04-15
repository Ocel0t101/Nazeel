package nazeel.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import nazeel.utils.DelayedWebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

import static nazeel.base.TestBase.Waits.WAIT_UNTIL_LOADS;

/**
 * Base test class for all automated test suites.
 * Handles WebDriver lifecycle, default waits, and reusable utility methods.
 */
public class TestBase {

    // The shared root WebDriver instance across tests (singleton-style)
    private static WebDriver rootDriver;

    /**
     * Setup method to initialize the WebDriver before any test class runs.
     * Uses Edge browser with a wrapped DelayedWebDriver to introduce delays between actions.
     */
    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        rootDriver = new DelayedWebDriver(new EdgeDriver(), 1000); // 1000ms delay between actions
        rootDriver.manage().window().maximize();
        rootDriver.get("https://staging.nazeel.net:9002/login");
    }

    /**
     * Teardown method to quit the WebDriver after the test class finishes execution.
     */
    @AfterClass
    public void tearDown() {
        rootDriver.quit();
    }

    /**
     * Returns the current WebDriver instance.
     * Used throughout the project for driver interactions.
     */
    public static WebDriver getRootDriver() {
        return rootDriver;
    }

    /**
     * Utility method to get an explicit wait object for the current driver.
     *
     * @param seconds Number of seconds to wait before timeout.
     * @return WebDriverWait instance.
     */
    public static WebDriverWait explicitWait(int seconds) {
        return new WebDriverWait(rootDriver, Duration.ofSeconds(seconds));
    }

    /**
     * Sets an implicit wait timeout globally for the WebDriver.
     *
     * @param seconds Number of seconds to wait implicitly before throwing NoSuchElementException.
     */
    public static void implicitWait(int seconds) {
        rootDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    /**
     * Waits until the given URL is loaded and the page loader disappears.
     * Ensures navigation is fully completed before proceeding with interactions.
     *
     * @param URL The expected URL after navigation.
     */
    public static void waitForPageToLoad(String URL) {
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlToBe(URL));
        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds())
                    .until(driver -> !new PageBase().isPageLoaderShown());
        } catch (TimeoutException ignored) {
            // Loader may disappear before check — safe to ignore.
        }
    }

    /**
     * Pauses execution for a fixed amount of time (in milliseconds).
     *
     * @param milliseconds Time to sleep.
     */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Enum to standardize common wait durations used throughout the test suite.
     */
    public enum Waits {
        WAIT_UNTIL_LOADS(10),
        WAIT_UNTIL_DISPLAYED(5),
        WAIT_UNTIL_CLICKABLE(5),
        WAIT_TEMP(1);

        private final int seconds;

        Waits(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return seconds;
        }
    }
}
