package nazeel.test_suites.Setup_Menus;

import nazeel.base.TestBase;
import nazeel.pages.DashboardPage;
import nazeel.pages.Setup_Menus.SuppliesUnitUsagesPage;
import nazeel.pages.LoginPage;
import org.testng.annotations.Test;

public class supplies_unit_usages_class extends TestBase {

    DashboardPage DashboardPage = new DashboardPage();
    SuppliesUnitUsagesPage SuppliesUnitUsagesPage = new SuppliesUnitUsagesPage();

   @Test (priority = 1 , testName = "TC01 - Login", suiteName = "Guest Supplies Usage")
    public void TC01_testLogin() throws Exception {
        LoginPage loginPage = new LoginPage();
//        loginPage.insertUsername("maiow");
//        loginPage.insertPassword("123456Mm&&");
//        loginPage.insertAccessCode("01373");
       loginPage.insertUsername("mai admin");
       loginPage.insertPassword("123456Mm&&");
       loginPage.insertAccessCode("");
        loginPage.clickLoginButton();
        Thread.sleep(9000);
       // ** Select Property **//****
        loginPage.Select_One_Property_("P01558");
        Thread.sleep(6000);
       DashboardPage.OpenSuppliesUnitUsagesPage();
       Thread.sleep(4000);
    }

    @Test (priority = 2, testName = "TC02 - AddNew_SuppliesUnitUsages", suiteName = "Guest Supplies Usage")
    public void TC02_AddNew_SuppliesUnitUsages() throws Exception {
        //** open Add new record here !!
        SuppliesUnitUsagesPage.Click_Add_New_Unit_Usage_button();
        Thread.sleep(7000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectUnitType("Room with Hall");
        Thread.sleep(4000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectCategory("meals");
        Thread.sleep(3000);
        SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectSupply("meals_first");
        Thread.sleep(1000);
        SuppliesUnitUsagesPage.Enter_Daily_Qty_AND_Monthly_Qty("1","2");
        Thread.sleep(1000);
        SuppliesUnitUsagesPage.Append();
        Thread.sleep(1000);
       SuppliesUnitUsagesPage.Save();
        Thread.sleep(1000);
        //Saved Successfully
        DashboardPage.AssertToastMessagesContains("Saved Successfully");}

@Test (priority = 3, testName = "TC03 - Edit_SuppliesUnitUsages", suiteName = "Guest Supplies Usage")

public void TC03_Edit_SuppliesUnitUsages() throws Exception {

    SuppliesUnitUsagesPage.Click_Filter_button();
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Filter_bySelectUnitType("Room with Hall");
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Click_Search_button();
    Thread.sleep(1000);
   SuppliesUnitUsagesPage.Verify_Unit_Types_In_Grid("Room with Hall");
    Thread.sleep(1000);
   SuppliesUnitUsagesPage.ClickEditButton();
    Thread.sleep(1000);
    // Edit
    SuppliesUnitUsagesPage.DeleteRecord_();
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Add_New_Guest_Supplies_bySelectUnitType("Two Rooms with Hall");
    Thread.sleep(4000);
    SuppliesUnitUsagesPage.Edit_Guest_Supplies_bySelectCategory("meals");
    Thread.sleep(3000);
    SuppliesUnitUsagesPage.Edit_Guest_Supplies_bySelectSupply("meals_first");
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Enter_Daily_Qty_AND_Monthly_Qty("3","3");
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Append();
    Thread.sleep(1000);
    SuppliesUnitUsagesPage.Save();
    Thread.sleep(1000);
    //Saved Successfully
    DashboardPage.AssertToastMessagesContains("Saved Successfully");}
}
