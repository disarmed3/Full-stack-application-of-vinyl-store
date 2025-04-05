package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class NegativeLoginTest {
    private final String userEmail = "tasos@ctrlspace.dev";
    private final String userPassword= "123555";
    private final String url="localhost:3000/login";
    private Browser browser;
    private Page page;
    private String[] dialogMessage;
    private boolean[] dialogOccurred;

    @BeforeEach
    public void setUp() {
        // Initialize browser and page
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();

        // Navigate to login page
        page.navigate(url);

        // Initialize dialog tracking variables
        dialogMessage = new String[1];
        dialogOccurred = new boolean[1];

        // Set up dialog handler
        page.onDialog(dialog -> {
            dialogMessage[0] = dialog.message();
            dialogOccurred[0] = true;
            // Store the message immediately then accept
            System.out.println("Dialog detected: " + dialog.message());
            dialog.accept();
        });
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
     * Helper method to attempt login and verify error dialog
     */
    private void attemptLoginAndVerifyError() {
        try {
            page.getByText("Log in").click();

            // Wait a short time to give the dialog a chance to appear
            page.waitForTimeout(2000);

            // If we get here without exception, check if dialog occurred
            if (dialogOccurred[0]) {
                assertTrue("Error message should be displayed for invalid login.",
                        dialogMessage[0].contains("Failed to log in. Please check your credentials."));
            } else {
                fail("Expected dialog did not appear");
            }
        } catch (Exception e) {
            // If we encountered an exception, check if it was after dialog was shown
            if (dialogOccurred[0]) {
                assertTrue("Error message should be displayed for invalid login.",
                        dialogMessage[0].contains("Failed to log in. Please check your credentials."));
            } else {
                throw e; // Re-throw if no dialog was shown
            }
        }
    }

    @Test
    public void Login_WithWrongUsername_shouldNotSucceed() {
        // Reset dialog occurred flag before each test
        dialogOccurred[0] = false;

        // Fill in wrong username but some password
        page.locator("#formLoginEmailInputField").fill("wrong");
        page.locator("#formLoginPasswordInputField").fill(userPassword);

        // Attempt login and verify error
        attemptLoginAndVerifyError();
    }

    @Test
    public void Login_WithWrongPassword_shouldNotSucceed() {
        // Reset dialog occurred flag before each test
        dialogOccurred[0] = false;

        // Fill in correct username but wrong password
        page.locator("#formLoginEmailInputField").fill(userEmail);
        page.locator("#formLoginPasswordInputField").fill("wrong");

        // Attempt login and verify error
        attemptLoginAndVerifyError();
    }

    @Test
    public void Login_WithoutCredentials_shouldNotSucceed() {
        // Reset dialog occurred flag before each test
        dialogOccurred[0] = false;

        // Don't fill in any credentials

        // Attempt login and verify error
        attemptLoginAndVerifyError();
    }
}