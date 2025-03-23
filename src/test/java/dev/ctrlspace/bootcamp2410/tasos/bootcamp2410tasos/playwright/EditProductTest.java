package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class EditProductTest {

    @Test
    public void User_ShouldNot_Edit_Products() {
        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();
            String expectedEmail = "tasos@ctrlspace.dev";

            // Navigate to login page
            page.navigate("http://localhost:3000/login");

            // Fill in login credentials
            page.locator("#formLoginEmailInputField").fill(expectedEmail);
            page.locator("#formLoginPasswordInputField").fill("123555");

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
}
