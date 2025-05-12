package nazeel.pages;

import nazeel.utils.DelayedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

public class DashboardPage extends SoftAssert {

    //Locators
    private static final By SetupMenu = By.xpath("//a[contains(text(),'Setup')]");
    private static final By SuppliesUnitUsages = By.xpath("//div[15]/a[3]");
    private static final By SUsages = By.xpath("//a[12]");
    private final By userVerificationLaterButton = By.xpath("//*[contains(text(),'Later')]");
    private final By reservationsMenuNode = By.cssSelector("[href = '/reservations']");
    public final static String URL = "https://staging.nazeel.net:9002/dashboard";

    //Actions
   /* public void clickOnUserVerificationLaterButton() {
        getRootDriver().findElement(userVerificationLaterButton).click(); }
   */

    public  void OpenSuppliesUnitUsagesPage() throws InterruptedException {
        getRootDriver().findElement(SetupMenu).click();

        Thread.sleep(3000);

        WebElement element = getRootDriver().findElement( By.xpath("//a[12]"));

        DelayedWebDriver.DelayedWebElement delayedWebElement = new DelayedWebDriver.DelayedWebElement(element , 1000);

         element = delayedWebElement.getWrappedElement();

        JavascriptExecutor jsExecutor;

            jsExecutor = (JavascriptExecutor) getRootDriver();

        jsExecutor.executeScript("arguments[0].click();", element);

        ((JavascriptExecutor) getRootDriver()).executeScript("arguments[0].scrollIntoView(true);", element);

        Thread.sleep(3000);

        getRootDriver().findElement(SuppliesUnitUsages).click();
    }

    public void AssertToastMessagesContains(String actualToastText) {
        //WebElement expectedToast= getRootDriver().findElement(By.cssSelector("div.toast-message"));

        WebDriverWait wait = new WebDriverWait(getRootDriver(), Duration.ofSeconds(10));
        WebElement expectedToast = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.visibilityOfElementLocated(By.className("toast-message"))));

        String expectedToastText = expectedToast.getText().trim();
        // Assert the toast text
        Assert.assertEquals(actualToastText, expectedToastText, "Toast message did not match!");
    }
}
