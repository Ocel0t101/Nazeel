package nazeel.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static nazeel.base.TestBase.sleep;

/**
 * Custom WebDriver wrapper that adds a fixed delay after WebDriver interactions.
 * Helps simulate user pacing or observe dynamic UI behavior during automation.
 */
public class DelayedWebDriver implements WebDriver, Interactive, JavascriptExecutor, TakesScreenshot {
    private final WebDriver driver;             // Original WebDriver instance
    private final long intervalInMillis;        // Delay duration after each operation (in ms)

    // Constructor to initialize with driver and delay interval
    public DelayedWebDriver(WebDriver driver, long intervalInMillis) {
        this.driver = driver;
        this.intervalInMillis = intervalInMillis;
    }

    // Central delay method using TestBase sleep
    private void delay() {
        sleep((int) intervalInMillis);
    }

    // ------------------ WebDriver Implementation ------------------

    @Override
    public void get(String url) {
        driver.get(url);   // Navigate to URL
        delay();           // Wait after operation
    }

    @Override
    public WebElement findElement(By by) {
        delay();  // Simulate delay before find
        WebElement element = driver.findElement(by);
        return new DelayedWebElement(element, intervalInMillis);  // Wrap in delayed element
    }

    @Override
    public List<WebElement> findElements(By by) {
        delay();  // Delay before search
        List<WebElement> elements = driver.findElements(by);
        List<WebElement> delayedElements = new ArrayList<>();
        for (WebElement element : elements) {
            delayedElements.add(new DelayedWebElement(element, intervalInMillis));
        }
        return delayedElements;
    }

    // Simple getters with no delay needed
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

    // Window/session management
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

    // ------------------ Interactive Interface ------------------

    @Override
    public void perform(Collection<Sequence> actions) {
        delay();  // Delay before performing interactions
        ((Interactive) driver).perform(actions);
    }

    @Override
    public void resetInputState() {
        delay();
        ((Interactive) driver).resetInputState();
    }

    // ------------------ JavascriptExecutor Interface ------------------

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    // ------------------ Screenshot Interface ------------------

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    // ******************************************************************
    // Inner class that wraps WebElement to introduce delay & retries
    // ******************************************************************

    public static class DelayedWebElement implements WebElement, WrapsElement {
        private final WebElement element;     // Original WebElement
        private final long delayInMillis;     // Delay after each action
        private final int maxRetries = 3;     // Retry count for flaky interactions

        public DelayedWebElement(WebElement element, long delayInMillis) {
            this.element = element;
            this.delayInMillis = delayInMillis;
        }

        private void delay() {
            sleep((int) delayInMillis);
        }

        // Wrap interaction with retry logic
        private void performWithRetry(Runnable action) {
            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                try {
                    action.run();
                    delay();
                    return;
                } catch (ElementNotInteractableException | NoSuchElementException e) {
                    if (attempt == maxRetries) throw e;
                    System.out.println("⚡ Retry (" + (attempt + 1) + "/" + maxRetries + ") after: " + e.getClass().getSimpleName());
                    sleep((int) delayInMillis);
                }
            }
        }

        // WebElement overrides with retry + delay
        @Override
        public void click() {
            performWithRetry(element::click);
        }

        @Override
        public void sendKeys(CharSequence... keysToSend) {
            performWithRetry(() -> element.sendKeys(keysToSend));
        }

        @Override
        public void clear() {
            performWithRetry(element::clear);
        }

        @Override
        public void submit() {
            element.submit();
            delay();
        }

        // Simple getters
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

        // Screenshot from element
        @Override
        public <X> X getScreenshotAs(OutputType<X> target) {
            return element.getScreenshotAs(target);
        }

        // Re-wrap nested elements with same delay logic
        @Override
        public WebElement findElement(By by) {
            delay();
            WebElement inner = element.findElement(by);
            return new DelayedWebElement(inner, delayInMillis);
        }

        @Override
        public List<WebElement> findElements(By by) {
            delay();
            List<WebElement> raw = element.findElements(by);
            List<WebElement> wrapped = new ArrayList<>();
            for (WebElement el : raw) wrapped.add(new DelayedWebElement(el, delayInMillis));
            return wrapped;
        }

        // Equality and identity
        @Override
        public boolean equals(Object obj) {
            return element.equals(obj);
        }

        @Override
        public int hashCode() {
            return element.hashCode();
        }

        // Get the raw WebElement (unwrapped)
        @Override
        public WebElement getWrappedElement() {
            return element;
        }
    }
}
