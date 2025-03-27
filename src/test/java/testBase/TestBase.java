package testBase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import pages.LoginPage;

public class TestBase {
    protected WebDriver driver;
    protected LoginPage loginPage;
    @BeforeClass
    public void setUp(){
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://staging.nazeel.net:9002/login");
        loginPage = new LoginPage(driver);
    }
    @AfterMethod
    public void goHome(){
        driver.get("https://staging.nazeel.net:9002/dashboard");
    }
    @AfterSuite
    public void tearDown(){
        driver.quit();
    }
}
