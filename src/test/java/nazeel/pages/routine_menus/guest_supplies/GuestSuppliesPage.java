package nazeel.pages.routine_menus.guest_supplies;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

import static nazeel.base.TestBase.getRootDriver;

/**
 * Page Object Model class for the "Guest Supplies" page.
 * Contains locators and actions related to adding and managing guest supplies.
 */
public class GuestSuppliesPage {

    // Locators
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
    private final By orderDropButtonBy = By.cssSelector(".popup__btn.popup__btn--blue.h-35.d-flex.flex-row-reverse>div:nth-child(1)");
    private final By createProposedOrderButtonBy = By.cssSelector("kendo-popup");
    private final By deleteAppendedOrderButtonBy = By.cssSelector(".button--danger.k-button");
    private final By editAppendedOrderButtonBy = By.cssSelector(".button--primary.n-table-action.k-button");
    private final By discardOrderButtonBy = By.cssSelector(".n-button.n-button--danger-border.margin-inline");
    private final By cancelButtonBy = By.cssSelector("div.col-md-4>button.n-button.n-button--danger-border");
    private final By unitOptionsLabel = By.cssSelector("label");
    private String selectedCategory, selectedSupply, selectedUnitTypes, selectedUnitNumbers;

    // Locators to interact with UI elements
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

    private WebElement getUnitTypeOptionLabel(WebElement unitTypeOption) {
        return unitTypeOption.findElement(unitOptionsLabel);
    }

    private WebElement getUnitNumberMultiSelector() {
        return getRootDriver().findElement(unitNumberInputBy);
    }

