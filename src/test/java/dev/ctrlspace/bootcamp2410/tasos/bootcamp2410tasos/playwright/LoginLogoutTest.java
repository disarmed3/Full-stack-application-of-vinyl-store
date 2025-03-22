package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class LoginLogoutTest {

    @Test
    public void Login_AsAdmin_shouldSucceed() {

        Browser browser = null;
        Page page = null;
      try {
          browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

          page = browser.newPage();

          page.navigate("http://localhost:3000/login");

          page.locator("#formLoginEmailInputField").fill("csekas@ctrlspace.dev");

          page.locator("#formLoginPasswordInputField").fill("1234");

          page.getByText("Log in").click();

          String buttonText = page.locator(".add-product-button").textContent();
          assertTrue("Button should contain 'Add'", buttonText.contains("Add"));


          page.getByText("Logout").click();

          String currentUrl = page.url();
          assertTrue("URL should contain 'login'", currentUrl.contains("login"));

      }
      finally {
          page.close();

          browser.close();
      }


    }

    @Test
    public void Login_AsCostumer_shouldSucceed() {

        Browser browser = null;
        Page page = null;
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            page = browser.newPage();

            page.navigate("http://localhost:3000/login");

            page.locator("#formLoginEmailInputField").fill("tasos@ctrlspace.dev");

            page.locator("#formLoginPasswordInputField").fill("123555");

            page.getByText("Log in").click();

            // Check that the button with text "Add a Product" does not exist
            boolean buttonExists = page.locator("text=Add a Product").count() > 0;

            assertFalse("Button 'Add a Product' should not exist on the page.", buttonExists);

            page.getByText("Logout").click();

            String currentUrl = page.url();
            assertTrue("URL should contain 'login'", currentUrl.contains("login"));

        }
        finally {
            page.close();

            browser.close();
        }


    }
}
