package karunasagarlearning.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

public class CartPage extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(CartPage.class);
    WebDriver driver;

    public CartPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // Fix the selector - the cart items might have a different class
    @FindBy(css = ".cartSection h3")
    List<WebElement> productTitles;
    
    // Alternative selector if .cartSection doesn't work
    @FindBy(css = ".cart-items .item-info h3")
    List<WebElement> productTitlesAlt;
    
    @FindBy(css = ".totalRow .btn-primary")
    WebElement checkoutButton;
    
    @FindBy(css = ".subtotal .value")
    WebElement subtotalAmount;
    
    @FindBy(css = ".cart .remove")
    List<WebElement> removeButtons;
    
    @FindBy(css = ".cart-empty")
    WebElement emptyCartMessage;
    
    // Updated locators - try different selectors
    private final By cartSection = By.cssSelector(".cartSection");
    private final By cartSectionAlt = By.cssSelector(".cart-items");
    private final By cartTable = By.cssSelector(".table-bordered");
    private final By checkoutSection = By.cssSelector(".totalRow");

    public Boolean verifyProductIsDisplayed(String productName) {
        log.info("Verifying product in cart: {}", productName);
        try {
            // Try multiple selectors
            try {
                waitForVisibilityOfElementLocated(cartSection, 5);
            } catch (Exception e) {
                log.warn("Cart section not found with .cartSection, trying alternative");
                try {
                    waitForVisibilityOfElementLocated(cartTable, 5);
                } catch (Exception e2) {
                    log.warn("Cart table not found");
                }
            }
            
            waitForInvisibilityOf(spinner);
            
            // Try to get product titles
            List<WebElement> titles = productTitles;
            if (titles.isEmpty()) {
                // Try alternative selector
                titles = productTitlesAlt;
            }
            
            boolean isProductFound = titles.stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));
            
            if (isProductFound) {
                log.info("Product found in cart: {}", productName);
            } else {
                log.warn("Product not found in cart: {}", productName);
            }
            
            return isProductFound;
            
        } catch (Exception e) {
            log.error("Error verifying product in cart: {}", e.getMessage());
            return false;
        }
    }
    
    public List<String> getProductNamesInCart() {
        waitForVisibilityOfElementLocated(cartSection);
        return productTitles.stream()
            .map(WebElement::getText)
            .toList();
    }
    
    public int getCartItemCount() {
        try {
            waitForVisibilityOfElementLocated(cartSection, 5);
            return productTitles.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public boolean isCartNotEmpty() {
        try {
            waitForVisibilityOfElementLocated(cartSection, 5);
            return !productTitles.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isCartEmpty() {
        try {
            return !isCartNotEmpty();
        } catch (Exception e) {
            return true;
        }
    }
    
    public CheckOutPage proceedToCheckout() {
        log.info("Proceeding to checkout");
        try {
            if (!isCartNotEmpty()) {
                throw new RuntimeException("Cannot proceed to checkout - Cart is empty");
            }
            
            waitForVisibilityOfElementLocated(checkoutSection);
            waitForElementToBeClickable(checkoutButton);
            scrollToElement(checkoutButton);
            checkoutButton.click();
            waitForInvisibilityOf(spinner);
            
            CheckOutPage checkoutPage = new CheckOutPage(driver);
            log.info("Successfully navigated to Checkout page");
            return checkoutPage;
            
        } catch (Exception e) {
            log.error("Failed to proceed to checkout: {}", e.getMessage());
            throw new RuntimeException("Could not proceed to checkout", e);
        }
    }
    
    public void waitForCartToLoad() {
        try {
            waitForVisibilityOfElementLocated(cartSection, 10);
        } catch (Exception e) {
            log.warn("Cart page may not have loaded correctly");
        }
        waitForInvisibilityOf(spinner);
        waitForPageToLoad();
    }
}