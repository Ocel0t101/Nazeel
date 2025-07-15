package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static nazeel.base.TestBase.getRootDriver;

public class DashboardPage {
    //Locators
    private final By userVerificationLaterButton = By.xpath("//*[contains(text(),'Later')]");
    private final By reservationsMenuNode = By.cssSelector("[href = '/reservations']");
    private final By userNameBy = By.cssSelector("div[class='user-menu__name']>span");
    public final static String URL = "https://staging.nazeel.net:9002/dashboard";

    private WebElement getUserNameLabel() {
        return getRootDriver().findElement(userNameBy);
    }

    //Actions
    public void clickOnUserVerificationLaterButton() {
        getRootDriver().findElement(userVerificationLaterButton).click();
    }

    public String getUserName() {
        return getUserNameLabel().getText().replace("...", "").trim();
    }

    /*public ReservationsPage clickOnReservationsPage(){
        driver.findElement(reservationsMenuNode).click();
        return new ReservationsPage(driver);
    }*/
}