    private List<WebElement> getUnitNumberOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private WebElement getUnitNumberOptionLabel(WebElement unitNumberOption) {
        return unitNumberOption.findElement(unitOptionsLabel);
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

    private WebElement getOrderDropButton() {
        return getRootDriver().findElement(orderDropButtonBy);
    }

    private WebElement getProposedOrderButton() {
        return getRootDriver().findElement(createProposedOrderButtonBy);
    }

    private List<WebElement> getDeleteAppendedOrderButtons() {
        return getRootDriver().findElements(deleteAppendedOrderButtonBy);
    }

    private List<WebElement> getEditAppendedOrderButtons() {
        return getRootDriver().findElements(editAppendedOrderButtonBy);
    }

    private WebElement getDiscardButton() {
        return getRootDriver().findElement(discardOrderButtonBy);
    }

    private WebElement getCancelButton() {
        return getRootDriver().findElement(cancelButtonBy);
    }

    // --------- Page Actions ---------

    /**
     * Navigates to the guest supplies creation page.
     */
    public GuestSuppliesPage navigateToGuestSuppliesPage() {
        getRootDriver().get(URL);
        return this;
    }

    /**
     * Opens the category dropdown.
     */
    public GuestSuppliesPage clickCategoryDropbox() {
        getCategoryDropbox().click();
        return this;
    }

    /**
     * Checks if the category dropdown is visible.
     */
    public boolean isCategoryDropboxShown() {
        try {
            return getCategoryDropbox().isDisplayed() && getCategoryDropbox().getCssValue("opacity").equals("1");
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Selects a category option by index.
     */
    public GuestSuppliesPage selectCategoryOption(int index) {
        selectedCategory = getCategoryOptions().get(index).getText();
        getCategoryOptions().get(index).click();
        return this;
    }

    /**
     * Checks if category options are visible.
     */
    public boolean isCategoryOptionsDisplayed() {
        return getCategoryOptions().getFirst().getCssValue("opacity").equals("1");
    }

    /**
     * Opens the supply dropdown.
     */
    public GuestSuppliesPage clickSupplyDropbox() {
        getSupplyDropbox().click();
        return this;
    }

    /**
     * Selects a supply option by index.
     */
    public GuestSuppliesPage selectSupplyOption(int index) {
        selectedSupply = getSupplyOptions().get(index).getText();
        getSupplyOptions().get(index).click();
        return this;
    }

    /**
     * Checks if supply options are visible.
     */
    public boolean isSupplyOptionsDisplayed() {
        return getSupplyOptions().getFirst().getCssValue("opacity").equals("1");
    }

    /**
     * Enters a quantity in the input field.
     */
    public GuestSuppliesPage insertQuantity(String quantity) {
        getQuantityTextBox().sendKeys(quantity);
        return this;
    }

    /**
     * Opens the unit types multi-select dropdown.
     */
    public GuestSuppliesPage clickUnitTypes() {
        getUnitTypesMultiSelector().click();
        return this;
    }

    /**
     * Selects a unit type option by index.
     */
    public GuestSuppliesPage selectSingleUnitTypesOption(int index) {
        selectedUnitTypes = getUnitTypeOptionLabel(getUnitTypesOptions().get(index)).getText();
        getUnitTypesOptions().get(index).click();
        return this;
    }

    /**
     * Checks if unit type options are visible.
     */
    public boolean isUnitTypesOptionsDisplayed() {
        return getUnitTypesOptions().getFirst().getCssValue("opacity").equals("1");
    }

    /**
     * Opens the unit number multi-select dropdown.
     */
    public GuestSuppliesPage clickUnitNumber() {
        getUnitNumberMultiSelector().click();
        return this;
    }

    /**
     * Selects a unit number option by index.
     */
    public GuestSuppliesPage selectSingleUnitNumberOption(int index) {
        selectedUnitNumbers = getUnitNumberOptionLabel(getUnitNumberOptions().get(index)).getText();
        getUnitNumberOptions().get(index).click();
        return this;
    }

    /**
     * Clicks the append button to add the supply row.
     */
    public void clickAppendButton() {
        getAppendButtonBy().click();
    }

    /**
     * Returns the count of added supplies in the table.
     */
    public int countOfAddedSupplies() {
        return getAddedSuppliesTable().size();
    }

    /**
     * Clicks the "Create Accomplished Order" button.
     */
    public GuestSuppliesPage clickCreateAccomplishedButton() {
        getCreateAccomplishedButton().click();
        return this;
    }

    /**
     * Gets the quantity value of a specific supply row by index.
     */
    public String quantityPerUnitValueByIndex(int index) {
        return getQuantityPerUnitValuesRows().get(index).getText();
    }

    /**
     * Inserts a comment into the comment text area.
     */
    public GuestSuppliesPage insertComment(String comment) {
        getCommentTextBox().sendKeys(comment);
        return this;
    }

    /**
     * Returns the length of the comment in the textarea.
     */
    public int getCommentLength() {
        return Objects.requireNonNull(getCommentTextBox().getAttribute("value")).length();
    }

    /**
     * Returns the count of clear buttons shown in the form.
     */
    public int getClearButtonsCount() {
        return getClearButtons().size();
    }

    /**
     * Opens the dropdown and clicks the "Create Proposed Order" option.
     */
    public GuestSuppliesPage clickCreateProposedOrderButton() {
        getOrderDropButton().click();
        getProposedOrderButton().click();
        return this;
    }

    /**
     * Clicks the edit button for an appended supply row by index.
     */
    public GuestSuppliesPage editAppendedSupplyByIndex(int index) {
        getEditAppendedOrderButtons().get(index).click();
        return this;
    }

    /**
     * Clicks the delete button for an appended supply row by index.
     */
    public GuestSuppliesPage deleteAppendedSupplyByIndex(int index) {
        getDeleteAppendedOrderButtons().get(index).click();
        return this;
    }

    /**
     * Clicks the discard button to cancel the entire supply order.
     */
    public GuestSuppliesPage clickDiscardButton() {
        getDiscardButton().click();
        return this;
    }

    /**
     * Clicks the cancel button to cancel the entire supply order item.
     */
    public GuestSuppliesPage clickCancelButton() {
        getCancelButton().click();
        return this;
    }

    public String getSelectedUnitNumbers() {
        return selectedUnitNumbers;
    }

    public String getSelectedUnitTypes() {
        return selectedUnitTypes;
    }

    public String getSelectedSupply() {
        return selectedSupply;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }
}
