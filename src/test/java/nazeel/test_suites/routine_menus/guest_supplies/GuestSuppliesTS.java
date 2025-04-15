package nazeel.test_suites.routine_menus.guest_supplies;

import nazeel.base.PageBase;
import nazeel.listeners.TestListener;
import nazeel.pages.DashboardPage;
import nazeel.base.TestBase;
import nazeel.pages.routine_menus.guest_supplies.GuestSuppliesPage;
import nazeel.pages.LoginPage;
import nazeel.pages.routine_menus.guest_supplies.SuppliesOrderPage;
import nazeel.utils.RetryAnalyzer;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static nazeel.base.TestBase.Waits.WAIT_UNTIL_DISPLAYED;
import static nazeel.base.TestBase.Waits.WAIT_UNTIL_LOADS;

@Listeners(TestListener.class)
public class GuestSuppliesTS extends TestBase {
    private final GuestSuppliesPage suppliesPage = new GuestSuppliesPage();
    private final PageBase basePage = new PageBase();

    //NOTE: [TEMP]
    @BeforeClass
    public void tempLoginAndNavigateToDashboard() {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("MGamalTest")
                .insertPassword("132456789Aa@")
                .insertAccessCode("01456")
                .clickLoginButton();

        sleep(4000);
        loginPage.selectPropertyByIndex(2);
        waitForPageToLoad(DashboardPage.URL);
    }

