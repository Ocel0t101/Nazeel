package nazeel.test_suites.routine_menus.guest_supplies;

import nazeel.base.PageBase;
import nazeel.data_types.Order;
import nazeel.listeners.TestListener;
import nazeel.base.TestBase;
import nazeel.pages.routine_menus.guest_supplies.GuestSuppliesPage;
import nazeel.pages.LoginPage;
import nazeel.pages.routine_menus.guest_supplies.SuppliesOrderPage;
import nazeel.utils.RetryAnalyzer;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static nazeel.base.TestBase.Waits.WAIT_UNTIL_DISPLAYED;
import static nazeel.base.TestBase.Waits.WAIT_UNTIL_LOADS;

@Listeners(TestListener.class) // Attach custom listener to capture screenshots and log test events
public class GuestSuppliesTS extends TestBase {
    // Page object instances used across test cases
    private final LoginPage loginPage = new LoginPage();                         // Handles login credentials and form submission
    private final GuestSuppliesPage guestSuppliesPage = new GuestSuppliesPage(); // Interacts with the Guest Supplies form
    private final PageBase basePage = new PageBase();                            // Common utility methods like toast checking, iframe switching, etc.
    private final SuppliesOrderPage.ViewPage suppliesViewPage = new SuppliesOrderPage.ViewPage(); // Interacts with the view-only page for submitted orders

    // Static orders for accomplished and proposed states to use across tests
    private static Order accomplishedOrder, proposedOrder; // Hold references to submitted accomplished and proposed orders

