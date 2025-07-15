package nazeel.test_suites.routine_menus.guest_supplies;

import nazeel.base.CalendarPicker;
import nazeel.base.PageBase;
import nazeel.base.TestBase;
import nazeel.data_types.Order;
import nazeel.listeners.TestListener;
import nazeel.pages.routine_menus.guest_supplies.SuppliesOrderPage;
import nazeel.utils.ScrollUtil;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Listeners(TestListener.class)
public class SuppliesOrderTS extends TestBase {
    // Page Objects used for viewing, interacting, and filtering orders
    private final SuppliesOrderPage.ViewPage suppliesViewPage = new SuppliesOrderPage.ViewPage(); // Handles order view page details
    private final SuppliesOrderPage suppliesOrderPage = new SuppliesOrderPage(); // Handles main order table page interactions
    private final PageBase basePage = new PageBase(); // Generic utility page methods (toast messages, iframe switch, etc.)
    private final SuppliesOrderPage.FiltersPage filtersPage = new SuppliesOrderPage.FiltersPage(); // Handles filter interactions on order page

    /**
     * Compares two Order objects field-by-field, including status, timestamp, and each item’s attributes.
     * Fails if any mismatch is detected, and prints expected vs actual for debugging.
     */
    public static void assertOrdersEqual(Order expectedOrder, Order actualOrder) {
        SoftAssert ordersAssert = new SoftAssert(); // Allows multiple soft assertions

        // Compare order status (e.g. ACCOMPLISHED, PROPOSED, CANCELLED)
        ordersAssert.assertEquals(
                actualOrder.getStatus(),
                expectedOrder.getStatus(),
                "Order statuses do not match."
        );

        // Compare creation time ignoring seconds and milliseconds
        ordersAssert.assertEquals(
                actualOrder.getCreatedDateTime().withSecond(0).withNano(0),
                expectedOrder.getCreatedDateTime().withSecond(0).withNano(0),
                "Order creation timestamps do not match."
        );

        // Compare count of items in each order
        List<Order.OrderItem> expectedItems = expectedOrder.getItems();
        List<Order.OrderItem> actualItems = actualOrder.getItems();
        ordersAssert.assertEquals(
                actualItems.size(),
                expectedItems.size(),
                "Order items size mismatch."
        );

        // Loop through items and compare field-by-field
        int minSize = Math.min(expectedItems.size(), actualItems.size());
        for (int i = 0; i < minSize; i++) {
            Order.OrderItem expectedItem = expectedItems.get(i);
            Order.OrderItem actualItem = actualItems.get(i);

            ordersAssert.assertEquals(actualItem.getCategory(), expectedItem.getCategory(), "Mismatch in item category at index " + i);
            ordersAssert.assertEquals(actualItem.getSupply(), expectedItem.getSupply(), "Mismatch in item supply at index " + i);
            ordersAssert.assertEquals(actualItem.getQuantityPerUnit(), expectedItem.getQuantityPerUnit(), "Mismatch in item quantity per unit at index " + i);
            ordersAssert.assertEquals(actualItem.getUnitTypes(), expectedItem.getUnitTypes(), "Mismatch in item unit types at index " + i);
            ordersAssert.assertEquals(actualItem.getUnitsNumbers(), expectedItem.getUnitsNumbers(), "Mismatch in item units numbers at index " + i);
            ordersAssert.assertEquals(actualItem.getTotal(), expectedItem.getTotal(), "Mismatch in item total at index " + i);
        }

        // Collect and throw all assertion failures together
        try {
            ordersAssert.assertAll();
        } catch (AssertionError assertionError) {
            // Include formatted expected vs actual content for easier debugging
            throw new AssertionError(
                    """
                            Discarded Proposed order doesn't match the table version.
                            Expected:
                            %s
                            Actual:
                            %s
                            """.formatted(expectedOrder, actualOrder),
                    assertionError
            );
        }
    }

