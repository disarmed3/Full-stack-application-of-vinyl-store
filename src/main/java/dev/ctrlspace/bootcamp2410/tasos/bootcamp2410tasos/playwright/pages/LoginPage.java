package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("http://localhost:3000/login");
    }

    public void login(String username, String password) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill(username);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in")).click();
    }

    public boolean isLoginSuccessful() {
        // Assuming successful login lands on a dashboard with id "dashboard"
        return page.isVisible("#dashboard");
    }

    public boolean isErrorMessageDisplayed() {
        // Assuming an error message element with id "error-message" is shown on failure
        return page.isVisible("#error-message");
    }
}

