package karunasagarlearning.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

public class LandingPage extends AbstractComponent {
    
    private static final Logger log = LoggerFactory.getLogger(LandingPage.class);
    WebDriver driver;
    
    public LandingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    
    @FindBy(id = "userEmail")
    WebElement userEmail;
    
    @FindBy(id = "userPassword")
    WebElement userPassword;
    
    @FindBy(id = "login")
    WebElement loginSubmit;
    
    @FindBy(css = ".toast-message")
    WebElement errorMessageText;
    
    @FindBy(css = ".forgot-password-link")
    WebElement forgotPasswordLink;
    
    public void goToURL() {
        log.info("Navigating to URL: https://rahulshettyacademy.com/client");
        driver.get("https://rahulshettyacademy.com/client");
        waitForVisibilityOf(userEmail);
        log.info("Successfully navigated to: {}", driver.getCurrentUrl());
    }
    
    public ProductCatalogue login(String email, String password) {
        log.info("Logging in with email: {}", email);
        waitForVisibilityOf(userEmail);
        userEmail.sendKeys(email);
        userPassword.sendKeys(password);
        loginSubmit.click();
        log.info("Login button clicked successfully");
        
        ProductCatalogue productCatalogue = new ProductCatalogue(driver);
        return productCatalogue;
    }
    
    public String getErrorMessage() {
        try {
            // Wait a moment for error to appear
            Thread.sleep(2000);
            
            // Try different selectors for error message
            List<WebElement> errorMessages = driver.findElements(By.cssSelector(".toast-message, .alert, .error, .toast-error, .ng-trigger"));
            
            if (!errorMessages.isEmpty()) {
                String errorMsg = errorMessages.get(0).getText();
                log.info("Error message displayed: {}", errorMsg);
                return errorMsg;
            }
            
            log.warn("No error message found");
            return "";
        } catch (Exception e) {
            log.warn("Error while getting error message: {}", e.getMessage());
            return "";
        }
    }
    
    public void clearLoginFields() {
        log.debug("Clearing login fields");
        userEmail.clear();
        userPassword.clear();
    }
    
    public boolean isLoginButtonEnabled() {
        return loginSubmit.isEnabled();
    }
    
    public void goToForgotPassword() {
        log.info("Navigating to Forgot Password page");
        waitForElementToBeClickable(forgotPasswordLink);
        forgotPasswordLink.click();
    }
}