    /**
     * Validates key overview fields from the table: status, createdBy, and timestamp.
     * This does not validate full item list, only the main row fields.
     */
    public static void assertOrdersOverviewEqual(Order expectedOrder, Order actualOrder) {
        SoftAssert ordersOverviewAssert = new SoftAssert();

        // Null checks before further comparison
        ordersOverviewAssert.assertNotNull(expectedOrder, "Expected order is null.");
        ordersOverviewAssert.assertNotNull(actualOrder, "Actual order is null.");

        // Check status match
        ordersOverviewAssert.assertEquals(actualOrder.getStatus(), expectedOrder.getStatus(), "Order statuses do not match in overview.");

        // Check createdBy match (partial match allowed due to potential formatting difference)
        ordersOverviewAssert.assertTrue(
                actualOrder.getCreatedBy().contains(expectedOrder.getCreatedBy()) ||
                        expectedOrder.getCreatedBy().contains(actualOrder.getCreatedBy()),
                "Order created by do not match in overview."
        );

        // Check timestamp match ignoring seconds and nanoseconds
        ordersOverviewAssert.assertEquals(
                actualOrder.getCreatedDateTime().withSecond(0).withNano(0),
                expectedOrder.getCreatedDateTime().withSecond(0).withNano(0),
                "Order creation timestamps do not match in overview."
        );

        // Throw if any mismatch
        try {
            ordersOverviewAssert.assertAll();
        } catch (AssertionError assertionError) {
            throw new AssertionError(
                    """
                            Discarded order doesn't match the overview table version.
                            Expected:
                            %s
                            Actual:
                            %s
                            """.formatted(expectedOrder, actualOrder),
                    assertionError
            );
        }
    }

    /**
     * Gathers all visible order details from the view page and builds a complete Order object.
     */
    private Order getRequestedOrderDetails() {
        return new Order(
                suppliesViewPage.getOrderNo(),               // Extract Order No from UI
                suppliesViewPage.getOrderStatus(),           // Extract Order Status (ACCOMPLISHED, etc.)
                null,                                         // CreatedBy not available here
                suppliesViewPage.getOrderDateAndTime(),      // Extract timestamp
                basePage.getRequestedItems(),                // Get attached order items
                getRootDriver().getCurrentUrl()              // Current URL (used later for navigation)
        );
    }

    /**
     * Opens the supplies order page and clicks on the filter button to load filters UI.
     */
    private void openAndClickFilters() {
        navigateToSuppliesOrderPageURL();       // Navigate to page
        suppliesOrderPage.clickFilterButton();  // Click filter icon
    }

    /**
     * Waits until the filter dropdown options appear.
     * If timeout happens before appearance, the test fails.
     */
    private void waitTillOptionsShown() {
        try {
            explicitWait(Waits.WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> filtersPage.isOptionsShown());
        } catch (TimeoutException exception) {
            Assert.fail("Options didn't show!");
        }
    }

