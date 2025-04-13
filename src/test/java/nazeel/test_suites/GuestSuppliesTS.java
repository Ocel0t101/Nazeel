package nazeel.test_suites;

import nazeel.TestBase;
import nazeel.pages.LoginPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GuestSuppliesTS extends TestBase {

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

        loginPage.selectPropertyByIndex(1);
    }

    @Test(testName = "Request valid accomplished order", suiteName = "Guest Supplies")
    public void tc01ValidAccomplishedOrder() {


    }


}
