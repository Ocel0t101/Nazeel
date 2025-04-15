package nazeel.base;

import org.openqa.selenium.*;

import static nazeel.base.TestBase.getRootDriver;

/**
 * Base page utility class that provides common reusable UI interaction methods
 * such as toast detection and loader visibility check.
 */
public class PageBase {

    // Locator for success toast notification
    private final By successToastBy = By.className("toast-success");

    // Locator for error toast notification
    private final By errorToastBy = By.className("toast-error");

    // Locator for page loading spinner
    private final By pageLoaderBy = By.className("page-loading");

    // Count how many times the error toast is checked (for debugging/logging)
    private static int appearanceCount = 0;

    /**
     * Returns the WebElement for success toast notification.
     */
    private WebElement getSuccessToast() {
        return getRootDriver().findElement(successToastBy);
    }

    /**
     * Returns the WebElement for error toast notification.
     */
    private WebElement getErrorToast() {
        return getRootDriver().findElement(errorToastBy);
    }

    /**
     * Returns the WebElement for the page loader spinner.
     */
    private WebElement getPageLoader() {
        return getRootDriver().findElement(pageLoaderBy);
    }

    /**
     * Checks whether the success toast is currently displayed.
     *
     * @return true if displayed, false otherwise
     */
    public boolean isSuccessToastDisplayed() {
        return getSuccessToast().isDisplayed();
    }

    /**
     * Checks whether the error toast is currently displayed.
     * Also prints a log of how many times it was checked.
     *
     * @return true if displayed, false otherwise
     */
    public boolean isErrorToastDisplayed() {
        System.out.println(++appearanceCount + " " + getErrorToast().isDisplayed());
        return getErrorToast().isDisplayed();
    }

    /**
     * Checks whether the page loader is currently shown on the screen.
     * This ensures that the loader is both visible and has full opacity.
     *
     * @return true if the loader is shown and visible, false otherwise
     */
    public boolean isPageLoaderShown() {
        try {
            return getPageLoader().isDisplayed() && getPageLoader().getCssValue("opacity").equals("1");
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
