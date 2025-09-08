package nazeel.test_suites;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nazeel.base.TestBase;
import nazeel.pages.LoginPage;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.util.Set;

public class SaveSessionDataTest extends TestBase {

    private static final String STORAGE_FILE = "sessionData.json";

    @Test(priority = 1)
    public void saveSessionData() throws Exception {

        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("mai admin");
        loginPage.insertPassword("123456Mm&&");
        loginPage.insertAccessCode("");
        loginPage.clickLoginButton();
        Thread.sleep(9000);

        // Select Property
        loginPage.Select_One_Property_("P01558");
        Thread.sleep(6000);

        Gson gson = new Gson();
        JsonObject sessionData = new JsonObject();

        // --- Save Cookies ---
        Set<Cookie> cookies = getRootDriver().manage().getCookies();
        sessionData.add("cookies", gson.toJsonTree(cookies));

        // --- Save LocalStorage ---
        JsonObject localStorage = new JsonObject();
        JavascriptExecutor js = (JavascriptExecutor) getRootDriver();
        Long localStorageLength = (Long) js.executeScript("return localStorage.length;");
        for (int i = 0; i < localStorageLength; i++) {
            String key = (String) js.executeScript(String.format("return localStorage.key(%s);", i));
            String value = (String) js.executeScript(String.format("return localStorage.getItem('%s');", key));
            localStorage.addProperty(key, value);
        }
        sessionData.add("localStorage", localStorage);

        // --- Save SessionStorage ---
        JsonObject sessionStorage = new JsonObject();
        Long sessionStorageLength = (Long) js.executeScript("return sessionStorage.length;");
        for (int i = 0; i < sessionStorageLength; i++) {
            String key = (String) js.executeScript(String.format("return sessionStorage.key(%s);", i));
            String value = (String) js.executeScript(String.format("return sessionStorage.getItem('%s');", key));
            sessionStorage.addProperty(key, value);
        }
        sessionData.add("sessionStorage", sessionStorage);

        // --- Save everything to file ---
        try (FileWriter file = new FileWriter(STORAGE_FILE)) {
            gson.toJson(sessionData, file);
        }

        System.out.println("✅ Session data saved successfully.");
    }
}

