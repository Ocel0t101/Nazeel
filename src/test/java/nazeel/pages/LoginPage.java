package nazeel.pages;

import org.openqa.selenium.By;

import static nazeel.TestBase.getRootDriver;

public class LoginPage {
    //Locators
    private final By userNameField = By.id("usern");
    private final By passwordField = By.id("pass");
    private final By accessCodeField = By.id("acc");
    private final By loginButton = By.xpath("//button[contains(text(),'Login')]");
    //private final By loader = By.cssSelector(".loader-circle");

    //Actions
    public void insertUsername(String username) {
        getRootDriver().findElement(userNameField).sendKeys(username);
    }

    public void insertPassword(String password) {
        getRootDriver().findElement(passwordField).sendKeys(password);
    }

    public void insertAccessCode(String accessCode) {
        getRootDriver().findElement(accessCodeField).sendKeys(accessCode);
    }

    public DashboardPage clickLoginButton() {
        getRootDriver().findElement(loginButton).click();
        return new DashboardPage();
    }
}
