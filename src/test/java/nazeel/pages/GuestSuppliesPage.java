package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static nazeel.TestBase.getRootDriver;

public class GuestSuppliesPage {
    private final By categoryDropboxBy = By.cssSelector("input[placeholder='Category']");
    private final By categoryOptionsBy = By.cssSelector("li[role='option']");
    private final By supplyDropboxBy = By.cssSelector("input[placeholder='Supply']");
    private final By quantityBy = By.cssSelector("input[placeholder='Supply']");
    private final By UnitTypesBy = By.cssSelector("kendo-multiselect[name='unitType']");
    private final By UnitNumberBy = By.cssSelector("kendo-multiselect[name='unitNumber']");
    private final By appendButton = By.className("n-button n-button--primary");
    public final static String URL = "https://staging.nazeel.net:9002/guest-supplies/create-supplies-order";

    private WebElement getCategoryDropbox(){
        return getRootDriver().findElement(categoryDropboxBy);
    }

    private WebElement getCategoryOption(int index){
        return getRootDriver().findElements(categoryOptionsBy).get(index);
    }

    public void selectCategoryDropbox(){
        getCategoryDropbox().click();
    }

    public void selectCategoryOption(int index){
        getCategoryOption(index).click();
    }


}
