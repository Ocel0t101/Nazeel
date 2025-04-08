package nazeel.pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;


import java.util.List;

import static nazeel.TestBase.getRootDriver;

public class SuppliesUnitUsagesPage {

    // Locators
    private final By Add_New_Unit_Usage = By.xpath("//div[2]/div/button");
    private final By dropdownLocator = By.xpath("//div/kendo-searchbar/input");
    private final By listItemText = By.xpath("//div/kendo-searchbar/input");

  // Actions
    public void Click_Add_New_Unit_Usage_buttons() {
        getRootDriver().findElement(Add_New_Unit_Usage).click();
    }

    public void selectListItem(String listItemText) throws Exception {

        getRootDriver().findElement(dropdownLocator).click();

        // Locate all <li> elements within the dropdown
        List<WebElement> items = getRootDriver().findElements(By.xpath("//li/label"));

        for (WebElement ele : items) {

            String firstElemen = ele.getText().trim();

            if (firstElemen.equalsIgnoreCase(listItemText)) {

                ele.click();

                return;
            }
        }
    }
    }