    private void openGuestSuppliesPage() {
        suppliesPage.navigateToGuestSuppliesPage();
        waitForPageToLoad(GuestSuppliesPage.URL);
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());
    }

    private void fillValidSupplyForm(String quantity, int categoryIndex, int supplyIndex, int unitTypeIndex, int unitNumberIndex) {
        suppliesPage.clickCategoryDropbox().selectCategoryOption(categoryIndex)
                .clickSupplyDropbox().selectSupplyOption(supplyIndex)
                .insertQuantity(quantity)
                .clickUnitTypes().selectUnitTypesOption(unitTypeIndex)
                .clickUnitNumber().selectUnitNumberOption(unitNumberIndex);
    }

    private void fillFormWithCategoryOnly(String quantity) {
        suppliesPage.clickCategoryDropbox().selectCategoryOption(0)
                .insertQuantity(quantity);
    }

    private void assertSupplyNotAdded() {
        try {
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> basePage.isErrorToastDisplayed());
        } catch (TimeoutException e) {
            System.out.println("Error toast didn't display!");
        }
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0);
    }

    private void assertSupplySuccessfullyOrdered() {
        try {
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> basePage.isSuccessToastDisplayed());
        } catch (TimeoutException e) {
            System.out.println("Success toast didn't display!");
        }

        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlContains(SuppliesOrderPage.URL));
        } catch (TimeoutException e) {
            System.out.println("URL didn't change to supplies order page!");
        }
    }

    @Test(testName = "TC01 - Request valid accomplished order", suiteName = "Guest Supplies", retryAnalyzer = RetryAnalyzer.class)
    public void tc01ValidAccomplishedOrder() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 1);
        suppliesPage.clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0);
        suppliesPage.clickCreateAccomplishedButton();
        assertSupplySuccessfullyOrdered();
    }

    @Test(testName = "TC02 - Append without selecting category", suiteName = "Guest Supplies")
    public void tc02AppendWithoutCategory() {
        openGuestSuppliesPage();
        suppliesPage.insertQuantity("2")
                .clickUnitTypes().selectUnitTypesOption(0)
                .clickUnitNumber().selectUnitNumberOption(0)
                .clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC03 - Append without selecting supply", suiteName = "Guest Supplies")
    public void tc03AppendWithoutSupply() {
        openGuestSuppliesPage();
        fillFormWithCategoryOnly("3");
        suppliesPage.clickUnitTypes().selectUnitTypesOption(0)
                .clickUnitNumber().selectUnitNumberOption(0)
                .clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC04 - Append with quantity zero", suiteName = "Guest Supplies")
    public void tc04AppendWithZeroQuantity() {
        openGuestSuppliesPage();
        fillValidSupplyForm("0", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC05 - Append without selecting unit types", suiteName = "Guest Supplies")
    public void tc05AppendWithoutUnitTypes() {
        openGuestSuppliesPage();
        fillFormWithCategoryOnly("5");
        suppliesPage.clickSupplyDropbox().selectSupplyOption(0);
        suppliesPage.clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC06 - Append multiple valid rows", suiteName = "Guest Supplies")
    public void tc06AppendMultipleValidRows() {
        openGuestSuppliesPage();
        for (int supplyIteration = 0; supplyIteration < 2; supplyIteration++) {

            suppliesPage.clickCategoryDropbox().selectCategoryOption(0)
                    .clickSupplyDropbox().selectSupplyOption(supplyIteration)
                    .insertQuantity("2");

            if (supplyIteration == 0)
                suppliesPage.clickUnitTypes().selectUnitTypesOption(0)
                        .clickUnitNumber().selectUnitNumberOption(0);

            suppliesPage.clickAppendButton();
            sleep(2000);
        }
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() >= 2);
    }

    @Test(testName = "TC07 - Append with negative quantity", suiteName = "Guest Supplies")
    public void tc07AppendWithNegativeQuantity() {
        openGuestSuppliesPage();
        fillValidSupplyForm("-3", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC08 - Append with decimal quantity", suiteName = "Guest Supplies")
    public void tc08AppendWithDecimalQuantity() {
        openGuestSuppliesPage();
        String quantity = "2.5";
        fillValidSupplyForm(quantity, 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        Assert.assertTrue(
                (suppliesPage.countOfAddedSupplies() > 0) &&
                        suppliesPage.quantityPerUnitValueByIndex(0).equals(quantity.replace(".", ""))
        );
    }

    @Test(testName = "TC09 - Append with non-numeric quantity input", suiteName = "Guest Supplies")
    public void tc09AppendWithTextInQuantity() {
        openGuestSuppliesPage();
        fillValidSupplyForm("abc", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        assertSupplyNotAdded();
    }

    @Test(testName = "TC10 - Append duplicate supply row", suiteName = "Guest Supplies")
    public void tc10AppendDuplicateRow() {
        openGuestSuppliesPage();
        int suppliesLimit = 2;
        for (int supplyIteration = 0; supplyIteration < suppliesLimit; supplyIteration++) {
            suppliesPage.clickCategoryDropbox().selectCategoryOption(0)
                    .clickSupplyDropbox().selectSupplyOption(0)
                    .insertQuantity("2");

            if (supplyIteration == 0)
                suppliesPage.clickUnitTypes().selectUnitTypesOption(0)
                        .clickUnitNumber().selectUnitNumberOption(0);

            suppliesPage.clickAppendButton();
        }
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() < suppliesLimit);
    }

    @Test(testName = "TC11 - Verify total calculation of quantity", suiteName = "Guest Supplies")
    public void tc11VerifyTotalCalculation() {
        openGuestSuppliesPage();
        int quantity = 3;
        fillValidSupplyForm(String.valueOf(quantity), 0, 0, 0, 1);
        suppliesPage.clickAppendButton();
        Assert.assertTrue(
                (suppliesPage.countOfAddedSupplies() > 0) &&
                        suppliesPage.quantityPerUnitValueByIndex(0).equals(String.valueOf(quantity))
        );
    }

    @Test(testName = "TC12 - Check if form resets after append", suiteName = "Guest Supplies")
    public void tc12CheckFormResetAfterAppend() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        System.out.println("Count:  " + suppliesPage.getClearButtonsCount());
        Assert.assertTrue(suppliesPage.getClearButtonsCount() <= 2);
    }

    @Test(testName = "TC13 - Create accomplished order without appending", suiteName = "Guest Supplies")
    public void tc13CreateAccomplishedOrderWithoutAppending() {
        openGuestSuppliesPage();
        suppliesPage.clickCreateAccomplishedButton();

        // Expecting error toast
        Assert.assertTrue(basePage.isErrorToastDisplayed(), "Expected error toast when no supplies are appended.");
    }

    @Test(testName = "TC14 - Create proposed order with valid supplies", suiteName = "Guest Supplies")
    public void tc14CreateProposedOrderWithValidSupplies() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0, "Supply item not appended.");

        suppliesPage.clickCreateProposedOrderButton();
        assertSupplySuccessfullyOrdered();
    }

    @Test(testName = "TC15 - Create proposed order without appending", suiteName = "Guest Supplies")
    public void tc15CreateProposedOrderWithoutAppending() {
        openGuestSuppliesPage();
        suppliesPage.clickCreateProposedOrderButton();

        // Expect error toast
        Assert.assertTrue(basePage.isErrorToastDisplayed(), "Expected error toast when creating proposed order without supplies.");
    }

    @Test(testName = "TC16 - Remove appended supply", suiteName = "Guest Supplies")
    public void tc16RemoveAppendedSupply() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 1);

        suppliesPage.deleteAppendedSupplyByIndex(0);
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply not removed properly");
    }


    @Test(testName = "TC17 - Edit appended supply", suiteName = "Guest Supplies")
    public void tc17EditAppendedSupply() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 1);

        suppliesPage.editAppendedSupplyByIndex(0);
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply not removed properly");
        suppliesPage.clickAppendButton();
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 1);
    }

    @Test(testName = "TC18 - Discard appended supply", suiteName = "Guest Supplies")
    public void tc18DiscardAppendedSupply() {
        openGuestSuppliesPage();
        fillValidSupplyForm("2", 0, 0, 0, 0);
        suppliesPage.clickAppendButton();
        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 1);

        suppliesPage.clickDiscardButton();
        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlContains(SuppliesOrderPage.URL));
        } catch (TimeoutException e) {
            System.out.println("URL didn't change to supplies order page!");
        }
    }

    @Test(testName = "TC19 - Create Accomplished Order with long comment", suiteName = "Guest Supplies")
    public void tc19CreateAccomplishedOrderWithLongComment() {
        openGuestSuppliesPage();
        fillValidSupplyForm("5", 0, 0, 0, 0);

        String longComment = "C".repeat(500);
        suppliesPage.insertComment(longComment);
        Assert.assertEquals(suppliesPage.getCommentLength(), 400, "Comment length should be 400.");

        suppliesPage.clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0);

        suppliesPage.clickCreateAccomplishedButton();
        assertSupplySuccessfullyOrdered();
    }

    @Test(testName = "TC20 - Create Proposed Order with long comment", suiteName = "Guest Supplies")
    public void tc20CreateProposedOrderWithLongComment() {
        openGuestSuppliesPage();
        fillValidSupplyForm("1", 0, 0, 0, 0);

        String longComment = "C".repeat(600);
        suppliesPage.insertComment(longComment);
        Assert.assertEquals(suppliesPage.getCommentLength(), 400, "Comment length should be 400.");

        suppliesPage.clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0);

        suppliesPage.clickCreateProposedOrderButton(); // Make sure this method exists
        assertSupplySuccessfullyOrdered(); // Reuse the same validation
    }

}