    /**
     * Enters a date and time into the calendar picker and returns the LocalDateTime result.
     * Used for filtering by From/To dates.
     *
     * @param isDateFrom true for 'From', false for 'To'
     * @param day        e.g., "25"
     * @param month      e.g., Month.APRIL
     * @param year       e.g., "2025"
     * @param hour       e.g., "02"
     * @param minute     e.g., "30"
     * @param amPm       "AM" or "PM"
     */
    public LocalDateTime enterDateTime(boolean isDateFrom, String day, Month month, String year, String hour, String minute, String amPm) {
        CalendarPicker picker = new CalendarPicker();

        // Click the relevant date input
        if (isDateFrom)
            filtersPage.clickCalenderFromInput();
        else
            filtersPage.clickCalenderToInput();

        // Use calendar picker to input date & time
        picker.enterDate(day, month, year).enterTime(hour, minute, amPm);

        // Format string to match expected pattern and parse
        String dateTimeString = "%s/%02d/%s %s:%s %s".formatted(day, month.getValue(), year, hour, minute, amPm.toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH);

        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Navigates the WebDriver to the main supplies order URL and waits until the page is fully loaded.
     */
    private static void navigateToSuppliesOrderPageURL() {
        getRootDriver().get(SuppliesOrderPage.URL);             // Load URL
        waitForPageToLoad(SuppliesOrderPage.URL);               // Ensure page finished loading
    }

    /**
     * Handles the end-to-end print process for a given order status.
     * It checks if the print button works, and ensures the iframe result completes successfully.
     */
    private void verifyPrintProcess(Order.OrderStatus orderStatus, String noOrderFoundMessage, String noPrintFoundMessage, String printProcessFailedMessage) {
        navigateToSuppliesOrderPageURL(); // Navigate to page

        // Find the index of first order with desired status
        int index = suppliesOrderPage.getOrderIndexByStatus(orderStatus);
        Assert.assertTrue(index >= 0, noOrderFoundMessage);

        // Open print modal
        suppliesOrderPage.clickMoreOptionsButtonByIndex(index).clickPrint();
        basePage.switchToPrintIframe(); // Switch to iframe context

        // Wait for print dialog to display
        try {
            explicitWait(Waits.WAIT_LONG_UNTIL_DISPLAYED.getSeconds())
                    .until(driver -> basePage.isPrintErrorMessageDisplayed());
        } catch (TimeoutException exception) {
            Assert.fail(noPrintFoundMessage);
        }

        // Wait for message to say "Done"
        try {
            explicitWait(Waits.WAIT_TILL_IT_READY.getSeconds()).until(driver ->
                    basePage.getPrintErrorMessageText().contains("Done")
            );
        } catch (TimeoutException e) {
            Assert.fail(printProcessFailedMessage);
        }
    }

    /**
     * Verifies that clicking 'View' for a certain status order navigates to the correct view page.
     */
    private void verifyViewProcess(Order.OrderStatus orderStatus, String noOrderFoundMessage, String viewProcessFailedMessage) {
        navigateToSuppliesOrderPageURL(); // Open supplies page

        // Find index of the order with given status
        int index = suppliesOrderPage.getOrderIndexByStatus(orderStatus);
        Assert.assertTrue(index >= 0, noOrderFoundMessage);

        // Click view button
        suppliesOrderPage.clickViewButtonByIndex(index);

        // Ensure it redirected to /view page
        Assert.assertTrue(
                Objects.requireNonNull(getRootDriver().getCurrentUrl()).contains("/view"),
                viewProcessFailedMessage
        );
    }

    /**
     * Verifies that the edit button is not visible for an order (e.g., accomplished or canceled).
     */
    private void verifyEditButtonNotDisplayed(Order.OrderStatus orderStatus, String noOrderFoundMessage, String buttonExistFailMessage) {
        navigateToSuppliesOrderPageURL(); // Navigate to orders page

        // Find index of the order with that status
        int index = suppliesOrderPage.getOrderIndexByStatus(orderStatus);
        Assert.assertTrue(index >= 0, noOrderFoundMessage);

        // Verify the edit button is NOT shown for this order
        Assert.assertTrue(
                suppliesOrderPage.isEditButtonDisplayedByOrderIndex(index),
                buttonExistFailMessage
        );
    }

    /**
     * TC01 - Verifies that the Accomplished order details in the view page
     * match the expected order that was submitted from the guest supplies page.
     */
    @Test(testName = "TC01 - Compare Accomplished Order with Sent Order from guest supplies page", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc01CreateValidAccomplishedOrder"})
    public void tc01CompareAccomplishedOrderDetails() {
        // Retrieve the accomplished order stored during creation
        Order expectedOrder = GuestSuppliesTS.getRequestedAccomplishedOrder();

        // Navigate directly to the view page of the expected order
        getRootDriver().get(expectedOrder.getUrl());
        waitForPageToLoad(expectedOrder.getUrl());

        // Get actual order details from the view page
        Order actualOrder = getRequestedOrderDetails();

        // Assert full match between expected and actual
        assertOrdersEqual(expectedOrder, actualOrder);
    }

    /**
     * TC02 - Compares Proposed order details in the view page against what was submitted.
     */
    @Test(testName = "TC02 - Compare Proposed Order with Sent Order from guest supplies page", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc14CreateValidProposedOrder"})
    public void tc02CompareProposedOrderDetails() {
        // Get the expected proposed order
        Order expectedOrder = GuestSuppliesTS.getRequestedProposedOrder();

        // Open the view page for the order
        getRootDriver().get(expectedOrder.getUrl());
        waitForPageToLoad(expectedOrder.getUrl());

        // Read the order details from the page
        Order actualOrder = getRequestedOrderDetails();

        // Assert full match
        assertOrdersEqual(expectedOrder, actualOrder);
    }

    /**
     * TC03 - Discards the Accomplished order and verifies it appears correctly in the orders table overview.
     */
    @Test(testName = "TC03 - Discard and verify Accomplished order exists in Supplies Orders table", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc01CreateValidAccomplishedOrder"})
    public void tc03VerifyAccomplishedOrderInListOfOrders() {
        // Retrieve expected accomplished order
        Order expectedOrder = GuestSuppliesTS.getRequestedAccomplishedOrder();

        // Open its view page and discard it
        getRootDriver().get(expectedOrder.getUrl());
        suppliesViewPage.clickDiscardButton();

        // Confirm redirection to supplies order list
        Assert.assertEquals(getRootDriver().getCurrentUrl(), SuppliesOrderPage.URL);

        // Fetch order from the table by order number
        Order actualOrder = suppliesOrderPage.getOrderByOrderNo(expectedOrder.getOrderNo());
        Assert.assertNotNull(actualOrder, "Order not found in Supplies Orders list.");

        // Compare overview fields (not full items)
        assertOrdersOverviewEqual(expectedOrder, actualOrder);
    }

    /**
     * TC04 - Discards the Proposed order and verifies it's listed in Supplies Orders table.
     */
    @Test(testName = "TC04 - Discard and verify Proposed order exists in Supplies Orders table", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc14CreateValidProposedOrder"})
    public void tc04VerifyProposedOrderInListOfOrders() {
        // Get expected proposed order
        Order expectedOrder = GuestSuppliesTS.getRequestedProposedOrder();

        // Open the view page and discard it
        getRootDriver().get(expectedOrder.getUrl());
        suppliesViewPage.clickDiscardButton();

        // Verify redirect and fetch the actual order
        Assert.assertEquals(getRootDriver().getCurrentUrl(), SuppliesOrderPage.URL);
        Order actualOrder = suppliesOrderPage.getOrderByOrderNo(expectedOrder.getOrderNo());

        // Verify order is found and correct
        Assert.assertNotNull(actualOrder, "Proposed order not found in Supplies Orders list.");
        assertOrdersOverviewEqual(expectedOrder, actualOrder);
    }

    /**
     * TC05 - Ensures that accomplished orders cannot be canceled from the UI.
     */
    @Test(testName = "TC05 - Verify that accomplished order can't be canceled", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc01CreateValidAccomplishedOrder"})
    public void tc05VerifyThatAccomplishedOrderCantBeCanceled() {
        // Navigate to orders page
        navigateToSuppliesOrderPageURL();

        // Find the index of an accomplished order
        int index = suppliesOrderPage.getOrderIndexByStatus(Order.OrderStatus.ACCOMPLISHED);

        // If found, try to open options; otherwise fail
        if (index != -1)
            suppliesOrderPage.clickMoreOptionsButtonByIndex(index);
        else
            Assert.fail("No Accomplished Order Found!");

        // Cancel button must NOT be visible
        Assert.assertFalse(suppliesOrderPage.isCancelButtonDisplayed(), "Cancel button should not be shown for accomplished order.");
    }

    /**
     * TC06 - Confirms that a Proposed order can be successfully canceled.
     */
    @Test(testName = "TC06 - Verify that Proposed order can be canceled", suiteName = "Supplies Orders",
            dependsOnMethods = {"nazeel.test_suites.routine_menus.guest_supplies.GuestSuppliesTS.tc14CreateValidProposedOrder"})
    public void tc06VerifyThatProposedOrderCanBeCanceled() {
        // Navigate to page and find proposed order
        navigateToSuppliesOrderPageURL();
        int index = suppliesOrderPage.getOrderIndexByStatus(Order.OrderStatus.PROPOSED);

        // Ensure order exists
        if (index != -1)
            suppliesOrderPage.clickMoreOptionsButtonByIndex(index);
        else
            Assert.fail("No Proposed Order Found!");

        // Perform cancellation flow
        suppliesOrderPage.clickCancel().clickYesCancelButton();

        // Wait for success toast message
        try {
            explicitWait(Waits.WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> basePage.isSuccessToastDisplayed());
        } catch (TimeoutException e) {
            Assert.fail("Expected success toast did not appear within the timeout period.");
        }

        // Confirm the status is now CANCELLED
        Assert.assertEquals(
                suppliesOrderPage.getOrderByIndex(index).getStatus(),
                Order.OrderStatus.CANCELLED,
                "Order status was not updated to CANCELLED"
        );
    }

    /**
     * TC07 - Filters the order list by status and validates that all returned records match the selected status.
     */
    @Test(testName = "TC07 - Filter by Status", suiteName = "Supplies Filters")
    public void tc07FilterByStatus() {
        openAndClickFilters();                         // Open filters
        filtersPage.clickStatusDropdown();             // Open status dropdown
        waitTillOptionsShown();                        // Wait for dropdown
        filtersPage.selectStatusOption(0).clickSearchButton(); // Select first status and search

        // Fetch all result statuses
        List<String> results = suppliesOrderPage.getSuppliesValues(SuppliesOrderPage.SuppliesCells.STATUS);
        Assert.assertFalse(results.isEmpty(), "No records found for selected status.");

        // Verify all rows match selected status
        Assert.assertTrue(results.stream().allMatch(result -> result.equalsIgnoreCase(filtersPage.getSelectedStatus())),
                "Status doesn't match the selected category.");
    }

    /**
     * TC08 - Filters by category and checks if all results belong to the selected category.
     */
    @Test(testName = "TC08 - Filter by Category", suiteName = "Supplies Filters")
    public void tc08FilterByCategory() {
        openAndClickFilters();
        filtersPage.clickCategoryDropdown();
        waitTillOptionsShown();
        filtersPage.selectCategoryOption(0).clickSearchButton();

        List<String> results = suppliesOrderPage.getSuppliesValues(SuppliesOrderPage.SuppliesCells.CATEGORIES);
        Assert.assertFalse(results.isEmpty(), "No items found for selected category.");

        // Confirm that all rows contain selected category
        Assert.assertTrue(results.stream().allMatch(result -> result.contains(filtersPage.getSelectedCategory())),
                "Results don't match the selected category.");
    }

    /**
     * TC09 - Filters by a supply name and confirms the matching item appears after expanding.
     */
    @Test(testName = "TC09 - Filter by Supply Name", suiteName = "Supplies Filters")
    public void tc09FilterBySupplyName() {
        openAndClickFilters();

        // Choose category then supply to narrow list
        filtersPage.clickCategoryDropdown();
        waitTillOptionsShown();
        filtersPage.selectCategoryOption(0);
        filtersPage.clickSupplyDropdown();
        waitTillOptionsShown();
        filtersPage.selectSupplyOption(0).clickSearchButton();

        // Attempt to expand item rows until found or list is exhausted
        while (true) {
            try {
                suppliesOrderPage.clickExpandButtonByIndex(0); // Expand row
            } catch (NoSuchElementException | IndexOutOfBoundsException e) {
                // If all rows expanded, check for the target supply
                List<Order.OrderItem> results = suppliesOrderPage.getRequestedItems();
                Assert.assertFalse(results.isEmpty(), "No items found for selected supply.");

                // One of the items must match selected supply
                Assert.assertTrue(
                        results.stream().anyMatch(item -> item.getSupply().equalsIgnoreCase(filtersPage.getSelectedSupply())),
                        "Results don't match the selected supplies."
                );

                // Try scrolling in case of lazy loading
                try {
                    ScrollUtil.scrollDownLittle();
                } catch (WebDriverException | ScrollUtil.ScrollLimitReachedException ignored) {
                    break;
                }
            }
        }
    }

    /**
     * TC10 - Filters the table by selected unit type and confirms all rows match the filter.
     */
    @Test(testName = "TC10 - Filter by Unit Types", suiteName = "Supplies Filters")
    public void tc10FilterByUnitTypes() {
        openAndClickFilters();
        filtersPage.clickUnitTypesDropdown(); // open unit type dropdown
        waitTillOptionsShown();               // wait for options
        filtersPage.selectUnitTypesOption(0).clickSearchButton(); // select and search

        // Fetch result column values
        List<String> results = suppliesOrderPage.getSuppliesValues(SuppliesOrderPage.SuppliesCells.UNIT_TYPES);
        Assert.assertFalse(results.isEmpty(), "No items found for selected unit types.");

        // Ensure all match selected value
        Assert.assertTrue(results.stream().allMatch(result -> result.equals(filtersPage.getSelectedUnitTypes())),
                "Results don't match the selected unit types.");
    }

    /**
     * TC11 - Verify filtering supplies orders by Order Number returns accurate results.
     */
    @Test(testName = "TC11 - Filter by Order No", suiteName = "Supplies Filters")
    public void tc11FilterByOrderNo() {
        // Open the supplies order page and click the filter button
        openAndClickFilters();

        // Get a random order number from the list of existing orders
        List<Order> requestedOrdersResults = suppliesOrderPage.getRequestedOrders();
        String orderNo = requestedOrdersResults.get(new Random().nextInt(0, 10)).getOrderNo();

        // Insert the order number in the filter and click search
        filtersPage.insertOrderNo(orderNo).clickSearchButton();

        // Fetch the results after filtering
        List<Order> searchedResults = suppliesOrderPage.getRequestedOrders();

        // Assert the result is not empty
        Assert.assertFalse(searchedResults.isEmpty(), "No records found for the given order number.");

        // Assert all results match the searched order number
        Assert.assertTrue(searchedResults.stream().allMatch(order -> order.getOrderNo().equalsIgnoreCase(orderNo)),
                "Results don't match the selected order number.");
    }

    /**
     * TC12 - Verify filtering supplies orders by User returns the correct records.
     */
    @Test(testName = "TC12 - Filter by User", suiteName = "Supplies Filters")
    public void tc12FilterByUser() {
        openAndClickFilters(); // Navigate to the filter form
        filtersPage.clickUserDropdown(); // Click the user dropdown
        waitTillOptionsShown(); // Wait for the dropdown options to appear

        // Select the second user from the dropdown
        int userOrderedSuppliesIndex = 1;
        filtersPage.selectUserOption(userOrderedSuppliesIndex).clickSearchButton();

        // Get the list of users shown after filtering
        List<String> results = suppliesOrderPage.getSuppliesValues(SuppliesOrderPage.SuppliesCells.CREATED_BY);
        Assert.assertFalse(results.isEmpty(), "No records found for selected user.");

        // Extract username from the format [company - username]
        String extractedName = (filtersPage.getSelectedUser().split(" - ", 2)[1]).trim();

        // Ensure all records were created by the selected user
        Assert.assertTrue(results.stream().allMatch(result -> result.equals(extractedName)),
                "Results don't match the selected user.");
    }

    /**
     * TC13 - Filter supplies orders from a specific date.
     */
    @Test(testName = "TC13 - Filter by Date From", suiteName = "Supplies Filters")
    public void tc13FilterByDateFrom() {
        openAndClickFilters(); // Open filters section
        // Set 'From' date using date picker
        LocalDateTime selectedFrom = enterDateTime(true, "25", Month.APRIL, "2025", "02", "00", "PM");
        filtersPage.clickSearchButton(); // Apply filter

        // Fetch filtered results
        List<Order> results = suppliesOrderPage.getRequestedOrders();
        Assert.assertFalse(results.isEmpty(), "No records found for the date from filter.");

        // Assert each order date is after or equal to the 'from' date
        results.forEach(order -> Assert.assertTrue(
                order.getCreatedDateTime().isAfter(selectedFrom) || order.getCreatedDateTime().isEqual(selectedFrom),
                "Order created before selected 'from' datetime: " + order.getCreatedDateTimeAsString()));
    }

    /**
     * TC14 - Filter supplies orders to a specific date.
     */
    @Test(testName = "TC14 - Filter by Date To", suiteName = "Supplies Filters")
    public void tc14FilterByDateTo() {
        openAndClickFilters(); // Open filter section
        // Set 'To' date using date picker
        LocalDateTime selectedTo = enterDateTime(false, "30", Month.APRIL, "2025", "04", "30", "PM");
        filtersPage.clickSearchButton(); // Apply filter

        // Fetch results and validate
        List<Order> results = suppliesOrderPage.getRequestedOrders();
        Assert.assertFalse(results.isEmpty(), "No records found for the date to filter.");
        results.forEach(order -> Assert.assertTrue(
                order.getCreatedDateTime().isBefore(selectedTo) || order.getCreatedDateTime().isEqual(selectedTo),
                "Order created after selected 'to' datetime: " + order.getCreatedDateTimeAsString()));
    }

    /**
     * TC15 - Filter supplies orders within a specific date range.
     */
    @Test(testName = "TC15 - Filter by Date Range (From & To)", suiteName = "Supplies Filters")
    public void tc15FilterByDateRange() {
        openAndClickFilters(); // Open filters section

        // Select both From and To dates
        LocalDateTime selectedFrom = enterDateTime(true, "25", Month.APRIL, "2025", "02", "00", "PM");
        LocalDateTime selectedTo = enterDateTime(false, "30", Month.APRIL, "2025", "04", "30", "PM");
        filtersPage.clickSearchButton();

        // Fetch and validate date ranges
        List<Order> results = suppliesOrderPage.getRequestedOrders();
        Assert.assertFalse(results.isEmpty(), "No records found for the date range filter.");

        results.forEach(order -> {
            LocalDateTime created = order.getCreatedDateTime();
            Assert.assertTrue((created.isEqual(selectedFrom) || created.isAfter(selectedFrom)) &&
                            (created.isEqual(selectedTo) || created.isBefore(selectedTo)),
                    "Order datetime %s is outside range [%s - %s]".formatted(
                            order.getCreatedDateTimeAsString(),
                            selectedFrom,
                            selectedTo
                    ));
        });
    }

    /**
     * TC16 - Apply combined filters and verify at least one record is returned.
     */
    @Test(testName = "TC16 - Filter by Combined Filters", suiteName = "Supplies Filters")
    public void tc16FilterByCombined() {
        openAndClickFilters(); // Open filter section

        // Apply all filters one by one
        filtersPage.clickStatusDropdown().selectStatusOption(0)
                .clickCategoryDropdown().selectCategoryOption(0)
                .clickSupplyDropdown().selectSupplyOption(0)
                .clickUnitTypesDropdown().selectUnitTypesOption(0)
                .clickSearchButton();

        // Assert results exist
        List<Order> results = suppliesOrderPage.getRequestedOrders();
        Assert.assertFalse(results.isEmpty(), "No records found for combined filter.");
    }

    /**
     * TC17 - Verify printing of Accomplished orders functions correctly.
     */
    @Test(testName = "TC17 - Print Accomplished Order", suiteName = "Supplies Orders")
    public void tc17PrintAccomplishedOrder() {
        verifyPrintProcess(
                Order.OrderStatus.ACCOMPLISHED,
                "No Accomplished Order Found!",
                "Print message of Accomplished order not displayed!",
                "Accomplished order print process failed!"
        );
    }

    /**
     * TC18 - Verify printing of Proposed orders functions correctly.
     */
    @Test(testName = "TC18 - Print Proposed Order", suiteName = "Supplies Orders")
    public void tc18PrintProposedOrder() {
        verifyPrintProcess(
                Order.OrderStatus.PROPOSED,
                "No Proposed Order Found!",
                "Print message of Proposed order not displayed!",
                "Proposed order print process failed!"
        );
    }

    /**
     * TC19 - Verify printing of Canceled orders functions correctly.
     * ❗ This mistakenly uses PROPOSED status instead of CANCELLED.
     */
    @Test(testName = "TC19 - Print Canceled Order", suiteName = "Supplies Orders")
    public void tc19PrintCanceledOrder() {
        verifyPrintProcess(
                Order.OrderStatus.PROPOSED,
                "No Canceled Order Found!",
                "Print message of Canceled order not displayed!",
                "Canceled order print process failed!"
        );
    }

    /**
     * TC20 - Verify that clicking 'view' on an accomplished order navigates correctly.
     */
    @Test(testName = "TC20 - View Accomplished Order", suiteName = "Supplies Orders")
    public void tc20ViewAccomplishedOrder() {
        verifyViewProcess(Order.OrderStatus.ACCOMPLISHED,
                "No Accomplished Order Found!",
                "View of accomplished order didn't navigate correctly");
    }

    /**
     * TC21 - Verify that clicking 'view' on a proposed order navigates correctly.
     */
    @Test(testName = "TC21 - View Proposed Order", suiteName = "Supplies Orders")
    public void tc21ViewProposedOrder() {
        verifyViewProcess(Order.OrderStatus.PROPOSED,
                "No Proposed Order Found!",
                "View of Proposed order didn't navigate correctly");
    }

    /**
     * TC22 - Verify that clicking 'view' on a canceled order navigates correctly.
     */
    @Test(testName = "TC22 - View Canceled Order", suiteName = "Supplies Orders")
    public void tc22ViewCanceledOrder() {
        verifyViewProcess(Order.OrderStatus.CANCELLED,
                "No canceled Order Found!",
                "View of canceled order didn't navigate correctly");
    }

    /**
     * TC23 - Verify that the edit button is NOT displayed for accomplished orders.
     */
    @Test(testName = "TC23 - Edit Not Shown for Accomplished Order", suiteName = "Supplies Orders")
    public void tc23EditNotAllowedForAccomplished() {
        verifyEditButtonNotDisplayed(Order.OrderStatus.ACCOMPLISHED,
                "No Accomplished Order Found!",
                "Edit button for accomplished orders should not be visible");
    }

    /**
     * TC24 - Verify that the edit button is NOT displayed for canceled orders.
     */
    @Test(testName = "TC24 - Edit Not Shown for Canceled Order", suiteName = "Supplies Orders")
    public void tc24EditNotAllowedForCanceled() {
        verifyEditButtonNotDisplayed(Order.OrderStatus.CANCELLED,
                "No canceled Order Found!",
                "Edit button for canceled orders should not be visible");
    }

    /**
     * TC25 - Verify that edit button on proposed order navigates to edit page.
     */
    @Test(testName = "TC25 - Edit Proposed Order Navigates", suiteName = "Supplies Orders")
    public void tc25EditProposedOrder() {
        navigateToSuppliesOrderPageURL();
        int index = suppliesOrderPage.getOrderIndexByStatus(Order.OrderStatus.PROPOSED);
        Assert.assertTrue(index >= 0, "No Proposed Order Found!");
        suppliesOrderPage.clickEditButtonByOrderIndex(index);
        Assert.assertTrue(Objects.requireNonNull(getRootDriver().getCurrentUrl()).contains("/edit"),
                "Edit did not navigate correctly");
    }

    /**
     * TC26 - Verify the orders checklist page opens and completes the print lifecycle.
     */
    @Test(testName = "TC26 - Orders Checklist opened successfully", suiteName = "Supplies Orders")
    public void tc26OrdersChecklist() {
        navigateToSuppliesOrderPageURL(); // Navigate to the page
        suppliesOrderPage.clickCheckListButton(); // Click checklist button
        basePage.switchToPrintIframe(); // Switch context to iframe

        // Wait until the print toast appears
        try {
            explicitWait(Waits.WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> basePage.isPrintErrorMessageDisplayed());
        } catch (TimeoutException exception) {
            Assert.fail("Print message checklist not displayed!");
        }

        // Wait for print message to indicate "Done"
        try {
            explicitWait(Waits.WAIT_TILL_IT_READY.getSeconds()).until(driver ->
                    basePage.getPrintErrorMessageText().contains("Done"));
        } catch (TimeoutException e) {
            Assert.fail("Checklist orders print process failed!");
        }
    }

}
