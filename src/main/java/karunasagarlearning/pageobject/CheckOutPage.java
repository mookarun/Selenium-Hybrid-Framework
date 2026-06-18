package karunasagarlearning.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

public class CheckOutPage extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(CheckOutPage.class);
    WebDriver driver;

    public CheckOutPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @FindBy(css = ".form-group .text-validated")
    WebElement countryInputField;
    
    @FindBy(css = ".ta-results .ta-item")
    List<WebElement> countryList;
    
    @FindBy(css = ".action__submit")
    WebElement placeOrderButton;
    
    @FindBy(css = ".hero-primary")
    WebElement thankYouMessage;
    
    @FindBy(css = ".ta-backdrop")
    WebElement backdrop;
    
    private final By dropdownResults = By.cssSelector(".ta-results");

    public void enterCountry(String countryName) {
        log.info("Entering country: {}", countryName);
        try {
            waitForVisibilityOf(countryInputField);
            countryInputField.clear();
            countryInputField.sendKeys(countryName);
            Thread.sleep(2000); // Wait for dropdown to appear
            log.debug("Country name entered: {}", countryName);
        } catch (Exception e) {
            log.error("Failed to enter country: {}", e.getMessage());
            throw new RuntimeException("Could not enter country name", e);
        }
    }
  
    
    public boolean selectCountry(String countryName) {
        log.info("Selecting country: {}", countryName);
        try {
            // Wait for dropdown
            Thread.sleep(2000);
            
            // Try multiple ways to select country
            boolean selected = false;
            
            // Method 1: Click using JavaScript
            try {
                String script = "const items = document.querySelectorAll('.ta-results .ta-item'); " +
                                "for(let item of items) { " +
                                "  if(item.innerText.trim().toLowerCase() === '" + countryName.toLowerCase() + "') { " +
                                "    item.scrollIntoView({behavior: 'smooth', block: 'center'}); " +
                                "    setTimeout(() => item.click(), 500); " +
                                "    return true; " +
                                "  } " +
                                "} " +
                                "return false;";
                
                selected = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
                Thread.sleep(1000);
                
                if (selected) {
                    log.info("Country selected via JavaScript: {}", countryName);
                    return true;
                }
            } catch (Exception e) {
                log.warn("JavaScript selection failed: {}", e.getMessage());
            }
            
            // Method 2: Regular click with scroll
            if (!selected) {
                WebElement targetCountry = countryList.stream()
                    .filter(country -> country.getText().equalsIgnoreCase(countryName))
                    .findFirst()
                    .orElse(null);
                
                if (targetCountry != null) {
                    scrollToElement(targetCountry);
                    Thread.sleep(500);
                    targetCountry.click();
                    selected = true;
                    log.info("Country selected via normal click: {}", countryName);
                }
            }
            
            if (!selected) {
                log.warn("Country not found: {}", countryName);
            }
            
            return selected;
            
        } catch (Exception e) {
            log.error("Error selecting country: {}", e.getMessage());
            return false;
        }
    }
    
    
    
    public void setCountry(String countryName) {
        enterCountry(countryName.substring(0, 3));
        selectCountry(countryName);
    }
    
    public List<String> getAvailableCountries() {
        waitForVisibilityOfElementLocated(dropdownResults);
        return countryList.stream()
            .map(WebElement::getText)
            .toList();
    }
    
    public String placeOrder() {
        log.info("Placing order");
        try {
            if (countryInputField.getAttribute("value").isEmpty()) {
                throw new RuntimeException("Country not selected before placing order");
            }
            
            waitForElementToBeClickable(placeOrderButton);
            scrollToElement(placeOrderButton);
            Thread.sleep(500);
            clickElementWithJS(placeOrderButton);
            log.debug("Place order button clicked");
            
            waitForInvisibilityOf(backdrop);
            waitForVisibilityOf(thankYouMessage);
            String confirmationMessage = thankYouMessage.getText();
            log.info("Order placed successfully. Message: {}", confirmationMessage);
            
            return confirmationMessage;
            
        } catch (Exception e) {
            log.error("Failed to place order: {}", e.getMessage());
            throw new RuntimeException("Could not place order", e);
        }
    }
    
    public String placeOrderWithCountry(String countryName) {
        setCountry(countryName);
        return placeOrder();
    }
    
    public String getOrderConfirmationMessage() {
        try {
            waitForVisibilityOf(thankYouMessage);
            return thankYouMessage.getText();
        } catch (Exception e) {
            log.warn("Order confirmation message not found");
            return null;
        }
    }
    
    public boolean isOrderConfirmed() {
        try {
            String message = getOrderConfirmationMessage();
            boolean isConfirmed = message != null && !message.isEmpty();
            log.info("Order confirmation status: {}", isConfirmed);
            return isConfirmed;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean verifyConfirmationMessage(String expectedMessage) {
        String actualMessage = getOrderConfirmationMessage();
        boolean matches = actualMessage != null && actualMessage.equalsIgnoreCase(expectedMessage);
        if (!matches) {
            log.warn("Confirmation message mismatch. Expected: '{}', Actual: '{}'", 
                     expectedMessage, actualMessage);
        }
        return matches;
    }
    
    public String getSelectedCountry() {
        return countryInputField.getAttribute("value");
    }
    
    public void clearCountryField() {
        countryInputField.clear();
    }
    
    public boolean isPlaceOrderButtonEnabled() {
        return placeOrderButton.isEnabled();
    }
    
    public void waitForCheckoutPageToLoad() {
        log.debug("Waiting for Checkout page to load");
        waitForVisibilityOf(countryInputField);
        waitForVisibilityOf(placeOrderButton);
        waitForPageToLoad();
    }
}