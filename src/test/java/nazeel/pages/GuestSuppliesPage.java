package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static nazeel.TestBase.getRootDriver;

public class GuestSuppliesPage {
    private final By categoryDropboxBy = By.cssSelector("kendo-combobox[name='category']>span>span");
    private final By optionsBy = By.cssSelector("li[role='option']");
    private final By supplyDropboxBy = By.cssSelector("kendo-combobox[name='supply']>span>span");
    private final By quantityBy = By.cssSelector("input[placeholder='Quantity']");
    private final By unitTypesBy = By.cssSelector("kendo-multiselect[name='unitType']");
    private final By unitNumberBy = By.cssSelector("kendo-multiselect[name='unitNumber']");
    private final By appendButton = By.className(".n-button.n-button--primary");
    public final static String URL = "https://staging.nazeel.net:9002/guest-supplies/create-supplies-order";

    /**
     * Locators
     */
    private WebElement getCategoryDropbox() {
        return getRootDriver().findElement(categoryDropboxBy);
    }

    private List<WebElement> getCategoryOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private WebElement getSupplyDropbox() {
        return getRootDriver().findElement(supplyDropboxBy);
    }

    private List<WebElement> getSupplyOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    public WebElement getQuantityTextBox() {
        return getRootDriver().findElement(quantityBy);
    }

    public WebElement getUnitTypesMultiSelector() {
        return getRootDriver().findElement(unitTypesBy);
    }

    private List<WebElement> getUnitTypesOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    public WebElement getUnitNumberMultiSelector() {
        return getRootDriver().findElement(unitNumberBy);
    }

    private WebElement getUnitNumberOption(int index) {
        return getRootDriver().findElements(optionsBy).get(index);
    }

    public WebElement getAppendButton() {
        return getRootDriver().findElement(appendButton);
    }

    /**
     * Actions
     */
    public GuestSuppliesPage navigateToGuestSuppliesPage() {
        getRootDriver().get(URL);
        return this;
    }

    public GuestSuppliesPage clickCategoryDropbox() {
        getCategoryDropbox().click();
        return this;
    }

    public boolean isCategoryDropboxShown() {
        return getCategoryDropbox().isDisplayed();
    }

    public GuestSuppliesPage selectCategoryOption(int index) {
        getCategoryOptions().get(index).click();
        return this;
    }

    public boolean isCategoryOptionsEmpty() {
        return getCategoryOptions().isEmpty();
    }

    public GuestSuppliesPage clickSupplyDropbox() {
        getSupplyDropbox().click();
        return this;
    }

    public GuestSuppliesPage selectSupplyOption(int index) {
        getSupplyOptions().get(index).click();
        return this;
    }

    public boolean isSupplyOptionsEnabled() {
        return getSupplyOptions().getFirst().isEnabled();
    }

    public GuestSuppliesPage insertQuantity(int quantity) {
        getQuantityTextBox().sendKeys(String.valueOf(quantity));
        return this;
    }

    public GuestSuppliesPage clickUnitTypes() {
        getUnitTypesMultiSelector().click();
        return this;
    }

    public GuestSuppliesPage selectUnitTypesOption(int index) {
        getUnitTypesOptions().get(index).click();
        return this;
    }

    public boolean isUnitTypesEmpty(){
        return getUnitTypesOptions().isEmpty();
    }

    public GuestSuppliesPage clickUnitNumber() {
        getUnitNumberMultiSelector().click();
        return this;
    }

    public GuestSuppliesPage selectUnitNumberOption(int index) {
        getUnitNumberOption(index).click();
        return this;
    }

    public void clickAppendButton() {
        getAppendButton().click();
    }
}
