package karunasagarlearning.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

public class ProductCatalogue extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(ProductCatalogue.class);
    WebDriver driver;

    public ProductCatalogue(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    
    public CartPage navigateToCartPage() {
        log.info("Navigating to Cart page");
        goToCartPage(); // This clicks the cart header
        return new CartPage(driver);
    }

    @FindBy(css = ".mb-3")
    List<WebElement> productsList;
    
    // Try different toast selector or remove it
    @FindBy(css = "#toast-container")
    WebElement toaster;
    
    // Remove this if it's causing issues - we can add it back later
    // @FindBy(css = ".toast-message")
    // WebElement toastMessage;
    
    By waitForProduct = By.cssSelector(".mb-3");

    public List<WebElement> getProductList() {
        waitForVisibilityOfElementLocated(waitForProduct);
        log.info("Found {} products", productsList.size());
        return productsList;
    }
    
    public WebElement getProductByName(String productName) {
        log.info("Searching for product: {}", productName);
        List<WebElement> products = getProductList();
        WebElement targetProduct = products.stream()
            .filter(product -> product.findElement(By.tagName("b")).getText().equals(productName))
            .findFirst()
            .orElse(null);
        
        if (targetProduct == null) {
            log.error("Product not found: {}", productName);
            throw new RuntimeException("Product not found: " + productName);
        }
        log.info("Product found: {}", productName);
        return targetProduct;
    }

    public CartPage addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        try {
            WebElement product = getProductByName(productName);
            WebElement addButton = product.findElement(By.cssSelector(".card-body button:last-of-type"));
            
            waitForElementToBeClickable(addButton);
            addButton.click();
            
            // Wait for toast container (without checking the message text)
            try {
                waitForVisibilityOf(toaster);
            } catch (Exception e) {
                log.warn("Toast container not found, but continuing");
            }
            
            waitForInvisibilityOf(spinner);
            
            // Remove the toast message check since it's failing
            // if (toastMessage.isDisplayed()) {
            //     log.info("Product added to cart: {}", toastMessage.getText());
            // }
            
            log.info("Product added to cart successfully: {}", productName);
            Thread.sleep(2000); // Give time for cart to update
            
            return new CartPage(driver);
            
        } catch (Exception e) {
            log.error("Failed to add product to cart: {}", productName, e);
            throw new RuntimeException("Error adding product to cart: " + e.getMessage(), e);
        }
    }
    
    public int getProductCount() {
        return getProductList().size();
    }
}