package nazeel.test_suites;

import nazeel.TestBase;
import nazeel.pages.DashboardPage;
import nazeel.pages.GuestSuppliesPage;
import nazeel.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static nazeel.TestBase.Waits.*;

public class GuestSuppliesTS extends TestBase {
    private final GuestSuppliesPage suppliesPage = new GuestSuppliesPage();

    @BeforeClass
    public void tempProceedToDashboard() {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("MGamalTest");
        loginPage.insertPassword("132456789Aa@");
        loginPage.insertAccessCode("01456");
        loginPage.clickLoginButton();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loginPage.selectPropertyByIndex(2);
    }

    @Test(testName = "Request valid accomplished order", suiteName = "Guest Supplies")
    public void tc01ValidAccomplishedOrder() {
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlToBe(DashboardPage.URL));
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());
        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0)
                .clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.selectSupplyOption(0);
        suppliesPage.insertQuantity(2)
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.selectUnitTypesOption(0);
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(1)
                .clickAppendButton();
    }


}
