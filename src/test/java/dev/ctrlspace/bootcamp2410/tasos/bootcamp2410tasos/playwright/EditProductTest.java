package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Consumer;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class EditProductTest {
    private final String userEmail = "tasos@ctrlspace.dev";
    private final String userPassword = "123555";
    private final String adminEmail = "csekas@ctrlspace.dev";
    private final String adminPassword = "1234";
    private final String url = "localhost:3000/login";

    @Test
    public void User_ShouldNot_Edit_Products() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();

            // Navigate to login page
            page.navigate(url);

            // Fill in login credentials
            page.locator("#formLoginEmailInputField").fill(userEmail);
            page.locator("#formLoginPasswordInputField").fill(userPassword);

            // Click login and wait for navigation to products page
            page.getByText("Log in").click();

            // Wait for URL to contain 'products'
            page.waitForURL("**/products**");
            assertTrue("URL should contain 'products'", page.url().contains("products"));

            // Wait for page to be fully loaded
            page.waitForLoadState();

            // Verify "Add a Product" button is not visible using proper selector syntax
            // Create two separate locators for different ways to find the button
            Locator addProductButtonByClass = page.locator(".add-product-button");
            Locator addProductButtonByText = page.locator("text=Add a Product");

            // Use a polling approach to ensure page is fully loaded
            try {
                // Set a short timeout (2 seconds) to check if button exists by class
                page.waitForSelector(".add-product-button",
                        new Page.WaitForSelectorOptions().setTimeout(2000));
                // If we get here, button was found
                assertFalse("'Add a Product' button should not be visible",
                        addProductButtonByClass.isVisible());
            } catch (TimeoutError e) {
                // Button wasn't found by class within timeout, which is expected
                System.out.println("'Add a Product' button not found by class, as expected");
            }

            // Also check by text
            try {
                // Set a short timeout to check if button exists by text
                page.waitForSelector("text=Add a Product",
                        new Page.WaitForSelectorOptions().setTimeout(2000));
                // If we get here, button was found
                assertFalse("'Add a Product' button should not be visible",
                        addProductButtonByText.isVisible());
            } catch (TimeoutError e) {
                // Button wasn't found by text within timeout, which is expected
                System.out.println("'Add a Product' button not found by text, as expected");
            }

            // Double-check using count method which doesn't throw exceptions
            int buttonCountByClass = addProductButtonByClass.count();
            int buttonCountByText = addProductButtonByText.count();
            assertEquals("'Add a Product' button should not exist (by class)", 0, buttonCountByClass);
            assertEquals("'Add a Product' button should not exist (by text)", 0, buttonCountByText);

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
    public void Admin_Should_Edit_Products() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();

            // Navigate to login page
            page.navigate(url);

            // Fill in login credentials
            page.locator("#formLoginEmailInputField").fill(adminEmail);
            page.locator("#formLoginPasswordInputField").fill(adminPassword);

            // Click login and wait for navigation to products page
            page.getByText("Log in").click();

            // Wait for URL to contain 'products'
            page.waitForURL("**/products**");
            assertTrue("URL should contain 'products'", page.url().contains("products"));

            // Wait for page to be fully loaded
            page.waitForLoadState();

            // Verify "Add a Product" button is visible using proper selector syntax
            page.getByText("Add a Product").click();

            page.waitForURL("**/products/new**");
            assertTrue("URL should contain 'new'", page.url().contains("new"));

            page.locator("input[placeholder='Name']").fill("Test Vinyl");
            page.locator("textarea[placeholder='Description']").fill("Test Description");
            page.locator("input[placeholder='Stock']").fill("10");
            page.locator("input[placeholder='Price']").fill("100");

            // Click and wait for navigation
            page.getByText("Save").click();
            page.waitForURL("**/products/SKU**");

            page.waitForSelector("text=Test Vinyl");
            assertTrue("Test Vinyl is not visible", page.getByText("Test Vinyl").isVisible());

            page.getByText("Edit").click();


            page.locator("input[placeholder='Price']").fill("1000");
            page.getByText("Save").click();

            page.waitForLoadState();


            page.waitForSelector("text=1000", new Page.WaitForSelectorOptions().setTimeout(5000));
            assertTrue("Price is not visible", page.getByText("1000").isVisible());

            Consumer<Dialog> dialogHandler = dialog -> {
                String message = dialog.message();
                System.out.println("Dialog message: " + message);

                if (message.equals("Are you sure?") || message.contains("deleted successfully")) {
                    // Accept any dialog that matches expected patterns
                    dialog.accept();
                } else {
                    // If unexpected dialog appears, fail with details
                    fail("Unexpected dialog message: " + message);
                }
            };

            // Register the handler
            page.onDialog(dialogHandler);

            page.getByText("Delete").click();
            page.waitForURL("**/products");
            assertTrue("URL should end in 'products'", page.url().endsWith("products"));

            Locator testVinylLocator = page.getByText("Test Vinyl");
            int count = testVinylLocator.count();
            assertEquals("Test Vinyl should be deleted", 0, count);

            page.offDialog(dialogHandler);

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