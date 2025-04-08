package nazeel.pages;

import org.openqa.selenium.By;


import static nazeel.TestBase.getRootDriver;

public class DashboardPage {

    //Locators
    private final By userVerificationLaterButton = By.xpath("//*[contains(text(),'Later')]");
    private static final By SetupMenu = By.xpath("//a[contains(text(),'Setup')]");
    private static final By SuppliesUnitUsages = By.xpath("//div[15]/a[3]");
    private static final By SUsages = By.xpath("//a[12]");
    //Actions
   /* public void clickOnUserVerificationLaterButton() {
        getRootDriver().findElement(userVerificationLaterButton).click(); }
    */

    public static void OpenSuppliesUnitUsagesPage() throws InterruptedException {
        getRootDriver().findElement(SetupMenu).click();
        Thread.sleep(3000);

//        WebElement element = getRootDriver().findElement( By.xpath("//a[12]"));
//        ((JavascriptExecutor) getRootDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
//        element.click();

        getRootDriver().findElement(SUsages).click();

        Thread.sleep(1000);

        getRootDriver().findElement(SuppliesUnitUsages).click();

    }
}
