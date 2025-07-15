package nazeel.pages.routine_menus.guest_supplies;

import nazeel.data_types.Order;
import nazeel.data_types.Order.OrderStatus;
import org.openqa.selenium.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static nazeel.base.TestBase.getRootDriver;

public class SuppliesOrderPage {
    public final static String URL = "https://staging.nazeel.net:9002/guest-supplies/supplies-order";

    // -------------------- Locators --------------------
    // Table and action locators
    private final By suppliesRowsBy = By.cssSelector("tbody>tr");
    private final By suppliesColumnsBy = By.cssSelector("td[role='gridcell']");
    private final By suppliesItemsRowsBy = By.cssSelector(".k-detail-cell>div>div>kendo-grid>div>kendo-grid-list>div>div>table>tbody>tr");
    private final By suppliesItemsColumnsBy = By.cssSelector("td");
    private final By viewButtonsBy = By.cssSelector("div.n-table-actions>button");
    private final By buttonsPanelBy = By.cssSelector("div.n-table-actions");
    private final By editButtonBy = By.cssSelector("div>button");
    private final By moreOptionsButtonBy = By.cssSelector(".n-table-actions>div:nth-child(3)");
    private final By printButtonBy = By.cssSelector("kendo-popup>div>div>div:nth-child(1)");
    private final By cancelButtonBy = By.className("popup__item--red");
    private final By yesCancelButtonBy = By.className("sweet-alert__button--danger-border");
    private final By suppliesOrderButtonsBy = By.cssSelector(".n-table__top-btns > button");
    private final By expandButtonsBy = By.className("k-plus");
    private final By collapseButtonsBy = By.className("k-minus");

    // -------------------- Element Accessors --------------------

    private List<WebElement> getSuppliesRows() {
        return getRootDriver().findElements(suppliesRowsBy);
    }

    private List<WebElement> getSuppliesCells(WebElement supplyRow) {
        return supplyRow.findElements(suppliesColumnsBy);
    }

    private List<WebElement> getSuppliesItemsRows() {
        return getRootDriver().findElements(suppliesItemsRowsBy);
    }

    private List<WebElement> getSuppliesItemsCells(WebElement supplyItemRow) {
        return supplyItemRow.findElements(suppliesItemsColumnsBy);
    }

    private List<WebElement> getViewButtons() {
        return getRootDriver().findElements(viewButtonsBy);
    }

    private List<WebElement> getButtonsPanels() {
        return getRootDriver().findElements(buttonsPanelBy);
    }

    private WebElement getEditButtonFromPanelByOrderIndex(int index) {
        return getButtonsPanels().get(index).findElement(editButtonBy);
    }

    private List<WebElement> getMoreOptionsButtons() {
        return getRootDriver().findElements(moreOptionsButtonBy);
    }

    private WebElement getPrintButton() {
        return getRootDriver().findElement(printButtonBy);
    }

    private WebElement getCancelButton() {
        return getRootDriver().findElement(cancelButtonBy);
    }

    private WebElement getYesCancelButton() {
        return getRootDriver().findElement(yesCancelButtonBy);
    }

    private WebElement getFilterButton() {
        return getRootDriver().findElements(suppliesOrderButtonsBy).get(1);
    }

    private WebElement getCheckListButton() {
        return getRootDriver().findElements(suppliesOrderButtonsBy).getFirst();
    }

    private List<WebElement> getExpandButtons() {
        return getRootDriver().findElements(expandButtonsBy);
    }

    private List<WebElement> getCollapseButtons() {
        return getRootDriver().findElements(collapseButtonsBy);
    }

    // -------------------- Public Actions --------------------

    /**
     * Returns a list of text values for a specific column in the supplies table.
     */
    public List<String> getSuppliesValues(SuppliesCells column) {
        List<String> requestedValues = new ArrayList<>();
        for (WebElement row : getSuppliesRows()) {
            List<WebElement> cellsList = getSuppliesCells(row);
            requestedValues.add(cellsList.get(column.getIndex()).getText());
        }
        return requestedValues;
    }

