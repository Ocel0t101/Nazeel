package nazeel.pages;

import org.openqa.selenium.By;

import static nazeel.TestBase.getRootDriver;

public class DashboardPage {
    //Locators
    private final By userVerificationLaterButton = By.xpath("//*[contains(text(),'Later')]");
    private final By reservationsMenuNode = By.cssSelector("[href = '/reservations']");
    public final static String URL = "https://staging.nazeel.net:9002/dashboard";

    //Actions
    public void clickOnUserVerificationLaterButton() {
        getRootDriver().findElement(userVerificationLaterButton).click();
    }

    /*public ReservationsPage clickOnReservationsPage(){
        driver.findElement(reservationsMenuNode).click();
        return new ReservationsPage(driver);
    }*/
}