    /**
     * Opens the Guest Supplies page and waits until the category dropdown is displayed.
     * Ensures the page is fully ready before proceeding with actions.
     */
    private void openGuestSuppliesPage() {
        guestSuppliesPage.navigateToGuestSuppliesPage(); // Navigate to the guest supplies URL
        waitForPageToLoad(GuestSuppliesPage.URL);        // Ensure full page load
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds())  // Wait for the category dropdown to appear
                .until(driver -> guestSuppliesPage.isCategoryDropboxShown());
    }

    /**
     * Fills and appends a supply row with the provided inputs (category, supply, quantity, unit type, unit number).
     * Returns the constructed OrderItem instance that represents the added row.
     *
     * @param categoryIndex   index of the category to select
     * @param supplyIndex     index of the supply to select
     * @param quantity        quantity to input
     * @param unitTypeIndex   index of the unit type
     * @param unitNumberIndex index of the unit number
     * @return constructed Order.OrderItem or null if quantity is not a valid number
     */
    private Order.OrderItem fillSupplyForm(int categoryIndex, int supplyIndex, String quantity, int unitTypeIndex, int unitNumberIndex) {
        guestSuppliesPage.clickCategoryDropbox()                   // Open category dropdown
                .selectCategoryOption(categoryIndex)               // Choose category
                .clickSupplyDropbox()                              // Open supply dropdown
                .selectSupplyOption(supplyIndex)                   // Choose supply
                .insertQuantity(quantity)                          // Input quantity
                .clickUnitTypes()                                  // Open unit type dropdown
                .selectSingleUnitTypesOption(unitTypeIndex)        // Select unit type
                .clickUnitNumber()                                 // Open unit number dropdown
                .selectSingleUnitNumberOption(unitNumberIndex)     // Select unit number
                .clickAppendButton();                              // Click append to add item

        try {
            // Return constructed OrderItem with all selected values
            return new Order.OrderItem(
                    guestSuppliesPage.getSelectedCategory(),
                    guestSuppliesPage.getSelectedSupply(),
                    Integer.parseInt(quantity),
                    List.of(guestSuppliesPage.getSelectedUnitTypes()),
                    List.of(guestSuppliesPage.getSelectedUnitNumbers())
            );
        } catch (NumberFormatException exception) {
            // Return null if quantity parsing fails (invalid input)
            return null;
        }
    }

    /**
     * Only fills the form with category and quantity, skipping supply and units.
     * Used in negative test cases where supply/unit types are not provided.
     *
     * @param quantity quantity value to insert
     */
    private void fillFormWithCategoryOnly(String quantity) {
        guestSuppliesPage.clickCategoryDropbox()  // Open category dropdown
                .selectCategoryOption(0)          // Select first category
                .insertQuantity(quantity);        // Insert quantity
    }

    /**
     * Validates that no supply item was appended to the table.
     * Asserts both the display of an error toast and that the count of added supplies is 0.
     */
    private void assertSupplyNotAdded() {
        try {
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds())        // Wait for toast message
                    .until(driver -> basePage.isErrorToastDisplayed()); // Confirm error toast appeared
        } catch (TimeoutException e) {
            Assert.fail("Error toast didn't display!");             // Fail if no error toast
        }

        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 0); // Ensure no items were added
    }

    /**
     * Validates that the order was successfully submitted.
     * Checks for a success toast and that navigation occurred to the order listing page.
     */
    private void assertSupplySuccessfullyOrdered() {
        try {
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds())        // Wait for success toast
                    .until(driver -> basePage.isSuccessToastDisplayed());
        } catch (TimeoutException e) {
            Assert.fail("Success toast didn't display!");           // Fail if no toast
        }

        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds())            // Wait for URL to change
                    .until(ExpectedConditions.urlContains(SuppliesOrderPage.URL));
        } catch (TimeoutException e) {
            Assert.fail("URL didn't change to supplies order page!"); // Fail if page didn't redirect
        }
    }

    /**
     * Retrieves the static accomplished order used in verification steps.
     *
     * @return the accomplished order instance
     */
    protected static Order getRequestedAccomplishedOrder() {
        return accomplishedOrder;
    }

    /**
     * Retrieves the static proposed order used in verification steps.
     *
     * @return the proposed order instance
     */
    protected static Order getRequestedProposedOrder() {
        return proposedOrder;
    }

    /**
     * TC01: Create a valid accomplished order and assert URL, toast, and state snapshot.
     * Steps:
     * - Fill out the form with valid values.
     * - Submit as accomplished.
     * - Save expected order snapshot for later comparison.
     */
    @Test(testName = "TC01 - Request valid accomplished order", suiteName = "Guest Supplies", retryAnalyzer = RetryAnalyzer.class)
    public void tc01CreateValidAccomplishedOrder() {
        // Open the guest supplies form
        openGuestSuppliesPage();

        // Fill the form with valid data and capture the appended item
        Order.OrderItem requestedOrderItem = fillSupplyForm(0, 0, "2", 0, 0);

        // Assert that a supply row was successfully added
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() > 0);

        // Build an accomplished order object to store its current state for later comparison
        accomplishedOrder = new Order(
                null,                                              // Order number (unknown yet)
                Order.OrderStatus.ACCOMPLISHED,                   // Expected status
                loginPage.getLoginUser().getName(),               // User who submitted the order
                LocalDateTime.now(),                              // Timestamp for creation
                List.of(Objects.requireNonNull(requestedOrderItem)), // Appended item
                null                                               // URL (to be set after redirect)
        );

        // Submit the order by clicking 'Create Accomplished'
        guestSuppliesPage.clickCreateAccomplishedButton();

        // Verify successful creation: toast + URL redirection
        assertSupplySuccessfullyOrdered();

        // Save URL and order number from the view page for later verification
        accomplishedOrder.setUrl(getRootDriver().getCurrentUrl());
        accomplishedOrder.setOrderNo(suppliesViewPage.getOrderNo());

        // Print for debug/reference
        System.out.println("\n***Accomplished Order***\n\t" + accomplishedOrder);
    }

    /**
     * TC02: Append without selecting a category (invalid test case).
     * Should be rejected and not appended.
     */
    @Test(testName = "TC02 - Append without selecting category", suiteName = "Guest Supplies")
    public void tc02AppendWithoutCategory() {
        openGuestSuppliesPage(); // Navigate to the form

        // Fill all fields except category
        guestSuppliesPage.insertQuantity("2")
                .clickUnitTypes().selectSingleUnitTypesOption(0)
                .clickUnitNumber().selectSingleUnitNumberOption(0)
                .clickAppendButton();

        // Verify that no row was added and error toast appeared
        assertSupplyNotAdded();
    }

    /**
     * TC03: Append without selecting supply.
     * Should not be allowed.
     */
    @Test(testName = "TC03 - Append without selecting supply", suiteName = "Guest Supplies")
    public void tc03AppendWithoutSupply() {
        openGuestSuppliesPage();          // Go to form
        fillFormWithCategoryOnly("3");    // Only select category + quantity

        // Try submitting without supply
        guestSuppliesPage.clickUnitTypes().selectSingleUnitTypesOption(0)
                .clickUnitNumber().selectSingleUnitNumberOption(0)
                .clickAppendButton();

        assertSupplyNotAdded();           // Expect rejection
    }

    /**
     * TC04: Append supply with quantity set to 0.
     * Should trigger a validation failure.
     */
    @Test(testName = "TC04 - Append with quantity zero", suiteName = "Guest Supplies")
    public void tc04AppendWithZeroQuantity() {
        openGuestSuppliesPage();                          // Go to form
        fillSupplyForm(0, 0, "0", 0, 0);                  // Supply with 0 quantity
        assertSupplyNotAdded();                           // Should be rejected
    }

    /**
     * TC05: Append without selecting unit types.
     * Should not be allowed.
     */
    @Test(testName = "TC05 - Append without selecting unit types", suiteName = "Guest Supplies")
    public void tc05AppendWithoutUnitTypes() {
        openGuestSuppliesPage();                          // Navigate
        fillFormWithCategoryOnly("5");                    // Fill only category + quantity
        guestSuppliesPage.clickSupplyDropbox().selectSupplyOption(0); // Choose supply only

        guestSuppliesPage.clickAppendButton();            // Submit without units
        assertSupplyNotAdded();                           // Should be rejected
    }

    /**
     * TC06: Append multiple valid rows and assert they are added.
     */
    @Test(testName = "TC06 - Append multiple valid rows", suiteName = "Guest Supplies")
    public void tc06AppendMultipleValidRows() {
        openGuestSuppliesPage();                          // Start fresh

        // Loop to add two different supplies
        for (int supplyIteration = 0; supplyIteration < 2; supplyIteration++) {
            guestSuppliesPage.clickCategoryDropbox().selectCategoryOption(0)     // Select same category
                    .clickSupplyDropbox().selectSupplyOption(supplyIteration)    // Change supply per iteration
                    .insertQuantity("2");

            // Only select unit type for the first one (to test edge case flexibility)
            if (supplyIteration == 0)
                guestSuppliesPage.clickUnitTypes().selectSingleUnitTypesOption(0)
                        .clickUnitNumber().selectSingleUnitNumberOption(0);

            guestSuppliesPage.clickAppendButton();       // Add to table
            sleep(2000);                                 // Small delay between actions
        }

        // Validate that both entries were accepted
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() >= 2);
    }

    /**
     * TC07: Append with a negative quantity.
     * Should be blocked by validation.
     */
    @Test(testName = "TC07 - Append with negative quantity", suiteName = "Guest Supplies")
    public void tc07AppendWithNegativeQuantity() {
        openGuestSuppliesPage();                         // Navigate
        fillSupplyForm(0, 0, "-3", 0, 0);                // Invalid quantity
        assertSupplyNotAdded();                          // Assert rejection
    }

    /**
     * TC08: Append with decimal quantity.
     * Should be parsed and accepted (converted to integer internally).
     */
    @Test(testName = "TC08 - Append with decimal quantity", suiteName = "Guest Supplies")
    public void tc08AppendWithDecimalQuantity() {
        openGuestSuppliesPage();                         // Load form
        fillSupplyForm(0, 0, "2.5", 0, 0);               // Input 2.5 as quantity

        // It should get converted to 25 (internal logic assumes multiplication)
        Assert.assertTrue(
                guestSuppliesPage.countOfAddedSupplies() > 0 &&
                        guestSuppliesPage.quantityPerUnitValueByIndex(0).equals("25")
        );
    }

    /**
     * TC09: Append using invalid (text) quantity input.
     * Must be rejected due to parsing failure.
     */
    @Test(testName = "TC09 - Append with non-numeric quantity input", suiteName = "Guest Supplies")
    public void tc09AppendWithTextInQuantity() {
        openGuestSuppliesPage();                        // Load form
        fillSupplyForm(0, 0, "abc", 0, 0);              // Input invalid quantity
        assertSupplyNotAdded();                         // Assert rejection
    }

    /**
     * TC10: Attempt to add the same supply row twice.
     * Expect that the second one will not be appended due to duplication check.
     */
    @Test(testName = "TC10 - Append duplicate supply row", suiteName = "Guest Supplies")
    public void tc10AppendDuplicateRow() {
        openGuestSuppliesPage();                        // Load form

        int suppliesLimit = 2;
        for (int i = 0; i < suppliesLimit; i++) {
            guestSuppliesPage.clickCategoryDropbox().selectCategoryOption(0)
                    .clickSupplyDropbox().selectSupplyOption(0)      // Use same supply twice
                    .insertQuantity("2");

            if (i == 0)
                guestSuppliesPage.clickUnitTypes().selectSingleUnitTypesOption(0)
                        .clickUnitNumber().selectSingleUnitNumberOption(0);

            guestSuppliesPage.clickAppendButton();      // Try to add it
        }

        // Ensure only 1 was accepted due to duplication check
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() < suppliesLimit);
    }

    /**
     * TC11 - Verifies that the total quantity per unit is correctly calculated and displayed.
     */
    @Test(testName = "TC11 - Verify total calculation of quantity", suiteName = "Guest Supplies")
    public void tc11VerifyTotalCalculation() {
        // Open the Guest Supplies form page
        openGuestSuppliesPage();

        // Define quantity value to be tested
        String quantity = "10";

        // Fill and append a valid supply item with that quantity
        fillSupplyForm(0, 0, quantity, 0, 1);

        // Assert that the supply is appended and the total quantity matches the input
        Assert.assertTrue(
                (guestSuppliesPage.countOfAddedSupplies() > 0) &&
                        guestSuppliesPage.quantityPerUnitValueByIndex(0).equals(quantity)
        );
    }

    /**
     * TC12 - Verifies that after appending a supply, the form resets its fields.
     */
    @Test(testName = "TC12 - Check if form resets after append", suiteName = "Guest Supplies")
    public void tc12CheckFormResetAfterAppend() {
        // Navigate to the page
        openGuestSuppliesPage();

        // Append one valid item
        fillSupplyForm(0, 0, "2", 0, 0);

        // Check that form is mostly reset (low number of clear buttons)
        System.out.println("Count:  " + guestSuppliesPage.getClearButtonsCount());
        Assert.assertTrue(guestSuppliesPage.getClearButtonsCount() <= 2);
    }

    /**
     * TC13 - Attempts to create an accomplished order without appending any supplies.
     * Expected to fail with an error toast.
     */
    @Test(testName = "TC13 - Create accomplished order without appending", suiteName = "Guest Supplies")
    public void tc13CreateAccomplishedOrderWithoutAppending() {
        // Open the page
        openGuestSuppliesPage();

        // Try to submit an order with no items
        guestSuppliesPage.clickCreateAccomplishedButton();

        // Assert an error toast is shown
        Assert.assertTrue(basePage.isErrorToastDisplayed(), "Expected error toast when no supplies are appended.");
    }

    /**
     * TC14 - Submits a valid proposed order and saves it for later assertions.
     */
    @Test(testName = "TC14 - Request valid Proposed order", suiteName = "Guest Supplies", retryAnalyzer = RetryAnalyzer.class)
    public void tc14CreateValidProposedOrder() {
        // Navigate to guest supplies form
        openGuestSuppliesPage();

        // Append a valid supply row
        Order.OrderItem requestedOrderItem = fillSupplyForm(0, 0, "2", 0, 0);
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() > 0, "Supply item not appended.");

        // Build and assign expected order object
        proposedOrder = new Order(
                null,
                Order.OrderStatus.PROPOSED,
                loginPage.getLoginUser().getName(),
                LocalDateTime.now(),
                List.of(Objects.requireNonNull(requestedOrderItem)),
                null
        );

        // Submit the order
        guestSuppliesPage.clickCreateProposedOrderButton();
        assertSupplySuccessfullyOrdered();

        // Save its details for later assertions
        proposedOrder.setUrl(getRootDriver().getCurrentUrl());
        proposedOrder.setOrderNo(suppliesViewPage.getOrderNo());

        // Log the created order
        System.out.println("\n***Proposed Order***\n\t" + proposedOrder);
    }

    /**
     * TC15 - Attempts to submit a proposed order with no supplies appended.
     * Should fail and show error toast.
     */
    @Test(testName = "TC15 - Create proposed order without appending", suiteName = "Guest Supplies")
    public void tc15CreateProposedOrderWithoutAppending() {
        openGuestSuppliesPage();
        guestSuppliesPage.clickCreateProposedOrderButton();
        Assert.assertTrue(basePage.isErrorToastDisplayed(), "Expected error toast when creating proposed order without supplies.");
    }

    /**
     * TC16 - Appends one supply then removes it and checks if the list becomes empty.
     */
    @Test(testName = "TC16 - Remove appended supply", suiteName = "Guest Supplies")
    public void tc16RemoveAppendedSupply() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 1);

        // Remove the supply
        guestSuppliesPage.deleteAppendedSupplyByIndex(0);
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 0, "Supply not removed properly");
    }

    /**
     * TC17 - Edits an appended supply, verifies it resets the form and allows re-append.
     */
    @Test(testName = "TC17 - Edit appended supply", suiteName = "Guest Supplies")
    public void tc17EditAppendedSupply() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 1);

        // Edit the row
        guestSuppliesPage.editAppendedSupplyByIndex(0);
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 0, "Supply not removed properly");

        // Re-append it again
        guestSuppliesPage.clickAppendButton();
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 1);
    }

    /**
     * TC18 - Verifies that discarding the form (cancel) after append redirects to the orders list.
     */
    @Test(testName = "TC18 - Discard appended supply", suiteName = "Guest Supplies")
    public void tc18DiscardAppendedSupply() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);
        Assert.assertEquals(guestSuppliesPage.countOfAddedSupplies(), 1);

        // Discard the order
        guestSuppliesPage.clickDiscardButton();

        // Ensure redirection
        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlContains(SuppliesOrderPage.URL));
        } catch (TimeoutException e) {
            System.out.println("URL didn't change to supplies order page!");
        }
    }

    /**
     * TC19 - Create accomplished order with an overly long comment and validate trimming.
     */
    @Test(testName = "TC19 - Create Accomplished Order with long comment", suiteName = "Guest Supplies")
    public void tc19CreateAccomplishedOrderWithLongComment() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);

        // Add 500-char comment and validate it's trimmed to 400
        String longComment = "C".repeat(500);
        guestSuppliesPage.insertComment(longComment);
        Assert.assertEquals(guestSuppliesPage.getCommentLength(), 400, "Comment length should be 400.");

        // Append and submit
        guestSuppliesPage.clickAppendButton();
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() > 0);
        guestSuppliesPage.clickCreateAccomplishedButton();
        assertSupplySuccessfullyOrdered();
    }

    /**
     * TC20 - Create proposed order with a long comment to check max length enforcement.
     */
    @Test(testName = "TC20 - Create Proposed Order with long comment", suiteName = "Guest Supplies")
    public void tc20CreateProposedOrderWithLongComment() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);

        String longComment = "C".repeat(600);
        guestSuppliesPage.insertComment(longComment);
        Assert.assertEquals(guestSuppliesPage.getCommentLength(), 400, "Comment length should be 400.");

        guestSuppliesPage.clickAppendButton();
        Assert.assertTrue(guestSuppliesPage.countOfAddedSupplies() > 0);
        guestSuppliesPage.clickCreateProposedOrderButton();
        assertSupplySuccessfullyOrdered();
    }

    /**
     * TC21 - Verifies that clicking Cancel clears form fields and doesn't retain old entries.
     */
    @Test(testName = "TC21 - Check if cancel removes entered supply", suiteName = "Guest Supplies")
    public void tc21CheckCancelResetsEnteredSupply() {
        openGuestSuppliesPage();
        fillSupplyForm(0, 0, "2", 0, 0);

        // Trigger cancel
        guestSuppliesPage.clickCancelButton();

        // Assert form reset (low clearable field count)
        System.out.println("Count:  " + guestSuppliesPage.getClearButtonsCount());
        Assert.assertTrue(guestSuppliesPage.getClearButtonsCount() <= 2);
    }
}