package nazeel.test_suites;

import nazeel.TestBase;
import nazeel.pages.LoginPage;
import org.testng.annotations.Test;

public class SuppliesUnitUsagesClass extends TestBase {

    @Test(priority = 1)
    public void testLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("maiow");
        loginPage.insertPassword("123456Mm&&");
        loginPage.insertAccessCode("01373");
        loginPage.clickLoginButton();
        loginPage.Select_One_Property_("p03156");

    }



}
