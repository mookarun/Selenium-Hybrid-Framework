package karunasagarlearning.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.io.File; 
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import karunasagarlearning.TestComponent.BaseTest;
import karunasagarlearning.pageobject.CartPage;
import karunasagarlearning.pageobject.CheckOutPage;
import karunasagarlearning.pageobject.OrderPage;
import karunasagarlearning.pageobject.ProductCatalogue;
import karunasagarlearning.pageobject.RegistrationPage;
import karunasagarlearning.pageobject.LandingPage;

/**
 * SubmitOrderTest - End-to-End Test for User Registration and Order Submission
 *
 * This test class performs a complete user journey:
 * 1. Register a new user on the application
 * 2. Login with registered credentials
 * 3. Browse products and add item to cart
 * 4. Proceed to checkout
 * 5. Enter delivery address and place order
 *
 * The test uses data-driven approach with TestNG DataProvider to run multiple
 * test scenarios with different user data and product selections.
 *
 * Test Data Source: src/test/java/karunasagarlearning/data/PurchaseOrder.json
 *
 * Required Test Data Fields:
 * - firstName: User's first name
 * - lastName: User's last name
 * - email: User's email address
 * - password: User's password
 * - productName: Name of product to add to cart
 *
 * @author Karun Sagar
 * @version 1.0
 */
public class SubmitOrderTest extends BaseTest {

    /**
     * Country name to be used during checkout
     * This is hardcoded as "India" for all test executions
     * Can be made data-driven if needed in future
     */
    String countryName = "India";
    
    /**
     * Main Test Method: End-to-End Flow for Registration and Order Submission
     *
     * This test performs the following steps:
     * 1. REGISTRATION PHASE:
     *    - Navigate to registration page
     *    - Fill registration form with user data from DataProvider
     *    - Submit registration
     *    - Verify registration success message
     *    - Handle registration failure (if user already exists)
     *
     * 2. SHOPPING PHASE:
     *    - Login with registered credentials
     *    - Navigate to product catalog
     *    - Search and add product to cart
     *    - Verify product in cart
     *
     * 3. CHECKOUT PHASE:
     *    - Navigate to cart
     *    - Proceed to checkout
     *    - Enter delivery country
     *    - Place order
     *    - Verify order confirmation message
     *
     * @param input - HashMap containing test data from DataProvider with fields:
     *                firstName, lastName, email, password, productName
     * @throws IOException - If JSON data file is not found or readable
     * @throws InterruptedException - If thread sleep operations are interrupted
     */
    @Test(dataProvider = "getData", groups = {"Purchase"})
    public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {

        // Print test execution header
        System.out.println("=========================================");
        System.out.println("Starting submitOrder test for: " + input.get("productName"));
        System.out.println("=========================================");

        // ====================================================================
        // PHASE 1: USER REGISTRATION
        // ====================================================================

        System.out.println("Registering user with email: " + input.get("email"));
        try {
            // Create RegistrationPage instance
            RegistrationPage registrationPage = new RegistrationPage(driver);

            // Step 1: Navigate to registration page
            registrationPage.goToRegistrationURL();

            // Step 2: Wait for page to fully load
            registrationPage.waitForRegistrationPageToLoad();

            // Step 3: Fill registration form with user data
            registrationPage.registerUser(
                input.get("firstName"),      // First name from test data
                input.get("lastName"),       // Last name from test data
                input.get("email"),          // Email from test data
                input.get("password"),       // Password from test data
                input.get("password")        // Confirm password (same as password)
            );

            // Step 4: Wait for registration to complete
            registrationPage.waitForRegistrationToComplete();

            // Step 5: Get and log success message
            String registrationMessage = registrationPage.getSuccessMessage();
            System.out.println("✅ User registered successfully. Message: " + registrationMessage);

            // Step 6: Navigate back to login page
            registrationPage.goToLoginPage();
            Thread.sleep(1000); // Brief wait for page navigation

        } catch (Exception e) {
            // Handle registration failure gracefully
            // This can happen if user already exists - continue with login instead
            System.out.println("⚠️ Registration failed or user already exists: " + e.getMessage());
            System.out.println("Proceeding with login using existing credentials...");
            landingpage.goToURL();
        }

        // ====================================================================
        // PHASE 2: USER LOGIN AND PRODUCT BROWSING
        // ====================================================================

        // Step 1: Login with credentials from test data
        ProductCatalogue productCatalogue = landingpage.login(
            input.get("email"), 
            input.get("password")
        );
        
        // Step 2: Add product to cart
        CartPage cartPage = productCatalogue.addProductToCart(input.get("productName"));
        
        // Step 3: Navigate to cart page to verify product
        cartPage = productCatalogue.navigateToCartPage();
        
        // Step 4: Wait for cart page to load with items
        Thread.sleep(2000);
        
        // Step 5: Verify product is in cart
        boolean isProductInCart = cartPage.verifyProductIsDisplayed(input.get("productName"));
        Assert.assertTrue(isProductInCart, "Product not found in cart: " + input.get("productName"));
        
        // ====================================================================
        // PHASE 3: CHECKOUT AND ORDER SUBMISSION
        // ====================================================================

        // Step 1: Proceed to checkout from cart page
        CheckOutPage checkOutPage = cartPage.proceedToCheckout();
        
        // Step 2: Enter country for delivery (using first 3 characters)
        checkOutPage.enterCountry(countryName.substring(0, 3));
        Thread.sleep(1000); // Wait for dropdown to appear

        // Step 3: Select country from dropdown
        boolean isCountrySelected = checkOutPage.selectCountry(countryName);
        Assert.assertTrue(isCountrySelected, "Country not selected: " + countryName);
        
        // Step 4: Place order
        String confirmationMessage = checkOutPage.placeOrder();
        
        // Step 5: Verify order confirmation message
        Assert.assertTrue(
            confirmationMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."),
            "Order confirmation message mismatch"
        );
        
        // Print test completion message
        System.out.println("✅ Order placed successfully for: " + input.get("productName"));
    }
    
    /**
     * DataProvider Method: Reads test data from JSON file
     *
     * This method provides test data to the @Test method using TestNG's DataProvider
     * mechanism. Each row in the JSON array becomes one test execution.
     *
     * JSON File Location:
     * src/test/java/karunasagarlearning/data/PurchaseOrder.json
     *
     * Expected JSON Structure:
     * [
     *   {
     *     "firstName": "Karun",
     *     "lastName": "Sagar",
     *     "email": "user@example.com",
     *     "password": "Password123",
     *     "productName": "ZARA COAT 3"
     *   },
     *   {
     *     "firstName": "Karun",
     *     "lastName": "Sagar",
     *     "email": "user2@example.com",
     *     "password": "Password123",
     *     "productName": "ADIDAS ORIGINAL"
     *   }
     * ]
     *
     * The method converts JSON array to a 2D Object array for TestNG DataProvider
     *
     * @return Object[][] - 2D array of test data where each row is a HashMap
     * @throws IOException - If JSON file cannot be read or parsed
     */
    @DataProvider
    public Object[][] getData() throws IOException {
        // Construct the path to the JSON test data file
        String jsonPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "java" + File.separator + "karunasagarlearning" + File.separator 
                        + "data" + File.separator + "PurchaseOrder.json";
        
        // Read JSON file and convert to List of HashMaps
        List<HashMap<String, String>> data = getJsonDataToMap(jsonPath);
        
        // Convert List to 2D Object array for TestNG
        Object[][] testData = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i); // Each row contains one HashMap
        }

        return testData;
    }
}