    /**
     * Parses all orders listed in the supplies table into a list of Order objects.
     */
    public List<Order> getRequestedOrders() {
        List<Order> requestedOrders = new ArrayList<>();
        for (WebElement row : getSuppliesRows()) {
            List<WebElement> cellsList = getSuppliesCells(row);
            requestedOrders.add(
                    new Order()
                            .setOrderNo(cellsList.get(SuppliesCells.ORDER_NO.getIndex()).getText())
                            .setStatus(OrderStatus.stringToStatus(cellsList.get(SuppliesCells.STATUS.getIndex()).getText()))
                            .setCreatedBy(cellsList.get(SuppliesCells.CREATED_BY.getIndex()).getText())
                            .setCreatedDateTime(cellsList.get(SuppliesCells.DATE_TIME.getIndex()).getText())
            );
        }
        return requestedOrders;
    }

    /**
     * Finds and returns a specific order by its order number.
     */
    public Order getOrderByOrderNo(String orderNo) {
        return getRequestedOrders()
                .stream()
                .filter(order -> order.getOrderNo().equals(orderNo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the index of the first order matching the given status.
     */
    public int getOrderIndexByStatus(OrderStatus status) {
        List<Order> orders = getRequestedOrders();
        for (int index = 0; index < orders.size(); index++) {
            if (orders.get(index).getStatus().equals(status)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Gets an order by its index in the supplies table.
     */
    public Order getOrderByIndex(int index) {
        return getRequestedOrders().get(index);
    }

    /**
     * Finds an order using the formatted creation date/time string.
     */
    public Order getOrderByCreatedDateTime(String dateTimeText) {
        return getRequestedOrders()
                .stream()
                .filter(order -> order.getCreatedDateTimeAsString().equals(dateTimeText))
                .findFirst()
                .orElse(null);
    }

    /**
     * Clicks the "View" button of a specific order row.
     */
    public SuppliesOrderPage clickViewButtonByIndex(int index) {
        getViewButtons().get(index).click();
        return this;
    }

    /**
     * Clicks the "Edit" button of a specific order row.
     */
    public SuppliesOrderPage clickEditButtonByOrderIndex(int index) {
        getEditButtonFromPanelByOrderIndex(index).click();
        return this;
    }

    /**
     * Checks whether the edit button is visible for a specific order index.
     */
    public boolean isEditButtonDisplayedByOrderIndex(int index) {
        try {
            return getEditButtonFromPanelByOrderIndex(index).isDisplayed()
                    && getEditButtonFromPanelByOrderIndex(index).getCssValue("opacity").equals("1");
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Expands a collapsible row to show supply items.
     */
    public SuppliesOrderPage clickExpandButtonByIndex(int index) {
        getExpandButtons().get(index).click();
        return this;
    }

    /**
     * Collapses a previously expanded row.
     */
    public SuppliesOrderPage clickCollapseButtonByIndex(int index) {
        getCollapseButtons().get(index).click();
        return this;
    }

    /**
     * Returns a list of all appended supply items (OrderItem) under a selected order row.
     */
    public List<Order.OrderItem> getRequestedItems() {
        List<Order.OrderItem> requestedItems = new ArrayList<>();
        for (WebElement row : getSuppliesItemsRows()) {
            List<WebElement> cellsList = getSuppliesItemsCells(row);
            requestedItems.add(
                    new Order.OrderItem()
                            .setCategory(cellsList.get(SuppliesItemsCells.CATEGORY.getIndex()).getText())
                            .setSupply(cellsList.get(SuppliesItemsCells.SUPPLY.getIndex()).getText())
                            .setQuantityPerUnit(Integer.parseInt(cellsList.get(SuppliesItemsCells.QUANTITY_PER_UNIT.getIndex()).getText()))
                            .setTotal(Integer.parseInt(cellsList.get(SuppliesItemsCells.TOTAL.getIndex()).getText()))
                            .setUnitsNumbers(List.of(cellsList.get(SuppliesItemsCells.UNITS.getIndex()).getText()))
            );
        }
        return requestedItems;
    }

    /**
     * Opens the "More Options" dropdown of a specific order row.
     */
    public SuppliesOrderPage clickMoreOptionsButtonByIndex(int index) {
        getMoreOptionsButtons().get(index).click();
        return this;
    }

    /**
     * Clicks the "Print" button in the dropdown.
     */
    public SuppliesOrderPage clickPrint() {
        getPrintButton().click();
        return this;
    }

    /**
     * Clicks the red "Cancel" button to begin order cancellation.
     */
    public SuppliesOrderPage clickCancel() {
        getCancelButton().click();
        return this;
    }

    /**
     * Returns true if the print button is currently visible on screen.
     */
    public boolean isPrintButtonDisplayed() {
        try {
            return getPrintButton().isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Returns true if the cancel button is currently visible on screen.
     */
    public boolean isCancelButtonDisplayed() {
        try {
            return getCancelButton().isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Returns true if the "Yes, Cancel" confirmation button is shown.
     */
    public boolean isYesCancelButtonDisplayed() {
        try {
            return getYesCancelButton().isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Clicks the confirmation button to proceed with order cancellation.
     */
    public SuppliesOrderPage clickYesCancelButton() {
        getYesCancelButton().click();
        return this;
    }

    /**
     * Clicks the "Filter" button in the header.
     */
    public SuppliesOrderPage clickFilterButton() {
        getFilterButton().click();
        return this;
    }

    /**
     * Clicks the "Checklist" print button in the header.
     */
    public SuppliesOrderPage clickCheckListButton() {
        getCheckListButton().click();
        return this;
    }

    // Enum representing the columns of the main Supplies Orders table
    public enum SuppliesCells {
        ORDER_NO(0),       // Column index for Order Number
        STATUS(1),         // Column index for Order Status (e.g., Proposed, Accomplished)
        CATEGORIES(2),     // Column index for Supply Category
        ITEMS(3),          // Column index for Items Count/Details
        CREATED_BY(4),     // Column index for the user who created the order
        DATE_TIME(5),      // Column index for the creation date/time
        UNIT_TYPES(6),     // Column index for unit types involved
        UNITS(7);          // Column index for unit numbers

        private final int index;

        SuppliesCells(int index) {
            this.index = index;
        }

        /**
         * Gets the column index associated with the enum.
         */
        public int getIndex() {
            return index;
        }
    }

    // Enum representing the columns of the nested Supplies Items table shown after expansion
    public enum SuppliesItemsCells {
        CATEGORY(0),            // Column index for Item Category (e.g., النظافة)
        SUPPLY(1),              // Column index for specific supply (e.g., صابون)
        QUANTITY_PER_UNIT(2),   // Column index for how many units per item
        TOTAL(3),               // Column index for total count
        UNITS(4);               // Column index for unit numbers affected

        private final int index;

        SuppliesItemsCells(int index) {
            this.index = index;
        }

        /**
         * Returns the column index within the item details table.
         */
        public int getIndex() {
            return index;
        }
    }

    // Page Object class for handling the Filters Sidebar in Supplies Order page
    public static class FiltersPage {

        // -------------------- Locators --------------------
        private final By dropdownButtonBy = By.className("k-select");  // Generic dropdown toggle
        private final By orderNoInputBy = By.id("order-number");       // Input for order number
        private final By searchButtonBy = By.cssSelector("button.button--primary"); // Search button
        private final By optionsBy = By.cssSelector("div>ul>li[role='option']");    // Dropdown option list
        private final By calendarFromInputBy = By.cssSelector("input[placeholder='From']"); // Calendar input (from)
        private final By calendatToInputBy = By.cssSelector("input[placeholder='To']");     // Calendar input (to)

        // -------------------- State Tracking --------------------
        private String selectedStatus, selectedCategory, selectedSupply, selectedUnitTypes, selectedUser;

        // -------------------- UI Accessors --------------------
        private WebElement getStatusDropdown() {
            return getRootDriver().findElements(dropdownButtonBy).get(FilterCellsDropdown.STATUS.getIndex());
        }

        private WebElement getCategoryDropdown() {
            return getRootDriver().findElements(dropdownButtonBy).get(FilterCellsDropdown.CATEGORY_NAME.getIndex());
        }

        private WebElement getSupplyDropdown() {
            return getRootDriver().findElements(dropdownButtonBy).get(FilterCellsDropdown.SUPPLY_NAME.getIndex());
        }

        private WebElement getUnitTypesDropdown() {
            return getRootDriver().findElements(dropdownButtonBy).get(FilterCellsDropdown.UNIT_TYPES.getIndex());
        }

        private WebElement getOrderNoInput() {
            return getRootDriver().findElement(orderNoInputBy);
        }

        private WebElement getUserDropdown() {
            return getRootDriver().findElements(dropdownButtonBy).get(FilterCellsDropdown.USER.getIndex());
        }

        private WebElement getCalenderFromInput() {
            return getRootDriver().findElement(calendarFromInputBy);
        }

        private WebElement getCalenderToInput() {
            return getRootDriver().findElement(calendatToInputBy);
        }

        private WebElement getSearchButton() {
            return getRootDriver().findElement(searchButtonBy);
        }

        private List<WebElement> getDropdownOptions() {
            return getRootDriver().findElements(optionsBy);
        }

        // -------------------- Filter Interactions --------------------

        /**
         * Checks if the dropdown options are shown in the DOM.
         */
        public boolean isOptionsShown() {
            try {
                List<WebElement> options = getDropdownOptions();
                return options.stream().anyMatch(WebElement::isDisplayed);
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        public FiltersPage clickStatusDropdown() {
            getStatusDropdown().click();
            return this;
        }

        public FiltersPage selectStatusOption(int index) {
            WebElement option = getDropdownOptions().get(index);
            selectedStatus = option.getText();
            option.click();
            return this;
        }

        public FiltersPage clickCategoryDropdown() {
            getCategoryDropdown().click();
            return this;
        }

        public FiltersPage selectCategoryOption(int index) {
            WebElement option = getDropdownOptions().get(index);
            selectedCategory = option.getText();
            option.click();
            return this;
        }

        public FiltersPage clickSupplyDropdown() {
            getSupplyDropdown().click();
            return this;
        }

        public FiltersPage selectSupplyOption(int index) {
            WebElement option = getDropdownOptions().get(index);
            selectedSupply = option.getText();
            option.click();
            return this;
        }

        public FiltersPage clickUnitTypesDropdown() {
            getUnitTypesDropdown().click();
            return this;
        }

        public FiltersPage selectUnitTypesOption(int index) {
            WebElement option = getDropdownOptions().get(index);
            selectedUnitTypes = option.getText();
            option.click();
            return this;
        }

        public FiltersPage insertOrderNo(String orderNo) {
            getOrderNoInput().sendKeys(orderNo);
            return this;
        }

        public FiltersPage clickUserDropdown() {
            getUserDropdown().click();
            return this;
        }

        public FiltersPage selectUserOption(int index) {
            WebElement option = getDropdownOptions().get(index);
            selectedUser = option.getText();
            option.click();
            return this;
        }

        public FiltersPage clickSearchButton() {
            getSearchButton().click();
            return this;
        }

        public FiltersPage clickCalenderFromInput() {
            getCalenderFromInput().click();
            return this;
        }

        public FiltersPage clickCalenderToInput() {
            getCalenderToInput().click();
            return this;
        }

        // -------------------- Selected Option Getters --------------------

        public String getSelectedStatus() {
            return selectedStatus;
        }

        public String getSelectedCategory() {
            return selectedCategory;
        }

        public String getSelectedSupply() {
            return selectedSupply;
        }

        public String getSelectedUnitTypes() {
            return selectedUnitTypes;
        }

        public String getSelectedUser() {
            return selectedUser;
        }

        // Enum to map dropdown index to logical field
        private enum FilterCellsDropdown {
            STATUS(0),
            CATEGORY_NAME(1),
            SUPPLY_NAME(2),
            UNIT_TYPES(3),
            USER(4),
            DATE_TIME_FROM(5),
            DATE_TIME_TO(6);

            private final int index;

            FilterCellsDropdown(int index) {
                this.index = index;
            }

            public int getIndex() {
                return index;
            }
        }
    }

    public static class ViewPage {

        // -------------------- Locators --------------------

        private final By titlesBy = By.cssSelector("div.col-md-2"); // List of label containers (e.g., Order No, Status, Date)
        private final By statusSpanBy = By.cssSelector("span"); // Span inside the status container that holds the actual status
        private final By discardButtonBy = By.cssSelector(".n-button.n-button--danger-border.margin-inline"); // Button to discard the order
        private final By printButtonBy = By.cssSelector("button.ng-star-inserted"); // Print button (specific to Angular rendering)

        // -------------------- Element Accessors --------------------

        /**
         * Returns the WebElement containing the Order No label and value.
         * Assumes this is the first element in the list.
         */
        private WebElement getOrderNoLabel() {
            return getRootDriver().findElements(titlesBy).getFirst();
        }

        /**
         * Returns the WebElement containing the Order Status value.
         * This is the second element in the list of labels.
         */
        private WebElement getOrderStatusLabel() {
            return getRootDriver().findElements(titlesBy).get(1).findElement(statusSpanBy);
        }

        /**
         * Returns the WebElement containing the date and time information.
         * This is the third element in the titlesBy collection.
         */
        private WebElement getOrderDateAndTimeLabel() {
            return getRootDriver().findElements(titlesBy).get(2);
        }

        /**
         * Returns the discard button used to cancel the order.
         */
        private WebElement getDiscardButton() {
            return getRootDriver().findElement(discardButtonBy);
        }

        /**
         * Returns the print button WebElement.
         */
        private WebElement getPrintButton() {
            return getRootDriver().findElement(printButtonBy);
        }

        // -------------------- Actions --------------------

        /**
         * Retrieves the order number text from the UI.
         * The DOM structure typically places a label (e.g., "Order No") and the number together,
         * so this method splits the text by line and returns the second line.
         *
         * @return the extracted order number as a string
         */
        public String getOrderNo() {
            String fullText = getOrderNoLabel().getText().trim();
            String[] lines = fullText.split("\\R"); // "\\R" matches all line breaks
            return lines.length > 1 ? lines[1].trim() : ""; // Return the second line which contains the number
        }

        /**
         * Retrieves the order status from the UI and converts it into an enum.
         *
         * @return the parsed OrderStatus enum value
         */
        public OrderStatus getOrderStatus() {
            return OrderStatus.stringToStatus(getOrderStatusLabel().getText().trim());
        }

        /**
         * Parses the order's date and time from the UI label.
         * Expected format: "Issue Date/Time\n16/04/2025 02:27 PM"
         *
         * @return the parsed LocalDateTime object
         */
        public LocalDateTime getOrderDateAndTime() {
            String rawText = getOrderDateAndTimeLabel().getText().trim();
            String[] lines = rawText.split("\\n");

            if (lines.length < 2) {
                throw new RuntimeException("Invalid label format: " + rawText);
            }

            String dateText = lines[1].trim(); // Extracts date-time text (e.g., "16/04/2025 02:27 PM")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH);

            try {
                return LocalDateTime.parse(dateText, formatter);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid date format: " + dateText, e);
            }
        }

        /**
         * Clicks the discard button to remove or cancel the order.
         *
         * @return current ViewPage instance (fluent interface)
         */
        public ViewPage clickDiscardButton() {
            getDiscardButton().click();
            return this;
        }

        /**
         * Clicks the print button to initiate order printing.
         *
         * @return current ViewPage instance (fluent interface)
         */
        public ViewPage clickPrintButton() {
            getPrintButton().click();
            return this;
        }
    }
}
