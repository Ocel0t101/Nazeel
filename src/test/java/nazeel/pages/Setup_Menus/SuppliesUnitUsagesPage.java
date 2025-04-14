package nazeel.pages;

import org.openqa.selenium.By;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

public class SuppliesUnitUsagesPage {

    // Locators
    private final By Add_New_Unit_Usage = By.xpath("//div[2]/div/button");
    private final By UnitType_dropdownField = By.xpath("//div/kendo-searchbar/input");
    private final By dailyQuantityCriteria = By.name("dailyQuentityCriteria");
    private final By weeklyQuantityCriteria = By.name("weeklyQuentityCriteria");

    private final By CategoryField = By.xpath("//div[3]/div/kendo-combobox/span/span");
    private final By SupplyField = By.xpath("//div[3]/div[2]/kendo-combobox/span/span/span");
    private final By AppendButton = By.xpath("//div[5]/button");
    private final By SaveButton = By.xpath("//kendo-dialog-actions/button[2]");

    // Actions
    public void Click_Add_New_Unit_Usage_buttons() {
        getRootDriver().findElement(Add_New_Unit_Usage).click();
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

            // selectedItem.getText()); if need to return item
        } catch (NoSuchElementException e) {
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
        } catch (NoSuchElementException e) {
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
        } catch (NoSuchElementException e) {
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



