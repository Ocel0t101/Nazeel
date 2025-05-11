package nazeel.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.Actions;

import java.util.Objects;

import static nazeel.base.TestBase.getRootDriver;
import static nazeel.base.TestBase.sleep;

public class ScrollUtil {

    /**
     * Exception indicating that the scroll limit has been reached (bottom or top of the page).
     */
    public static class ScrollLimitReachedException extends RuntimeException {
        public ScrollLimitReachedException(String message) {
            super(message);
        }
    }

    /**
     * Scrolls down the page by sending PAGE_DOWN key press.
     *
     * @throws ScrollLimitReachedException if page bottom is already reached
     */
    public static void scrollDownPage() throws WebDriverException, ScrollLimitReachedException {
        long oldScrollPosition = getScrollPosition();
        Actions actions = new Actions(getRootDriver());
        actions.sendKeys(Keys.PAGE_DOWN).perform();

        sleep(500); // wait a bit after scrolling

        long newScrollPosition = getScrollPosition();
        if (newScrollPosition == oldScrollPosition) {
            throw new ScrollLimitReachedException("⚠️ Page bottom reached, cannot scroll down further.");
        }
    }

    /**
     * Scrolls up the page by sending PAGE_UP key press.
     *
     * @throws WebDriverException          if the driver is not able to perform the action
     * @throws ScrollLimitReachedException if page top is already reached
     */
    public static void scrollUpPage() throws WebDriverException, ScrollLimitReachedException {
        long oldScrollPosition = getScrollPosition();
        Actions actions = new Actions(getRootDriver());
        actions.sendKeys(Keys.PAGE_UP).perform();

        sleep(500); // wait a bit after scrolling

        long newScrollPosition = getScrollPosition();
        if (newScrollPosition == oldScrollPosition) {
            throw new ScrollLimitReachedException("⚠️ Page top reached, cannot scroll up further.");
        }
    }

    /**
     * Scrolls down a little using ARROW_DOWN key.
     */
    public static void scrollDownLittle() throws WebDriverException, ScrollLimitReachedException {
        long oldScrollPosition = getScrollPosition();
        Actions actions = new Actions(getRootDriver());
        actions.sendKeys(Keys.ARROW_DOWN).perform();

        sleep(300);

        long newScrollPosition = getScrollPosition();
        if (newScrollPosition == oldScrollPosition) {
            throw new ScrollLimitReachedException("⚠️ Page bottom reached (small scroll), cannot scroll down further.");
        }
    }

    /**
     * Scrolls up a little using ARROW_UP key.
     */
    public static void scrollUpLittle() throws WebDriverException, ScrollLimitReachedException {
        long oldScrollPosition = getScrollPosition();
        Actions actions = new Actions(getRootDriver());
        actions.sendKeys(Keys.ARROW_UP).perform();

        sleep(300);

        long newScrollPosition = getScrollPosition();
        if (newScrollPosition == oldScrollPosition) {
            throw new ScrollLimitReachedException("⚠️ Page top reached (small scroll), cannot scroll up further.");
        }
    }

    /**
     * Helper to get current scroll Y position.
     */
    private static long getScrollPosition() {
        JavascriptExecutor js = (JavascriptExecutor) getRootDriver();
        return Objects.requireNonNull((Long) js.executeScript("return window.scrollY;"));
    }
}
