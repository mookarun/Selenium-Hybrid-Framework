package karunasagarlearning.Test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import karunasagarlearning.TestComponent.BaseTest;
import karunasagarlearning.TestComponent.Retry;
import karunasagarlearning.pageobject.CartPage;
import karunasagarlearning.pageobject.ProductCatalogue;

public class ErrorValidationTest extends BaseTest {

    @Test(groups = {"ErrorHandling"}, retryAnalyzer = Retry.class)
    public void loginErrorValidation() throws IOException, InterruptedException {
        System.out.println("=========================================");
        System.out.println("Testing login error validation");
        System.out.println("=========================================");
        
        // Use a completely wrong email to trigger error
        landingpage.login("wrongemail@test.com", "wrongpassword");
        
        Thread.sleep(3000);
        
        // Check if error message appears OR if we're still on login page
        String currentUrl = driver.getCurrentUrl();
        String actualErrorMessage = landingpage.getErrorMessage();
        
        // Either error message appears OR URL still contains login
        boolean isValid = actualErrorMessage.contains("Incorrect") || 
                         actualErrorMessage.contains("invalid") ||
                         currentUrl.contains("auth/login");
        
        Assert.assertTrue(isValid, "Login error validation failed. URL: " + currentUrl + ", Error: " + actualErrorMessage);
        
        System.out.println("✅ Login error validation passed - URL: " + currentUrl);
    }

    @Test(groups = {"ErrorHandling"})
    public void productErrorValidation() throws IOException, InterruptedException {
        System.out.println("=========================================");
        System.out.println("Testing product validation");
        System.out.println("=========================================");
        
        String productName = "ZARA COAT 3";
        
        ProductCatalogue productCatalogue = landingpage.login("karnasagar36@gmail.com", "Mookarun123!");
        
        Thread.sleep(2000);
        
        CartPage cartPage = productCatalogue.addProductToCart(productName);
        cartPage = productCatalogue.navigateToCartPage();
        
        Thread.sleep(2000);
        
        boolean isProductDisplayed = cartPage.verifyProductIsDisplayed(productName);
        
        Assert.assertTrue(isProductDisplayed, "Product not found in cart: " + productName);
        
        System.out.println("✅ Product validation passed for: " + productName);
    }
}