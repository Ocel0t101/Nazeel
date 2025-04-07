package nazeel;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

public class TestBase {
    private static WebDriver rootDriver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        rootDriver = new EdgeDriver();
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
}
