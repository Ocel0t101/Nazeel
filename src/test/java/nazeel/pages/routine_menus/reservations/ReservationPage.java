package nazeel.pages.routine_menus.reservations;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

public class ReservationPage {
    public final static String GUEST_ID = "123456789";
    public final static String URL = "https://staging.nazeel.net:9002/reservations";
    private final By newReservationButtonBy = By.cssSelector(".n-button.n-button--green");
    private final By moreActionsButtonBy = By.cssSelector(".popup__btn.popup__btn--purple");
    private final By spansBy = By.cssSelector("span.k-input");
    private final By optionsBy = By.cssSelector("div>ul>li[role='option']");
    private final By addUnitsButtonBy = By.cssSelector(".n-table__header-action.ng-star-inserted");
    private final By unitTypeTapBy = By.cssSelector(".no-border-bottom.k-button.k-group-end.ng-star-inserted");
    private final By unitTypeCardBy = By.className("unit-type-card");
    private final By unitTypeAddRoomButtonBy = By.cssSelector(".unit-type-card__counter>button:nth-child(3)");
    private final By unitTypeRemoveRoomButtonBy = By.cssSelector(".unit-type-card__counter>button:nth-child(1)");
    private final By unitTypeConfirmButtonBy = By.cssSelector("button[type='submit']");
    private final By unitSelectionButtonBy = By.cssSelector(".button--secondary.n-table-action.k-button.ng-star-inserted");
    private final By selectGuestButtonBy = By.cssSelector(".n-button.n-button--primary.u-m-end-15");
    private final By inputIDNumberBy = By.cssSelector("input[placeholder='ID Number']");
    private final By guestContainerBy = By.id("guestFormDialogContainer");
    private final By guestRowBy = By.cssSelector("tr[data-kendo-grid-item-index]");
    private final By guestConfirmButtonBy = By.cssSelector("button.n-button.n-button--primary.ng-star-inserted");
    private final By saveReservationButtonBy = By.cssSelector("button.button--primary.u-px-50");
    private final By confirmSaveReservationButtonBy = By.cssSelector("kendo-dialog-actions>div>button.button--primary");
    private final By checkInButtonBy = By.cssSelector("button.button--green-border");
    private final By checkOutButtonBy = By.cssSelector("button.button--danger-border");
    private final By checkOutConfirmButtonBy = By.cssSelector(".swal2-confirm.sweet-alert__button");
    private final By checkOutPaymentInputBy = By.cssSelector("input[placeholder='Select Payment Method']");
    private final By checkOutSaveAndContinueButtonBy = By.xpath("//button[text()=' Save & Continue ']");
    private final By undoCheckInButtonBy = By.xpath("//div[@class='ng-star-inserted'][normalize-space(text())='Undo Check In']"); //Note: XPath is used here because there are no special parameters to locate

    // Locators to interact with UI elements
    private WebElement getNewReservationButton() {
        return getRootDriver().findElement(newReservationButtonBy);
    }

    private WebElement getMoreActionsButton() {
        return getRootDriver().findElement(moreActionsButtonBy);
    }

    private WebElement getVisitPurposeSpan() {
        return getRootDriver().findElements(spansBy).getFirst();
    }

    private WebElement getReservationSourceSpan() {
        return getRootDriver().findElements(spansBy).get(1);
    }

    private WebElement getRentalTypeSpan() {
        return getRootDriver().findElements(spansBy).get(2);
    }

    private List<WebElement> getVisitPurposeOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private List<WebElement> getReservationSourceOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private List<WebElement> getRentalTypeOptions() {
        return getRootDriver().findElements(optionsBy);
    }

    private WebElement getAddUnitsButton() {
        return getRootDriver().findElement(addUnitsButtonBy);
    }

    private WebElement getUnitTypeTap() {
        return getRootDriver().findElement(unitTypeTapBy);
    }

    private WebElement getUnitTypeAddRoomButton() {
        return getRootDriver().findElement(unitTypeAddRoomButtonBy);
    }

    private WebElement getUnitTypeRemoveRoomButton() {
        return getRootDriver().findElement(unitTypeRemoveRoomButtonBy);
    }

    private WebElement getUnitTypeConfirmButton() {
        return getRootDriver().findElement(unitTypeConfirmButtonBy);
    }

    private WebElement getUnitTypeCard() {
        return getRootDriver().findElement(unitTypeCardBy);
    }

