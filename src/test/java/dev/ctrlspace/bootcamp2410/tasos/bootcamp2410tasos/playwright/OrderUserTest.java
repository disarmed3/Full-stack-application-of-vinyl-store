package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.regex.Pattern;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class OrderUserTest {
    @Test
    public void User_Should_Order_Products() {
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

            // Check initial cart count - extract number from parentheses
            String initialCartCountText = page.locator(".jsx-2bc262920be81ef.cart-count").textContent();
            String initialCartCount = initialCartCountText.replaceAll("[^0-9]", "");
            assertTrue("Initial cart count should be 0", initialCartCount.equals("0"));

            page.getByText("The Verve - Urban Hymns (1997)").click();
            page.waitForURL("**/products/SKU-533");
            assertTrue("URL should contain 'SKU-533'", page.url().contains("SKU-533"));

            // Store stock and price values
            String productInfo1 = page.locator(".product-card").textContent();
            String stockValue1 = "";
            String priceValue1 = "";

            // Extract stock value using regex
            var stockPattern1 = Pattern.compile("Stock:\\s*(\\d+)");
            var stockMatcher1 = stockPattern1.matcher(productInfo1);
            if (stockMatcher1.find()) {
                stockValue1 = stockMatcher1.group(1);
                System.out.println("Stock value: " + stockValue1);
            }

            // Extract price value using regex
            var pricePattern1 = Pattern.compile("Price:\\s*\\$(\\d+\\.?\\d*)");
            var priceMatcher1 = pricePattern1.matcher(productInfo1);
            if (priceMatcher1.find()) {
                priceValue1 = priceMatcher1.group(1);
                System.out.println("Price value: $" + priceValue1);
            }

            // Set up dialog handler before clicking the button
            page.onDialog(dialog -> {
                assertTrue("Dialog message should be 'Product added to cart!'",
                        dialog.message().equals("Product added to cart!"));
                dialog.accept();
            });

            // Click add to cart button
            page.locator(".add-to-cart-button").click();

            // Wait for cart count to update and extract number
            page.waitForTimeout(1000); // Give a small timeout for the cart to update
            String updatedCartCountText = page.locator(".jsx-2bc262920be81ef.cart-count").textContent();
            String updatedCartCount = updatedCartCountText.replaceAll("[^0-9]", "");

            assertTrue("Cart count should be updated to 1", updatedCartCount.equals("1"));

            page.locator(".back-button").click();

            // Wait for URL to contain 'products'
            page.waitForURL("**/products**");
            assertTrue("URL should contain 'products'", page.url().contains("products"));

            page.getByText("Radiohead – OK Computer (1997)").click();
            page.waitForURL("**/products/SKU-000870");
            assertTrue("URL should contain 'SKU-000870'", page.url().contains("SKU-000870"));

            // Store stock and price values
            String productInfo2 = page.locator(".product-card").textContent();
            String stockValue2 = "";
            String priceValue2 = "";

            // Extract stock value using regex
            var stockPattern2 = Pattern.compile("Stock:\\s*(\\d+)");
            var stockMatcher2 = stockPattern2.matcher(productInfo2);
            if (stockMatcher2.find()) {
                stockValue2 = stockMatcher2.group(1);
                System.out.println("Stock value: " + stockValue2);
            }

            // Extract price value using regex
            var pricePattern = Pattern.compile("Price:\\s*\\$(\\d+\\.?\\d*)");
            var priceMatcher = pricePattern.matcher(productInfo2);
            if (priceMatcher.find()) {
                priceValue2 = priceMatcher.group(1);
                System.out.println("Price value: $" + priceValue2);
            }

            // Click add to cart button
            page.locator(".add-to-cart-button").click();

            // Wait for cart count to update and extract number
            page.waitForTimeout(1000); // Give a small timeout for the cart to update
            String finalCartCountText = page.locator(".jsx-2bc262920be81ef.cart-count").textContent();
            String finalCartCount = finalCartCountText.replaceAll("[^0-9]", "");

            assertTrue("Cart count should be updated to 2", finalCartCount.equals("2"));

            page.getByText("Shopping Cart").click();

            // Wait for URL to contain 'products'
            page.waitForURL("**/cart**");
            assertTrue("URL should contain 'cart'", page.url().contains("cart"));

            // Wait for the cart page to fully load with a more specific selector
            page.waitForSelector("text=Total Price:");

            // Get the text that contains "Total Price: $X.XX"
            String displayedTotalText = page.locator("text=Total Price:").textContent();

            // Extract just the number from the total price text using regex
            var totalPricePattern = Pattern.compile("Total Price:\\s*\\$(\\d+\\.?\\d*)");
            var totalPriceMatcher = totalPricePattern.matcher(displayedTotalText);
            String displayedTotalPrice = "";

            if (totalPriceMatcher.find()) {
                displayedTotalPrice = totalPriceMatcher.group(1);
                System.out.println("Displayed total price: $" + displayedTotalPrice);
            }

            // Calculate the expected total from the two items
            // Remove dollar signs if they exist in the stored price values
            String cleanPrice1 = priceValue1.replace("$", "").trim();
            String cleanPrice2 = priceValue2.replace("$", "").trim();

            double price1 = Double.parseDouble(cleanPrice1);
            double price2 = Double.parseDouble(cleanPrice2);
            double expectedTotal = price1 + price2;

            // Format expected total with period as decimal separator (US locale)
            // This matches the format on the webpage (60.00)
            java.util.Locale usLocale = java.util.Locale.US;
            java.text.NumberFormat formatter = java.text.NumberFormat.getNumberInstance(usLocale);
            if (formatter instanceof java.text.DecimalFormat) {
                ((java.text.DecimalFormat) formatter).applyPattern("0.00");
            }
            String expectedTotalFormatted = formatter.format(expectedTotal);

            // Compare the expected and displayed totals
            assertTrue("Total price should be the sum of the two items: $" + expectedTotalFormatted,
                    displayedTotalPrice.equals(expectedTotalFormatted));


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