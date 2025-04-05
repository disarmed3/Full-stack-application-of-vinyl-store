package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class LoginLogoutTest {

    private final String userEmail = "tasos@ctrlspace.dev";
    private final String userPassword= "123555";
    private final String adminEmail="csekas@ctrlspace.dev";
    private final String adminPassword="1234";
    private final String url="localhost:3000/login";
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setUp() {
        // Initialize browser and page
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();

        // Navigate to login page
        page.navigate(url);
    }

    @AfterEach
    public void tearDown() {
        // Close resources
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
    }

    /**
     * Helper method to perform login with given credentials
     */
    private void performLogin(String email, String password) {
        page.locator("#formLoginEmailInputField").fill(email);
        page.locator("#formLoginPasswordInputField").fill(password);
        page.getByText("Log in").click();
    }

    /**
     * Helper method to perform logout and verify redirection to login page
     */
    private void performLogoutAndVerify() {
        page.getByText("Logout").click();
        String currentUrl = page.url();
        assertTrue("URL should contain 'login'", currentUrl.contains("login"));
    }

    @Test
    public void Login_AsAdmin_shouldSucceed() {
        // Login as admin
        performLogin(adminEmail, adminPassword);

        // Verify admin has access to add product functionality
        String buttonText = page.locator(".add-product-button").textContent();
        assertTrue("Button should contain 'Add'", buttonText.contains("Add"));

        // Logout and verify redirection
        performLogoutAndVerify();
    }

    @Test
    public void Login_AsCostumer_shouldSucceed() {
        // Login as customer
        performLogin(userEmail, userPassword);

        // Verify customer does not have access to add product functionality
        boolean buttonExists = page.locator("text=Add a Product").count() > 0;
        assertFalse("Button 'Add a Product' should not exist on the page.", buttonExists);

        // Logout and verify redirection
        performLogoutAndVerify();
    }
}