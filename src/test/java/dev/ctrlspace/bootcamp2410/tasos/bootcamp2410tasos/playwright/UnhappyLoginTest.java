package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class UnhappyLoginTest {

    @Test
    public void Login_WithWrongUsername_shouldNotSucceed() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            page = browser.newPage();

            page.navigate("http://localhost:3000/login");

            page.locator("#formLoginEmailInputField").fill("wrong");

            page.locator("#formLoginPasswordInputField").fill("1234");

            // Set up dialog handler
            final String[] dialogMessage = new String[1];
            final boolean[] dialogOccurred = {false};
            page.onDialog(dialog -> {
                dialogMessage[0] = dialog.message();
                dialogOccurred[0] = true;
                // Store the message immediately then accept
                System.out.println("Dialog detected: " + dialog.message());
                dialog.accept();
            });

            // Use try-catch to handle both success and failure cases
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
        } finally {
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
        }


    }

    @Test
    public void Login_WithWrongPassword_shouldNotSucceed() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            page = browser.newPage();

            page.navigate("http://localhost:3000/login");

            page.locator("#formLoginEmailInputField").fill("tasos@ctrlspace.dev");

            page.locator("#formLoginPasswordInputField").fill("1234");

            // Set up dialog handler
            final String[] dialogMessage = new String[1];
            final boolean[] dialogOccurred = {false};
            page.onDialog(dialog -> {
                dialogMessage[0] = dialog.message();
                dialogOccurred[0] = true;
                // Store the message immediately then accept
                System.out.println("Dialog detected: " + dialog.message());
                dialog.accept();
            });

            // Use try-catch to handle both success and failure cases
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
        } finally {
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
        }
    }

    @Test
    public void Login_WithoutCredentials_shouldNotSucceed() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            page = browser.newPage();

            page.navigate("http://localhost:3000/login");



            // Set up dialog handler
            final String[] dialogMessage = new String[1];
            final boolean[] dialogOccurred = {false};
            page.onDialog(dialog -> {
                dialogMessage[0] = dialog.message();
                dialogOccurred[0] = true;
                // Store the message immediately then accept
                System.out.println("Dialog detected: " + dialog.message());
                dialog.accept();
            });

            // Use try-catch to handle both success and failure cases
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
        } finally {
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
        }
    }
}
