package nazeel.base;

import org.openqa.selenium.*;

import static nazeel.base.TestBase.getRootDriver;

public class PageBase {
    private final By successToastBy = By.className("toast-success");
    private final By errorToastBy = By.className("toast-error");
    private final By pageLoaderBy = By.className("page-loading");
    private static int appearanceCount = 0;

    private WebElement getSuccessToast() {
        return getRootDriver().findElement(successToastBy);
    }

    private WebElement getErrorToast() {
        return getRootDriver().findElement(errorToastBy);
    }

    private WebElement getPageLoader() {
        return getRootDriver().findElement(pageLoaderBy);
    }

    public boolean isSuccessToastDisplayed() {
        return getSuccessToast().isDisplayed();
    }

    public boolean isErrorToastDisplayed() {
        System.out.println(++appearanceCount + " " + getErrorToast().isDisplayed());
        return getErrorToast().isDisplayed();
    }

    public boolean isPageLoaderShown() {
        try {
            return getPageLoader().isDisplayed() && getPageLoader().getCssValue("opacity").equals("1");
        }catch (NoSuchElementException | StaleElementReferenceException e){
            return false;
        }
    }
}
