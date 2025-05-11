package nazeel.base;

import nazeel.data_types.Order;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static nazeel.base.TestBase.explicitWait;
import static nazeel.base.TestBase.getRootDriver;

/**
 * PageBase class provides commonly used reusable methods for interacting
 * with UI components shared across multiple pages, such as:
 * - success/error toasts
 * - page loader visibility
 * - handling iframe switches
 * - accessing structured row data (used in orders table)
 */
public class PageBase {

    // -------------------- Locators --------------------

    // Toast that appears after successful operations (e.g. order created)
    private final By successToastBy = By.className("toast-success");

    // Toast that appears on failed operations (e.g. form validation error)
    private final By errorToastBy = By.className("toast-error");

    // Full-screen loader used while page or data is loading
    private final By pageLoaderBy = By.className("page-loading");

    // Table row locator used in viewing order item details
    private final By orderItemsRowsBy = By.cssSelector("tbody>tr");

    // Locator for cell elements inside a row
    private final By orderItemsColumnsBy = By.cssSelector("td");

    // Status message that appears inside print iframe ("Done", etc.)
    private final By printErrorMessageBy = By.className("trv-error-message");

    // Iframe element used to embed printable content or PDF preview
    private final By responsiveIframeBy = By.className("responsive-iframe");

    // -------------------- Accessors --------------------

    private WebElement getSuccessToast() {
        return getRootDriver().findElement(successToastBy);
    }

    private WebElement getErrorToast() {
        return getRootDriver().findElement(errorToastBy);
    }

    private WebElement getPageLoader() {
        return getRootDriver().findElement(pageLoaderBy);
    }

    private List<WebElement> getOrderItemsRows() {
        return getRootDriver().findElements(orderItemsRowsBy);
    }

    private List<WebElement> getOrderCells(WebElement orderRow) {
        return orderRow.findElements(orderItemsColumnsBy);
    }

    private WebElement getPrintErrorMessageBy() {
        return getRootDriver().findElement(printErrorMessageBy);
    }

    // -------------------- Actions --------------------

    /**
     * Verifies if the success toast is currently visible.
     * Useful for confirming that an action succeeded.
     */
    public boolean isSuccessToastDisplayed() {
        try {
            return getSuccessToast().isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Verifies if the error toast is currently visible.
     * Useful for detecting failed form submissions or other validations.
     */
    public boolean isErrorToastDisplayed() {
        try {
            return getErrorToast().isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks whether the page loader is still displayed (e.g., after navigation or filter search).
     * @return true if loader is shown and active (opacity = 1), false otherwise
     */
    public boolean isPageLoaderShown() {
        try {
            return getPageLoader().isDisplayed() &&
                    getPageLoader().getCssValue("opacity").equals("1");
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks whether the print iframe contains the status/error message.
     * This is mainly used after clicking "Print" or "Checklist".
     */
    public boolean isPrintErrorMessageDisplayed() {
        try {
            return getPrintErrorMessageBy().isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Returns the text content inside the print iframe message.
     * Example expected value: "Done. Total 1 pages loaded."
     */
    public String getPrintErrorMessageText() {
        return getPrintErrorMessageBy().getText();
    }

    /**
     * Parses the visible order rows in the current table view (e.g., print page, checklist) and
     * maps each row to an `Order.OrderItem` object using known column indexes.
     *
     * @return list of structured order items with parsed values
     */
    public List<Order.OrderItem> getRequestedItems() {
        List<Order.OrderItem> requestedItems = new ArrayList<>();
        for (WebElement row : getOrderItemsRows()) {
            List<WebElement> cellsList = getOrderCells(row);
            requestedItems.add(
                    new Order.OrderItem()
                            .setCategory(cellsList.get(OrderCells.CATEGORY.getIndex()).getText())
                            .setSupply(cellsList.get(OrderCells.SUPPLY.getIndex()).getText())
                            .setQuantityPerUnit(Integer.parseInt(cellsList.get(OrderCells.QUANTITY_PER_UNIT.getIndex()).getText()))
                            .setUnitTypes(List.of(cellsList.get(OrderCells.UNIT_TYPES.getIndex()).getText()))
                            .setUnitsNumbers(List.of(cellsList.get(OrderCells.UNITS.getIndex()).getText()))
                            .setTotal(Integer.parseInt(cellsList.get(OrderCells.TOTAL.getIndex()).getText()))
            );
        }
        return requestedItems;
    }

    /**
     * Switches the WebDriver context to the embedded print iframe.
     * Must be called before interacting with print-related content.
     */
    public void switchToPrintIframe() {
        explicitWait(10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(responsiveIframeBy));
    }

    /**
     * Enum defining index positions of each cell in the order item rows.
     * Used to accurately extract cell data without relying on hardcoded values.
     */
    public enum OrderCells {
        CATEGORY(0),
        SUPPLY(1),
        QUANTITY_PER_UNIT(2),
        UNIT_TYPES(3),
        UNITS(4),
        TOTAL(5);

        private final int index;

        OrderCells(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
