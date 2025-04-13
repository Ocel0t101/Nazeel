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

public class TestBase {
    private static WebDriver rootDriver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        rootDriver = new DelayedWebDriver(new EdgeDriver(), 1000);
        rootDriver.manage().window().maximize();
        rootDriver.get("https://staging.nazeel.net:9002/login");
    }

    @AfterClass
    public void tearDown() {
        rootDriver.quit();
    }

    public static WebDriver getRootDriver() {
        return rootDriver;
    }

    /**
     * Creates and returns a WebDriverWait instance for explicit waits.
     *
     * @param seconds The duration of the explicit wait in seconds.
     * @return A WebDriverWait instance.
     */
    public static WebDriverWait explicitWait(int seconds) {
        return new WebDriverWait(rootDriver, Duration.ofSeconds(seconds));
    }

    /**
     * Sets an implicit wait for the WebDriver.
     *
     * @param seconds The duration of the implicit wait in seconds.
     */
    public static void implicitWait(int seconds) {
        rootDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public static void waitForPageToLoad(String URL) {
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlToBe(URL));
        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> !new PageBase().isPageLoaderShown());
        } catch (TimeoutException ignored) {
        }
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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
