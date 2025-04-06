package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {
    WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By userVerificationLaterButton = By.xpath("//*[contains(text(),'Later')]");
    private final By reservationsMenuNode = By.cssSelector("[href = '/reservations']");

    //Actions
    public void clickOnUserVerificationLaterButton() {
        driver.findElement(userVerificationLaterButton).click();
    }
    /*public ReservationsPage clickOnReservationsPage(){
        driver.findElement(reservationsMenuNode).click();
        return new ReservationsPage(driver);
    }*/
}
