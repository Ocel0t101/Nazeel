package nazeel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static nazeel.TestBase.getRootDriver;

public class GuestSuppliesPage {
    private final By categoryDropboxBy = By.cssSelector("input[placeholder='Category']");
    private final By categoryOptionsBy = By.cssSelector("li[role='option']");
    private final static String URL = "https://staging.nazeel.net:9002/guest-supplies/create-supplies-order";

    private WebElement getCategoryDropbox(){
        return getRootDriver().findElement(categoryDropboxBy);
    }

    private WebElement getCategoryOption(int index){
        return getRootDriver().findElements(categoryOptionsBy).get(index);
    }

    public void clickButton(){

    }


}
