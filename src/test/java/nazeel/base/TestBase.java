package nazeel.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import nazeel.pages.DashboardPage;
import nazeel.pages.LoginPage;
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
 * TestBase is the foundation for all test suites.
 * It initializes and tears down the WebDriver, provides wait utilities,
 * and contains project-wide setup logic like login and navigation.
 */
public class TestBase {

    // Singleton-style WebDriver shared across tests in the suite
    private static WebDriver rootDriver;

    /**
     * Temporary method used to login and land on the dashboard.
     * NOTE: This is hardcoded and should eventually be replaced by dedicated login test cases.
     */
    public void tempLoginAndNavigateToDashboard() {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("MGamalTest")
                .insertPassword("132456789Aa@")
                .insertAccessCode("01456")
                .clickLoginButton();

        sleep(2000); // Wait for the login animation/redirect
        loginPage.selectPropertyByIndex(2); // Selects the 3rd property from the list
        waitForPageToLoad(DashboardPage.URL); // Wait until dashboard loads
        loginPage.getLoginUser().setName(new DashboardPage().getUserName()); // Save user identity
    }

    /**
     * Initializes the WebDriver (Edge in this case) before any tests run.
     * Uses a DelayedWebDriver wrapper to artificially slow down actions for debug visibility.
     */
    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup(); // Setup EdgeDriver via WebDriverManager
        rootDriver = new DelayedWebDriver(new EdgeDriver(), 600); // Wrap driver with artificial delay (600ms)
        rootDriver.manage().window().maximize();
        rootDriver.get("https://staging.nazeel.net:9002/login"); // Go to login page
        tempLoginAndNavigateToDashboard(); // Perform login and select property
    }

    /**
     * Terminates the WebDriver session after the test suite finishes.
     * Ensures browser is closed and memory is freed.
     */
    @AfterClass
    public void tearDown() {
        rootDriver.quit();
    }

    /**
     * Provides global access to the current WebDriver instance.
     * Used in utility classes and page objects.
     */
    public static WebDriver getRootDriver() {
        return rootDriver;
    }

    /**
     * Creates a WebDriverWait with a custom timeout.
     * Used for explicit wait conditions (e.g., wait until element is visible).
     *
     * @param seconds number of seconds to wait
     * @return configured WebDriverWait object
     */
    public static WebDriverWait explicitWait(int seconds) {
        return new WebDriverWait(rootDriver, Duration.ofSeconds(seconds));
    }

    /**
     * Applies an implicit wait to the WebDriver.
     * Elements will be searched repeatedly up to the timeout before failing.
     *
     * @param seconds number of seconds for implicit wait
     */
    public static void implicitWait(int seconds) {
        rootDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    /**
     * Waits for a target URL to be loaded, and ensures the page loader (spinner) is gone.
     * Also waits 2 seconds afterward to allow for final JS/rendering.
     *
     * @param URL the expected URL to wait for
     */
    public static void waitForPageToLoad(String URL) {
        // Wait for URL match
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlToBe(URL));

        try {
            // Wait until loader disappears
            explicitWait(WAIT_UNTIL_LOADS.getSeconds())
                    .until(driver -> !new PageBase().isPageLoaderShown());
        } catch (TimeoutException ignored) {
            // Loader might already be gone—safe to continue
        }

        sleep(2000); // Give some buffer time after loader disappears
    }

    /**
     * Sleeps for the given milliseconds.
     * Used when explicit wait is not suitable (e.g., animations).
     *
     * @param milliseconds time to sleep
     */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // Rethrow as unchecked exception
        }
    }

    /**
     * Enum for common wait durations used across tests.
     * Improves readability and prevents magic numbers.
     */
    public enum Waits {
        WAIT_UNTIL_LOADS(30),          // Long wait for full page loads
        WAIT_UNTIL_DISPLAYED(5),       // Short wait for UI elements to appear
        WAIT_LONG_UNTIL_DISPLAYED(15), // Extended version of above
        WAIT_UNTIL_CLICKABLE(5),       // Wait for clickable elements
        WAIT_TEMP(1),                  // Small temporary pause
        WAIT_TILL_IT_READY(10);        // Custom wait for confirmation messages, etc.

        private final int seconds;

        Waits(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return seconds;
        }
    }
}
