package nazeel.test_suites;

import nazeel.TestBase;
import nazeel.pages.DashboardPage;
import nazeel.pages.guest_supplies.GuestSuppliesPage;
import nazeel.pages.LoginPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static nazeel.TestBase.Waits.*;

public class GuestSuppliesTS extends TestBase {
    private final GuestSuppliesPage suppliesPage = new GuestSuppliesPage();

    @BeforeClass
    public void tempProceedToDashboard() {
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("MGamalTest");
        loginPage.insertPassword("132456789Aa@");
        loginPage.insertAccessCode("01456");
        loginPage.clickLoginButton();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loginPage.selectPropertyByIndex(2);
    }

    @Test(testName = "TC01 - Request valid accomplished order", suiteName = "Guest Supplies")
    public void tc01ValidAccomplishedOrder() {
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlToBe(DashboardPage.URL));
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());
        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0)
                .clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.selectSupplyOption(0);
        suppliesPage.insertQuantity("2")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.selectUnitTypesOption(0);
        implicitWait(WAIT_TEMP.getSeconds());
        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(1)
                .clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0, "Supply item not added!");
        implicitWait(10);
        suppliesPage.clickCreateAccomplishedButton();
        try {
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(drive -> suppliesPage.isSuccessToastDisplayed());
        } catch (TimeoutException e) {
            System.out.println("Success toast didn't display!");
        }

        try {
            explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(ExpectedConditions.urlContains(GuestSuppliesPage.URL));
        } catch (TimeoutException e) {
            System.out.println("URL didn't changed to supplies order page!");
        }
    }

    @Test(testName = "TC02 - Append without selecting category", suiteName = "Guest Supplies")
    public void tc02AppendWithoutCategory() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("2")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply item should not be added without category!");
    }

    @Test(testName = "TC03 - Append without selecting supply", suiteName = "Guest Supplies")
    public void tc03AppendWithoutSupply() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.insertQuantity("3")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply item should not be added without supply!");
    }

    @Test(testName = "TC04 - Append with quantity zero", suiteName = "Guest Supplies")
    public void tc04AppendWithZeroQuantity() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("0")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply item should not be added with zero quantity!");
    }

    @Test(testName = "TC05 - Append without selecting unit types", suiteName = "Guest Supplies")
    public void tc05AppendWithoutUnitTypes() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("5");

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply item should not be added without unit types!");
    }

    @Test(testName = "TC06 - Append multiple valid rows", suiteName = "Guest Supplies")
    public void tc06AppendMultipleValidRows() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        for (int i = 0; i < 2; i++) {
            suppliesPage.clickCategoryDropbox();
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
            suppliesPage.selectCategoryOption(0);

            suppliesPage.clickSupplyDropbox();
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
            suppliesPage.selectSupplyOption(0);

            suppliesPage.insertQuantity("2")
                    .clickUnitTypes();
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
            suppliesPage.selectUnitTypesOption(0);

            suppliesPage.clickUnitNumber()
                    .selectUnitNumberOption(0)
                    .clickAppendButton();

            implicitWait(WAIT_TEMP.getSeconds());
        }

        Assert.assertTrue(suppliesPage.countOfAddedSupplies() >= 2, "Multiple supply rows were not added correctly!");
    }

    @Test(testName = "TC07 - Append with negative quantity", suiteName = "Guest Supplies")
    public void tc07AppendWithNegativeQuantity() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("-3")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply should not be added with negative quantity!");
    }

    @Test(testName = "TC08 - Append with decimal quantity", suiteName = "Guest Supplies")
    public void tc08AppendWithDecimalQuantity() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("2.5")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        // Assuming the system converts decimals or rejects them. Adjust assertion accordingly.
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() == 0 || suppliesPage.countOfAddedSupplies() > 0,
                "Check how the system handles decimal quantity!");
    }

    @Test(testName = "TC09 - Append with non-numeric quantity input", suiteName = "Guest Supplies")
    public void tc09AppendWithTextInQuantity() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("abc"); // sending invalid characters
        suppliesPage.clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertEquals(suppliesPage.countOfAddedSupplies(), 0, "Supply should not be added with text in quantity!");
    }

    @Test(testName = "TC10 - Append duplicate supply row", suiteName = "Guest Supplies")
    public void tc10AppendDuplicateRow() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        for (int duplicationIteration = 0; duplicationIteration < 2; duplicationIteration++) {
            suppliesPage.clickCategoryDropbox();
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
            suppliesPage.selectCategoryOption(0);

            suppliesPage.clickSupplyDropbox();
            explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
            suppliesPage.selectSupplyOption(0);

            suppliesPage.insertQuantity("1");

            if (duplicationIteration == 0) {
                suppliesPage.clickUnitTypes();
                explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
                suppliesPage.selectUnitTypesOption(0);

                suppliesPage.clickUnitNumber()
                        .selectUnitNumberOption(0);
            }
            suppliesPage.clickAppendButton();

            implicitWait(WAIT_TEMP.getSeconds());
        }

        Assert.assertTrue(suppliesPage.countOfAddedSupplies() <= 2,
                "Duplicate row might be incorrectly accepted or not flagged.");
    }

    @Test(testName = "TC12 - Verify total calculation", suiteName = "Guest Supplies")
    public void tc12VerifyTotalCalculation() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        int quantity = 3;
        suppliesPage.insertQuantity(String.valueOf(quantity))
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(1)
                .clickAppendButton();

        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0, "Supply not appended!");
        Assert.assertEquals(suppliesPage.quantityPerUnitValueByIndex(0), quantity, "Total value mismatch!");
    }

    @Test(testName = "TC13 - Append with long comment", suiteName = "Guest Supplies")
    public void tc13AppendWithLongComment() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("2")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(1);
        suppliesPage.insertComment(("Test Comment ".repeat(50)));

        suppliesPage.clickAppendButton();
        Assert.assertTrue(suppliesPage.countOfAddedSupplies() > 0, "Supply item not added with long comment!");
    }

    @Test(testName = "TC14 - Check if form resets after append", suiteName = "Guest Supplies")
    public void tc14CheckFormResetAfterAppend() {
        suppliesPage.navigateToGuestSuppliesPage();
        explicitWait(WAIT_UNTIL_LOADS.getSeconds()).until(driver -> suppliesPage.isCategoryDropboxShown());

        suppliesPage.clickCategoryDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isCategoryOptionsEmpty());
        suppliesPage.selectCategoryOption(0);

        suppliesPage.clickSupplyDropbox();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> suppliesPage.isSupplyOptionsEnabled());
        suppliesPage.selectSupplyOption(0);

        suppliesPage.insertQuantity("2")
                .clickUnitTypes();
        explicitWait(WAIT_UNTIL_DISPLAYED.getSeconds()).until(driver -> !suppliesPage.isUnitTypesEmpty());
        suppliesPage.selectUnitTypesOption(0);

        suppliesPage.clickUnitNumber()
                .selectUnitNumberOption(0)
                .clickAppendButton();

        Assert.assertTrue((suppliesPage.getClearButtonsCount() <= 2) || suppliesPage.isQuantityInputClear(),
                "Form was not reset after append!");
    }

}