    private WebElement getUnitSelectionButton() {
        return getRootDriver().findElement(unitSelectionButtonBy);
    }

    private WebElement getSelectGuestButton() {
        return getRootDriver().findElement(selectGuestButtonBy);
    }

    private WebElement getInputIDNumber() {
        return getRootDriver().findElement(inputIDNumberBy);
    }

    private WebElement getGuestRow(int index) {
        return getRootDriver().findElement(guestContainerBy).findElements(guestRowBy).get(index);
    }

    private WebElement getConfirmGuestButton() {
        return getRootDriver().findElement(guestConfirmButtonBy);
    }

    private WebElement getSaveReservationButton() {
        return getRootDriver().findElement(saveReservationButtonBy);
    }

    private WebElement getConfirmSaveReservationButton() {
        return getRootDriver().findElement(confirmSaveReservationButtonBy);
    }

    private WebElement getCheckInButton() {
        return getRootDriver().findElement(checkInButtonBy);
    }

    private WebElement getCheckOutButton() {
        return getRootDriver().findElement(checkOutButtonBy);
    }

    private WebElement getUndoCheckInButton() {
        return getRootDriver().findElement(undoCheckInButtonBy);
    }

    // --------- Page Actions ---------
    public ReservationPage clickNewReservationButton() {
        getNewReservationButton().click();
        return this;
    }

    public ReservationPage clickMoreActionsButton() {
        getMoreActionsButton().click();
        return this;
    }

    public ReservationPage navigateToReservationPage() {
        getRootDriver().get(URL);
        return this;
    }

    public ReservationPage clickVisitPurposeDropbox() {
        getVisitPurposeSpan().click();
        return this;
    }

    public ReservationPage selectVisitPurposeOption(int index) {
        getVisitPurposeOptions().get(index).click();
        return this;
    }

    public ReservationPage clickReservationSourceDropbox() {
        getReservationSourceSpan().click();
        return this;
    }

    public ReservationPage selectReservationSourceOption(int index) {
        getReservationSourceOptions().get(index).click();
        return this;
    }

    public ReservationPage clickRentalTypeDropbox() {
        getRentalTypeSpan().click();
        return this;
    }

    public ReservationPage selectRentalTypeOption(RentalType rentalType) {
        getRentalTypeOptions().get(rentalType.getIndex()).click();
        return this;
    }

    public ReservationPage clickAddUnitsButton() {
        getAddUnitsButton().click();
        return this;
    }

    public ReservationPage clickUnitTypeTap() {
        getUnitTypeTap().click();
        return this;
    }

    public ReservationPage clickUnitTypeCard() {
        getUnitTypeCard().click();
        return this;
    }

    public ReservationPage clickUnitTypeAddRoomButton() {
        getUnitTypeAddRoomButton().click();
        return this;
    }

    public ReservationPage clickUnitTypeRemoveRoomButton() {
        getUnitTypeRemoveRoomButton().click();
        return this;
    }

    public ReservationPage clickUnitTypeConfirmButton() {
        getUnitTypeConfirmButton().click();
        return this;
    }

    public ReservationPage clickUnitSelectionButton() {
        getUnitSelectionButton().click();
        return this;
    }

    public ReservationPage clickSelectGuestButton() {
        getSelectGuestButton().click();
        return this;
    }

    public ReservationPage enterIDNumber(String id) {
        getInputIDNumber().sendKeys(id, Keys.ENTER);
        return this;
    }

    public ReservationPage selectGuest(int index) {
        getGuestRow(index).click();
        return this;
    }

    public ReservationPage clickGuestConfirmButton() {
        getConfirmGuestButton().click();
        return this;
    }

    public ReservationPage clickSaveReservationButton() {
        getSaveReservationButton().click();
        return this;
    }

    public ReservationPage clickConfirmSaveReservationButton() {
        getConfirmSaveReservationButton().click();
        return this;
    }

    public ReservationPage clickCheckInButton() {
        getCheckInButton().click();
        return this;
    }

    public ReservationPage clickCheckOutButton() {
        getCheckOutButton().click();
        return this;
    }

    public boolean isUndoCheckInButtonVisible() {
        try {
            System.out.println(getUndoCheckInButton().getCssValue("opacity"));
            return (Double.parseDouble(getUndoCheckInButton().getCssValue("opacity")) > 0);
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    public enum RentalType {
        DAILY(0),
        MONTHLY(1);

        private final int index;

        RentalType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

}
