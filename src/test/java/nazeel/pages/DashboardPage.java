package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.asserts.SoftAssert;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nazeel.TestBase.getRootDriver;

public class DashboardPage extends SoftAssert {

    //Locators
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

        WebElement element = getRootDriver().findElement( By.xpath("//a[12]"));
        ((JavascriptExecutor) getRootDriver()).executeScript("arguments[0].scrollIntoView(true);", element);

        getRootDriver().findElement(SUsages).click();

        Thread.sleep(1000);

        getRootDriver().findElement(SuppliesUnitUsages).click();

    }
    public void AssertToastMessagesContains(String mesage) {
        //FixMe error with the toast handling
        List<WebElement> toastMsgs = getRootDriver().findElements(By.className("toast-message"));

        List<String> mesagesContents = new ArrayList<>();

        for (WebElement toast : toastMsgs) {

            if (toast.getText().trim().toLowerCase().contains(mesage.toLowerCase())) {
                this.assertTrue(true);
                return;
            } else {
                mesagesContents.add(toast.getText());
            }
        }
        if (!mesagesContents.isEmpty())
            //noinspection DataFlowIssue
            this.assertFalse(true, "actual : " + Arrays.toString(mesagesContents.toArray()) + "\nExpected : " + mesage.toLowerCase() + "\n");

    }

}


