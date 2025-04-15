package nazeel.test_suites.Setup_Menus;

import nazeel.base.TestBase;
import nazeel.pages.DashboardPage;
import nazeel.pages.SuppliesUnitUsagesPage;
import nazeel.pages.LoginPage;
import org.testng.annotations.Test;

public class SuppliesUnitUsagesClass extends TestBase {


   @Test (priority = 1 , testName = "TC01 - Login", suiteName = "Guest Supplies Usage")
    public void TC01_testLogin() throws Exception {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("maiow");
        loginPage.insertPassword("123456Mm&&");
        loginPage.insertAccessCode("01373");
        loginPage.clickLoginButton();
        Thread.sleep(4000);
        //** Select Property **//****
        loginPage.Select_One_Property_("P01558");
        Thread.sleep(3000);
        //loginPage.userVerificationLaterButton_click();
    }

    @Test (priority = 2, testName = "TC02 - AddNew_SuppliesUnitUsages", suiteName = "Guest Supplies Usage")
    public void TC02_AddNew_SuppliesUnitUsages() throws Exception {

     //   Thread.sleep(4000);

        DashboardPage DashboardPage = new DashboardPage();

        DashboardPage.OpenSuppliesUnitUsagesPage();
        Thread.sleep(4000);
        //** open Add new record here !!
        SuppliesUnitUsagesPage SuppliesUnitUsagesPage = new SuppliesUnitUsagesPage();
        SuppliesUnitUsagesPage.Click_Add_New_Unit_Usage_buttons();
        Thread.sleep(7000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectUnitType("Room with Hall");
        Thread.sleep(4000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectCategory("meals");
        Thread.sleep(3000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectSupply("measlfirst");
        Thread.sleep(1000);
        SuppliesUnitUsagesPage.Enter_Daily_Qty_AND_Monthly_Qty("1","2");
        Thread.sleep(1000);
        SuppliesUnitUsagesPage.Append();
        Thread.sleep(1000);
       SuppliesUnitUsagesPage.Save();
        Thread.sleep(1000);
        //Saved Successfully
        DashboardPage.AssertToastMessagesContains("tttes");

    }
}



