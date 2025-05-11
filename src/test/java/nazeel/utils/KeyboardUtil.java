package nazeel.utils;

import nazeel.base.TestBase;
import org.openqa.selenium.WebElement;


public class KeyboardUtil {
    /**
     * Sends the given character sequence to the element one character at a time,
     * with a delay between each keystroke.
     *
     * @param element       the WebElement to send keys to
     * @param keys          the string to type character by character
     * @param delayInMillis delay in milliseconds between characters
     */
    public static void sendKeysWithInterval(WebElement element, String keys, int delayInMillis) {
        for (char c : keys.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            TestBase.sleep(delayInMillis); // assuming you want to reuse your sleep
        }
    }
}
