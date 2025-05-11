package nazeel.base;

import nazeel.utils.KeyboardUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.Month;
import java.util.Arrays;

import static nazeel.base.TestBase.getRootDriver;

/**
 * Utility class used for interacting with custom calendar/time pickers
 * in the application under test.
 * <p>
 * This class assumes that the calendar and time pickers use text-based
 * inputs that are navigated via keyboard input (instead of separate dropdowns).
 */
public class CalendarPicker {

    // -------------------- Locators --------------------

    /**
     * Locator for the "Set" button in the time picker dialog (e.g., used after selecting hour/minute/AM-PM)
     */
    private final By setButtonBy = By.className("k-time-accept");

    // -------------------- Accessors --------------------

    /**
     * Returns the WebElement representing the "Set" button inside the date/time picker dialog.
     *
     * @return WebElement for the Set button
     */
    private WebElement getSetButton() {
        return getRootDriver().findElement(setButtonBy);
    }

    // -------------------- Actions --------------------

    /**
     * Fills in a date by sending day, month, and year values via keyboard input.
     * Assumes the input field is already focused and active.
     *
     * @param day   the day of the month (e.g., "25")
     * @param month the Java Month enum (e.g., Month.APRIL)
     * @param year  the full year (e.g., "2025")
     * @return the current CalendarPicker instance for chaining
     */
    public CalendarPicker enterDate(String day, Month month, String year) {
        WebElement inputField = getRootDriver().switchTo().activeElement(); // assumes date input is focused

        for (String date : Arrays.asList(day, String.valueOf(month.getValue()), year)) {
            KeyboardUtil.sendKeysWithInterval(inputField, date, 100);
        }

        return this;
    }

    /**
     * Fills in time values by sending hour, minute, and AM/PM via keyboard input.
     * Assumes the input field is already focused and active.
     *
     * @param hour   the hour value (e.g., "02")
     * @param minute the minute value (e.g., "30")
     * @param amPm   "AM" or "PM"
     * @return the current CalendarPicker instance for chaining
     */
    public CalendarPicker enterTime(String hour, String minute, String amPm) {
        WebElement inputField = getRootDriver().switchTo().activeElement(); // assumes time input is focused

        for (String time : Arrays.asList(hour, minute, amPm)) {
            KeyboardUtil.sendKeysWithInterval(inputField, time, 100);
        }

        return this;
    }

    /**
     * Clicks the "Set" button on the time picker to confirm selection.
     * May throw exceptions if the element is not found or not interactable.
     *
     * @return the current CalendarPicker instance for chaining
     * @throws NoSuchElementException          if the Set button is not found
     * @throws ElementNotInteractableException if the Set button cannot be clicked
     */
    public CalendarPicker clickSetButton() throws NoSuchElementException, ElementNotInteractableException {
        getSetButton().click();
        return this;
    }
}
