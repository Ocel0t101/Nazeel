package nazeel.pages;

import nazeel.data_types.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static nazeel.base.TestBase.getRootDriver;

/**
 * Page Object Model class for the Login Page.
 * Encapsulates user interactions like entering credentials and selecting a property.
 */
public class LoginPage {

    // -------------------- Locators --------------------

    // Username input field
    private final By userNameField = By.id("usern");

    // Password input field
    private final By passwordField = By.id("pass");

    // Access code input field
    private final By accessCodeField = By.id("acc");

    // Login button that triggers authentication
    private final By loginButton = By.xpath("//button[contains(text(),'Login')]");

    // List of company property rows shown after login (for property selection)
    private final By companyProperties = By.cssSelector("tbody[role='presentation']>tr");

    // Shared static User object to store login credentials for use across the test flow
    private final static User loginUser = new User();

    // -------------------- Actions --------------------

    /**
     * Fills in the username field and stores it in the shared loginUser object.
     *
     * @param username the login username
     * @return this LoginPage instance for method chaining
     */
    public LoginPage insertUsername(String username) {
        getRootDriver().findElement(userNameField).sendKeys(username);
        loginUser.setUsername(username);
        return this;
    }

    /**
     * Fills in the password field and stores it in the shared loginUser object.
     *
     * @param password the login password
     * @return this LoginPage instance for method chaining
     */
    public LoginPage insertPassword(String password) {
        getRootDriver().findElement(passwordField).sendKeys(password);
        loginUser.setPassword(password);
        return this;
    }

    /**
     * Fills in the access code field and stores it in the shared loginUser object.
     *
     * @param accessCode the access code provided for the account
     * @return this LoginPage instance for method chaining
     */
    public LoginPage insertAccessCode(String accessCode) {
        getRootDriver().findElement(accessCodeField).sendKeys(accessCode);
        loginUser.setAccessCode(accessCode);
        return this;
    }

    /**
     * Retrieves all selectable properties shown after login (for multi-property companies).
     *
     * @return list of WebElements representing property rows
     */
    private List<WebElement> getCompanyProperties() {
        return getRootDriver().findElements(companyProperties);
    }

    /**
     * Clicks the login button to authenticate and redirect to the dashboard.
     *
     * @return DashboardPage instance once login is successful
     */
    public DashboardPage clickLoginButton() {
        getRootDriver().findElement(loginButton).click();
        return new DashboardPage();
    }

    /**
     * Selects a specific property by its index from the available list after login.
     * This is used when the user has access to multiple properties.
     *
     * @param index index of the property row to click
     */
    public void selectPropertyByIndex(int index) {
        getCompanyProperties().get(index).click();
    }

    /**
     * Retrieves the User object containing the last used login credentials.
     *
     * @return a User object with username, password, and access code
     */
    public User getLoginUser() {
        return loginUser;
    }
}
