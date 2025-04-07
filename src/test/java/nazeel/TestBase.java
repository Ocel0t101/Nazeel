package nazeel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public class TestBase {
    private static WebDriver rootDriver;

    @BeforeClass
    public void setUp() {
        rootDriver = new EdgeDriver();
        rootDriver.manage().window().maximize();
        rootDriver.get("https://staging.nazeel.net:9002/login");
    }

    @AfterMethod
    public void goHome() {
        rootDriver.get("https://staging.nazeel.net:9002/dashboard");
    }

    @AfterSuite
    public void tearDown() {
        rootDriver.quit();
    }

    public static WebDriver getRootDriver() {
        return rootDriver;
    }
}
