package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class EditUserTest {

    @Test
    public void User_ShouldNot_Edit_Other_Users_Info() {
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

            // Click on Users and wait for navigation
            page.getByText("Users").click();

            // Wait for URL to contain 'users'
            page.waitForURL("**/users**");
            assertTrue("URL should contain 'users'", page.url().contains("users"));

            // Wait for email to be present in the page content
            page.waitForSelector("text=" + expectedEmail);

            // Get updated page content after waiting for email to appear
            String pageContent = page.content();
            assertTrue("Expected email should be found", pageContent.contains(expectedEmail));

            // Find all email elements
            Locator emailElements = page.locator("text=/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}/");

            // Wait for all email elements to be visible
            emailElements.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            int emailCount = emailElements.count();

            for (int i = 0; i < emailCount; i++) {
                String foundEmail = emailElements.nth(i).textContent();
                assertEquals("Found unexpected email on the users page", expectedEmail, foundEmail);


            }
            // Verify user edit his own information
            String buttonText = page.locator(".button-container").textContent();
            assertTrue("Button should contain 'Edit'", buttonText.contains("Edit"));
            assertTrue("Button should contain 'Delete'", buttonText.contains("Delete"));

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