package nazeel.test_suites;

import nazeel.TestBase;
import nazeel.pages.DashboardPage;
import nazeel.pages.SuppliesUnitUsagesPage;
import nazeel.pages.LoginPage;
import org.testng.annotations.Test;

public class SuppliesUnitUsagesClass extends TestBase {

    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("maiow");
        loginPage.insertPassword("123456Mm&&");
        loginPage.insertAccessCode("01373");
        loginPage.clickLoginButton();
        Thread.sleep(4000);

        //** Select Property **//****

        loginPage.Select_One_Property_("P01558");

           // to be in Test= 2

        Thread.sleep(4000);

        //**  Open clickOnSuppliesUnitUsagesPage**//****

        DashboardPage DashboardPage = new DashboardPage();

        DashboardPage.OpenSuppliesUnitUsagesPage();

        Thread.sleep(4000);

        //** open Add new record here !!

        SuppliesUnitUsagesPage SuppliesUnitUsagesPage = new SuppliesUnitUsagesPage();

        SuppliesUnitUsagesPage.Click_Add_New_Unit_Usage_buttons();

        Thread.sleep(4000);
        try {

            SuppliesUnitUsagesPage.selectListItem("Single Room");

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

      /*  @Test(priority = 2)
        public void Add_UnitUsages() throws InterruptedException {

            }
        }

       */
    }
}

