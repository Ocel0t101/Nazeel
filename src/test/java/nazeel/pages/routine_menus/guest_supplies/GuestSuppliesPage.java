package nazeel.pages.routine_menus.guest_supplies;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

public class GuestSuppliesPage {
    private final By categoryDropboxBy = By.cssSelector("kendo-combobox[name='category']>span>span");
    private final By optionsBy = By.cssSelector("div>ul>li[role='option']");
    private final By supplyDropboxBy = By.cssSelector("kendo-combobox[name='supply']>span>span");
    private final By quantityInputBy = By.cssSelector("input[placeholder='Quantity']");
    private final By unitTypesInputBy = By.cssSelector("kendo-multiselect[name='unitType']");
    private final By unitNumberInputBy = By.cssSelector("kendo-multiselect[name='unitNumber']");
    private final By appendButtonBy = By.className("n-button--primary");
    public final static String URL = "https://staging.nazeel.net:9002/guest-supplies/create-supplies-order";
    private final By addedSuppliesTableBy = By.cssSelector("tbody>tr[role='row']");
    private final By createAccomplishedOrderButtonBy = By.cssSelector(".popup__btn.popup__btn--blue.h-35.d-flex.flex-row-reverse");
    private final By quantityPerUnitValuesRowsBy = By.cssSelector("tbody[role='presentation']>tr>td:nth-child(3)");
    private final By commentInputBy = By.cssSelector("div>textarea.dropdown-toggle");
    private final By clearButtonsBy = By.cssSelector("span[role='button']");

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

    private WebElement getQuantityTextBox() {
        return getRootDriver().findElement(quantityInputBy);
    }

    private WebElement getUnitTypesMultiSelector() {
        return getRootDriver().findElement(unitTypesInputBy);
    }

    private List<WebElement> getUnitTypesOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private WebElement getUnitNumberMultiSelector() {
        return getRootDriver().findElement(unitNumberInputBy);
    }

    private WebElement getUnitNumberOption(int index) {
        return getRootDriver().findElements(optionsBy).get(index);
    }

    private WebElement getAppendButtonBy() {
        return getRootDriver().findElement(appendButtonBy);
    }

    private List<WebElement> getAddedSuppliesTable() {
        return getRootDriver().findElements(addedSuppliesTableBy);
    }

    private WebElement getCreateAccomplishedButton() {
        return getRootDriver().findElement(createAccomplishedOrderButtonBy);
    }

    private List<WebElement> getQuantityPerUnitValuesRows() {
        return getRootDriver().findElements(quantityPerUnitValuesRowsBy);
    }

    private WebElement getCommentTextBox() {
        return getRootDriver().findElement(commentInputBy);
    }

    private List<WebElement> getClearButtons() {
        return getRootDriver().findElements(clearButtonsBy);
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
        try {
            return getCategoryDropbox().isDisplayed() && getCategoryDropbox().getCssValue("opacity").equals("1");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public GuestSuppliesPage selectCategoryOption(int index) {
        getCategoryOptions().get(index).click();
        return this;
    }

    public boolean isCategoryOptionsDisplayed() {
        return getCategoryOptions().getFirst().getCssValue("opacity").equals("1");
    }

    public GuestSuppliesPage clickSupplyDropbox() {
        getSupplyDropbox().click();
        return this;
    }

    public GuestSuppliesPage selectSupplyOption(int index) {
        getSupplyOptions().get(index).click();
        return this;
    }

    public boolean isSupplyOptionsDisplayed() {
        return getSupplyOptions().getFirst().getCssValue("opacity").equals("1");
    }

    public GuestSuppliesPage insertQuantity(String quantity) {
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

    public boolean isUnitTypesOptionsDisplayed() {
        return getUnitTypesOptions().getFirst().getCssValue("opacity").equals("1");
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
        getAppendButtonBy().click();
    }

    public int countOfAddedSupplies() {
        return getAddedSuppliesTable().size();
    }

    public GuestSuppliesPage clickCreateAccomplishedButton() {
        getCreateAccomplishedButton().click();
        return this;
    }

    public String quantityPerUnitValueByIndex(int index) {
        return getQuantityPerUnitValuesRows().get(index).getText();
    }

    public GuestSuppliesPage insertComment(String comment) {
        getCommentTextBox().sendKeys(comment);
        return this;
    }

    public int getClearButtonsCount() {
        return getClearButtons().size();
    }
}
