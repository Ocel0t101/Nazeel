package nazeel.test_suites.Setup_Menus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nazeel.base.TestBase;
import nazeel.pages.Routine_Menus.DashboardPage;
import nazeel.pages.Setup_Menus.SuppliesUnitUsagesPage;
import nazeel.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.time.Duration;

public class SuppliesUnitUsagesTest extends TestBase {

    DashboardPage DashboardPage = new DashboardPage();
    SuppliesUnitUsagesPage SuppliesUnitUsagesPage = new SuppliesUnitUsagesPage();
    LoginPage loginPage = new LoginPage();

    private static final String STORAGE_FILE = "sessionData.json";

    @Test(priority = 1)
    public void loadSessionDataAndLogin() throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(new FileReader(STORAGE_FILE), JsonObject.class);

        //
        getRootDriver().get("https://app-simulation.nazeel.net/login");

        //
        getRootDriver().manage().deleteAllCookies();
        Thread.sleep(500);

        // --- Load Cookies ---
        JsonArray cookiesArray = jsonObject.getAsJsonArray("cookies");
        for (JsonElement element : cookiesArray) {
            JsonObject c = element.getAsJsonObject();
            Cookie cookie = new Cookie.Builder(c.get("name").getAsString(), c.get("value").getAsString())
                    .domain(c.get("domain").getAsString())
                    .path(c.get("path").getAsString())
                    .isSecure(c.get("isSecure").getAsBoolean())
                    .build();
            getRootDriver().manage().addCookie(cookie);
        }
        System.out.println("✅ Cookies loaded into browser.");

        // --- Load LocalStorage ---
        JsonObject localStorage = jsonObject.getAsJsonObject("localStorage");
        JavascriptExecutor js = (JavascriptExecutor) getRootDriver();
        for (String key : localStorage.keySet()) {
            String value = localStorage.get(key).getAsString();
            js.executeScript("localStorage.setItem(arguments[0], arguments[1]);", key, value);
        }

        // --- Load SessionStorage ---
        JsonObject sessionStorage = jsonObject.getAsJsonObject("sessionStorage");
        for (String key : sessionStorage.keySet()) {
            String value = sessionStorage.get(key).getAsString();
            js.executeScript("sessionStorage.setItem(arguments[0], arguments[1]);", key, value);
        }

        // --- Refresh  ---
        Thread.sleep(1000);
        getRootDriver().navigate().refresh();
        Thread.sleep(2000);

        String currentUrl = getRootDriver().getCurrentUrl();
        System.out.println("🌐 URL after refresh: " + currentUrl);

        // ---ـDashboard ---
        if (currentUrl.contains("/login")) {
            System.out.println("⚠️ Still on login, trying to go to /dashboard manually...");
            getRootDriver().navigate().to("https://app-simulation.nazeel.net/dashboard");
            Thread.sleep(2000);
        }

        try {
            WebElement dashboardElement = new WebDriverWait(getRootDriver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.page-header__title")));
            Assert.assertTrue(dashboardElement.isDisplayed(), "❌ Still not in Dashboard.");
            System.out.println("✅ Logged in successfully and dashboard is visible!");
        } catch (Exception e) {
            System.out.println("⚠️ Not in Dashboard yet. Trying fallback: login manually...");
//            try {
//
//                loginPage.insertUsername("mai admin");
//                loginPage.insertPassword("123456Mm&&");
//                loginPage.insertAccessCode("");
//                loginPage.clickLoginButton();
//                Thread.sleep(9000);
//                loginPage.Select_One_Property_("P01558");
//                System.out.println("✅ Logged in manually and property selected.");
//            } catch (Exception ex) {
//                System.out.println("❌ Could not login manually. Still stuck before Dashboard.");
//                throw new RuntimeException("Failed to access Dashboard after session load and manual login.");
//            }
        }
    }



    @Test (priority = 2, testName = "TC02 - AddNew_SuppliesUnitUsages", suiteName = "Guest Supplies Usage")
    public void TC02_AddNew_SuppliesUnitUsages() throws Exception {
        DashboardPage.OpenSuppliesUnitUsagesPage();
        Thread.sleep(7000);
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

    @Test (priority = 3)
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