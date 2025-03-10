package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

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

          //PlaywrightAssertions.assertThat(page.locator(".add-product-button")).containsText("Add");

          String buttonText = page.locator(".add-product-button").textContent();
          assertTrue("Button should contain 'Add'", buttonText.contains("Add"));


          page.getByText("Logout").click();

          //PlaywrightAssertions.assertThat(page).hasURL(Pattern.compile("login"));

          String currentUrl = page.url();
          assertTrue("URL should contain 'login'", currentUrl.contains("login"));

      }
      finally {
          page.close();

          browser.close();
      }


    }


}
