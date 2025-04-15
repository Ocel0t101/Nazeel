package nazeel.utils;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Custom WebDriver wrapper that introduces a delay after every WebDriver operation.
 * Useful for debugging, observing UI behavior, or simulating slow users.
 *
 * Implements WebDriver, Interactive, JavascriptExecutor, and TakesScreenshot interfaces
 * to ensure full compatibility with Selenium operations.
 */
public class DelayedWebDriver implements WebDriver, Interactive, JavascriptExecutor, TakesScreenshot {
    private final WebDriver driver;
    private final long intervalInMillis;

    /**
     * Constructor to initialize the wrapped WebDriver and delay interval.
     *
     * @param driver            The actual WebDriver to wrap.
     * @param intervalInMillis  Delay duration in milliseconds between actions.
     */
    public DelayedWebDriver(WebDriver driver, long intervalInMillis) {
        this.driver = driver;
        this.intervalInMillis = intervalInMillis;
    }

    /**
     * Helper method to apply the configured delay.
     */
    private void delay() {
        try {
            Thread.sleep(intervalInMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ----------- WebDriver Interface Implementation -----------

    @Override
    public void get(String url) {
        driver.get(url);
        delay();
    }

    @Override
    public WebElement findElement(By by) {
        delay();
        WebElement element = driver.findElement(by);
        return new DelayedWebElement(element, intervalInMillis);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    // ----------- Interactive Interface Implementation -----------

    @Override
    public void perform(Collection<Sequence> actions) {
        delay();
        ((Interactive) driver).perform(actions);
    }

    @Override
    public void resetInputState() {
        delay();
        ((Interactive) driver).resetInputState();
    }

    // ----------- JavascriptExecutor Interface Implementation -----------

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    // ----------- TakesScreenshot Interface Implementation -----------

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    /**
     * Inner class to wrap WebElement and add delay after interactions.
     */
    public static class DelayedWebElement implements WebElement {
        private final WebElement element;
        private final long delayInMillis;

        /**
         * Constructor to wrap a WebElement with a post-action delay.
         *
         * @param element         The actual WebElement to wrap.
         * @param delayInMillis   Delay duration after each action.
         */
        public DelayedWebElement(WebElement element, long delayInMillis) {
            this.element = element;
            this.delayInMillis = delayInMillis;
        }

        private void delay() {
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void click() {
            element.click();
            delay();
        }

        @Override
        public void sendKeys(CharSequence... keysToSend) {
            element.sendKeys(keysToSend);
            delay();
        }

        @Override
        public void clear() {
            element.clear();
            delay();
        }

        @Override
        public void submit() {
            element.submit();
            delay();
        }

        @Override
        public String getText() {
            return element.getText();
        }

        @Override
        public boolean isDisplayed() {
            return element.isDisplayed();
        }

        @Override
        public boolean isEnabled() {
            return element.isEnabled();
        }

        @Override
        public boolean isSelected() {
            return element.isSelected();
        }

        @Override
        public String getAttribute(String name) {
            return element.getAttribute(name);
        }

        @Override
        public String getCssValue(String propertyName) {
            return element.getCssValue(propertyName);
        }

        @Override
        public Rectangle getRect() {
            return element.getRect();
        }

        @Override
        public Dimension getSize() {
            return element.getSize();
        }

        @Override
        public Point getLocation() {
            return element.getLocation();
        }

        @Override
        public String getTagName() {
            return element.getTagName();
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> target) {
            return element.getScreenshotAs(target);
        }

        @Override
        public WebElement findElement(By by) {
            return element.findElement(by);
        }

        @Override
        public List<WebElement> findElements(By by) {
            return element.findElements(by);
        }

        @Override
        public boolean equals(Object obj) {
            return element.equals(obj);
        }

        @Override
        public int hashCode() {
            return element.hashCode();
        }
    }
}
