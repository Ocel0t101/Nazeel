package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static nazeel.TestBase.getRootDriver;

public class LoginPage {
    //Locators
    private final By userNameField = By.id("usern");
    private final By passwordField = By.id("pass");
    private final By accessCodeField = By.id("acc");
    private final By loginButton = By.xpath("//button[contains(text(),'Login')]");
    private final By companyProperties = By.cssSelector("tbody[role='presentation']>tr");
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

    private List<WebElement> getCompanyProperties() {
        return getRootDriver().findElements(companyProperties);
    }

    public DashboardPage clickLoginButton() {
        getRootDriver().findElement(loginButton).click();
        return new DashboardPage();
    }

    public void selectPropertyByIndex(int index) {
        getCompanyProperties().get(index).click();
    }
}
