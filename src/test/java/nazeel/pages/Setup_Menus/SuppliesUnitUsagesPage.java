package nazeel.pages.Setup_Menus;

import org.openqa.selenium.By;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

public class SuppliesUnitUsagesPage {

    // Locators
    private final By Add_New_Unit_Usage = By.xpath("//div[2]/div/button");
    private final By UnitType_dropdownField = By.xpath("//div/kendo-searchbar/input");

    private final By EditUnitType_dropdownField = By.xpath("//div[4]/div/kendo-combobox/span/span/span");

    private final By dailyQuantityCriteria = By.name("dailyQuentityCriteria");
    private final By weeklyQuantityCriteria = By.name("weeklyQuentityCriteria");

    private final By CategoryField = By.xpath("//div[3]/div/kendo-combobox/span/span");

    private final By CategoryField_edit = By.xpath("//div[4]/div/kendo-combobox/span/span/span");

    private final By SupplyField = By.xpath("//div[3]/div[2]/kendo-combobox/span/span/span");

    private final By SupplyField_edit = By.xpath("//div[4]/div[2]/kendo-combobox/span/span/span");

    //xpath=
    private final By AppendButton = By.xpath("//div[5]/button");
    private final By SaveButton = By.xpath("//kendo-dialog-actions/button[2]");
    private final By Filter = By.xpath("//button[2]");
    private final By UnitType_dropdownField_Filter = By.xpath("//div[2]/kendo-combobox/span/span/span");
    private final By Search = By.xpath("//div[4]/div/button");
    private final By EditButton = By.xpath("//tr[1]/td[7]/div/div/button");

    private final By Delete_button = By.xpath("//tr/td[5]/div/div/button");
    private final By Yes_B = By.xpath("//div/div[3]/button");

    // Actions

    public void DeleteRecord_() throws InterruptedException {
        getRootDriver().findElement(Delete_button).click();
        Thread.sleep(1000);
        getRootDriver().findElement(Yes_B).click();
    }

    public void ClickEditButton() {
        getRootDriver().findElement(EditButton).click();
    }
    public void Click_Add_New_Unit_Usage_button() {
        getRootDriver().findElement(Add_New_Unit_Usage).click();
    }

    public void Click_Search_button() {
        getRootDriver().findElement(Search).click();
    }

    public void Click_Filter_button() {
        getRootDriver().findElement(Filter).click();
    }

    public void Verify_Unit_Types_In_Grid (String UnitTypeIs ) {
        List<WebElement> items = getRootDriver().findElements(By.xpath("//tr/td[2]"));
        for (WebElement item : items) {
           String actualUnitsTypeIs = item.getText();
            Assert.assertEquals(actualUnitsTypeIs, UnitTypeIs, "there are a type in the grid  Mismatch" );
        }
    }

    public void Filter_bySelectUnitType(String UnitTypeIs) throws Exception {
        getRootDriver().findElement(UnitType_dropdownField_Filter).click();
        Thread.sleep(1000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//li"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(UnitTypeIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("unit Type not here"));
            selectedItem.click();
            Thread.sleep(1000);
        } catch (NoSuchElementException _) {
        }
    }

    public void Add_New_Guest_Supplies_bySelectUnitType(String UnitTypeIs) throws Exception {
        getRootDriver().findElement(UnitType_dropdownField).click();
        Thread.sleep(1000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//li/label"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(UnitTypeIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("unit Type not here"));

            selectedItem.click();
            Thread.sleep(1000);
            getRootDriver().findElement(dailyQuantityCriteria).click();

        } catch (NoSuchElementException _) {
        }
    }

    public void Add_New_Guest_Supplies_bySelectCategory(String CategoryIs) throws Exception {
        getRootDriver().findElement(CategoryField).click();
        Thread.sleep(2000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//kendo-list/div/ul/li"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(CategoryIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Category Type not here"));
            selectedItem.click();
        } catch (NoSuchElementException _) {
        }
    }

    public void Edit_Guest_Supplies_bySelectCategory(String CategoryIs) throws Exception {
        getRootDriver().findElement(CategoryField_edit).click();
        Thread.sleep(2000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//kendo-list/div/ul/li"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(CategoryIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Category Type not here"));
            selectedItem.click();
        } catch (NoSuchElementException _) {
        }
    }

    public void Add_New_Guest_Supplies_bySelectSupply(String SupplyIs) throws Exception {
        getRootDriver().findElement(SupplyField).click();
        Thread.sleep(1000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//kendo-list/div/ul/li"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(SupplyIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Supply not here"));
            selectedItem.click();
        } catch (NoSuchElementException _) {
        }
    }

    public void Edit_Guest_Supplies_bySelectSupply(String SupplyIs) throws Exception {
        getRootDriver().findElement(SupplyField_edit).click();
        Thread.sleep(1000);
        List<WebElement> items = getRootDriver().findElements(By.xpath("//kendo-list/div/ul/li"));
        try {
            WebElement selectedItem = items.stream()
                    .filter(i -> i.getText().toLowerCase().contains(SupplyIs.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Supply not here"));
            selectedItem.click();
        } catch (NoSuchElementException _) {
        }
    }


    public void Enter_Daily_Qty_AND_Monthly_Qty(String Daily_Qty, String Monthly_Qty) throws InterruptedException {
    getRootDriver().findElement(dailyQuantityCriteria).sendKeys(Daily_Qty);
        Thread.sleep(3000);
        getRootDriver().findElement(weeklyQuantityCriteria).sendKeys(Monthly_Qty);
    }
    public void Append() {
        getRootDriver().findElement(AppendButton).click();
    }

    public void Save() {
        getRootDriver().findElement(SaveButton).click();
    }
}



