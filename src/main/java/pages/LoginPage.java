package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By userNameField = By.id("usern");
    private final By passwordField = By.id("pass");
    private final By accessCodeField = By.id("acc");
    private final By loginButton = By.xpath("//button[contains(text(),'Login')]");
    //private final By loader = By.cssSelector(".loader-circle");

    //Actions
    public void insertUsername(String username){
        driver.findElement(userNameField).sendKeys(username);
    }
    public void insertPassword(String password){
        driver.findElement(passwordField).sendKeys(password);
    }
    public void insertAccessCode(String accessCode){
        driver.findElement(accessCodeField).sendKeys(accessCode);
    }
    public DashboardPage clickLoginButton(){
        driver.findElement(loginButton).click();
        return new DashboardPage(driver);
    }
}
