package nazeel.test_suites.routine_menus.reservations;

import nazeel.base.TestBase;
import nazeel.pages.routine_menus.reservations.ReservationPage;
import nazeel.utils.RetryAnalyzer;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

import static nazeel.base.TestBase.Waits.WAIT_UNTIL_LOADS;

public class UndoCheckInTS extends TestBase {
    private final ReservationPage reservationPage = new ReservationPage();
    private static boolean isNewReservationCreated = false;

    private boolean createNewDailyReservation() {
        if (!Objects.requireNonNull(getRootDriver().getCurrentUrl()).contains("/reservations")) {
            reservationPage.navigateToReservationPage()
                    .clickNewReservationButton();
            try {
                explicitWait(WAIT_UNTIL_LOADS.getSeconds())            // Wait for URL to change
                        .until(ExpectedConditions.urlContains("/reservations"));
            } catch (TimeoutException e) {
                Assert.fail("URL didn't change to new reservation page!"); // Fail if page didn't redirect
            }
        }

        if (!isNewReservationCreated) {
            isNewReservationCreated = true;
            reservationPage.clickVisitPurposeDropbox()
                    .selectVisitPurposeOption(0)
                    .clickReservationSourceDropbox()
                    .selectReservationSourceOption(0)
                    .clickRentalTypeDropbox()
                    .selectRentalTypeOption(ReservationPage.RentalType.DAILY)
                    .clickAddUnitsButton()
                    .clickUnitTypeTap()
                    .clickUnitTypeCard()
                    .clickUnitTypeConfirmButton()
                    .clickUnitSelectionButton()
                    .clickSelectGuestButton()
                    .enterIDNumber(ReservationPage.GUEST_ID)
                    .selectGuest(0)
                    .clickGuestConfirmButton()
                    .clickSaveReservationButton()
                    .clickConfirmSaveReservationButton();
            return false; //Created for the first time
        }

        return true; //Already created
    }

    @Test(testName = "TC01 - The Undo Check-In button is not visible when creating a new reservation", suiteName = "Undo Check-in", retryAnalyzer = RetryAnalyzer.class)
    public void tc01UndoCheckInForConfirmedReservation() {
        Assert.assertFalse(
                reservationPage.navigateToReservationPage()
                        .clickNewReservationButton()
                        .clickMoreActionsButton()
                        .isUndoCheckInButtonVisible()
        );

    }

    @Test(testName = "TC02 - The Undo Check-In is not visible for new daily confirmed reservation", suiteName = "Undo Check-in", retryAnalyzer = RetryAnalyzer.class)
    public void tc02UndoCheckInForDailyConfirmedReservation() {
        createNewDailyReservation();
        Assert.assertFalse(
                reservationPage.clickMoreActionsButton()
                        .isUndoCheckInButtonVisible()
        );
    }

    @Test(testName = "TC03 - The Undo Check-In is visible for daily checked-in reservation", suiteName = "Undo Check-in")
    public void tc03UndoCheckInForDailyCheckedInReservation() {
        createNewDailyReservation();
        Assert.assertTrue(
                reservationPage.clickCheckInButton()
                        .clickMoreActionsButton()
                        .isUndoCheckInButtonVisible()
        );
    }

    @Test(testName = "TC04 - The Undo Check-In is not visible for daily checked-out reservation", suiteName = "Undo Check-in")
    public void tc04UndoCheckInForDailyCheckedOutReservation() {
        if (!createNewDailyReservation())
            reservationPage.clickCheckInButton(); //click check-in if created for the first time [confirmed]
        Assert.assertFalse(
                reservationPage.clickCheckOutButton()
                        .clickMoreActionsButton()
                        .isUndoCheckInButtonVisible()
        );
    }


}
