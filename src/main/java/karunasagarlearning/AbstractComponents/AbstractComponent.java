package karunasagarlearning.AbstractComponents;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponent {
    
    private static final Logger log = LoggerFactory.getLogger(AbstractComponent.class);
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int EXPLICIT_TIMEOUT_LONG = 20;
    private static final int EXPLICIT_TIMEOUT_SHORT = 5;
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;
    protected WebDriverWait shortWait;
    protected By spinner = By.cssSelector(".ng-animating");
    protected By toaster = By.id("toast-container");

    public AbstractComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_TIMEOUT_LONG));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_TIMEOUT_SHORT));
        PageFactory.initElements(driver, this);
    }
    
    @FindBy(css = "[routerlink='/dashboard/myorders']")
    protected WebElement orderHeader;
    
    @FindBy(css = "[routerlink='/dashboard/cart']")
    protected WebElement cartHeader;
    
    @FindBy(css = ".toast-message")
    protected WebElement toastMessage;

    public void goToOrderPage() {
        try {
            log.info("Navigating to Orders page");
            waitForElementToBeClickable(orderHeader).click();
            log.debug("Successfully navigated to Orders page");
        } catch (Exception e) {
            log.error("Failed to navigate to Orders page: {}", e.getMessage());
            throw new RuntimeException("Could not navigate to Orders page", e);
        }
    }
    
    // Keep ONLY ONE of these - DELETE the duplicate
    public void goToCartPage() {
        try {
            log.info("Navigating to Cart page");
            waitForElementToBeClickable(cartHeader).click();
            log.debug("Successfully navigated to Cart page");
        } catch (Exception e) {
            log.error("Failed to navigate to Cart page: {}", e.getMessage());
            throw new RuntimeException("Could not navigate to Cart page", e);
        }
    }

    public void hoverOverElement(WebElement element) {
        try {
            log.debug("Hovering over element: {}", element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            log.error("Failed to hover over element: {}", e.getMessage());
            throw new RuntimeException("Could not hover over element", e);
        }
    }
    
    public void scrollToElement(WebElement element) {
        try {
            log.debug("Scrolling to element: {}", element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } catch (Exception e) {
            log.error("Failed to scroll to element: {}", e.getMessage());
        }
    }
    
    public void clickElementWithJS(WebElement element) {
        try {
            log.debug("Clicking element with JavaScript: {}", element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log.error("Failed to click element with JS: {}", e.getMessage());
            throw new RuntimeException("Could not click element with JavaScript", e);
        }
    }

    public WebElement waitForVisibilityOfElementLocated(By findBy) {
        try {
            log.debug("Waiting for visibility of element located by: {}", findBy);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
        } catch (Exception e) {
            log.error("Element not visible within {} seconds: {}", DEFAULT_TIMEOUT, findBy);
            throw new NoSuchElementException("Element not found: " + findBy);
        }
    }
    
    public WebElement waitForVisibilityOfElementLocated(By findBy, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }

    // For WebElement parameter
    public WebElement waitForVisibilityOf(WebElement element) {
        try {
            log.debug("Waiting for visibility of element: {}", element);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            log.error("Element not visible within {} seconds", DEFAULT_TIMEOUT);
            throw new NoSuchElementException("Element not visible: " + element);
        }
    }
    
    // For By locator parameter
    public WebElement waitForVisibilityOf(By locator) {
        try {
            log.debug("Waiting for visibility of element located by: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.error("Element not visible within {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw new NoSuchElementException("Element not found: " + locator);
        }
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            log.debug("Waiting for element to be clickable: {}", element);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            log.error("Element not clickable within {} seconds", DEFAULT_TIMEOUT);
            throw new RuntimeException("Element not clickable: " + element, e);
        }
    }
    
    public WebElement waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // For WebElement parameter
    public Boolean waitForInvisibilityOf(WebElement element) {
        try {
            log.debug("Waiting for element to become invisible: {}", element);
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            log.warn("Timeout waiting for element to become invisible: {}", e.getMessage());
            return false;
        }
    }
    
    // For By locator parameter
    public Boolean waitForInvisibilityOf(By locator) {
        try {
            log.debug("Waiting for element to become invisible: {}", locator);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Timeout waiting for element to become invisible: {}", e.getMessage());
            return false;
        }
    }
    
    public Boolean waitForInvisibilityOfElementLocated(By findBy) {
        try {
            log.debug("Waiting for element to become invisible located by: {}", findBy);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(findBy));
        } catch (Exception e) {
            log.warn("Timeout waiting for element to become invisible: {}", e.getMessage());
            return false;
        }
    }
    
    public String getToastMessage() {
        try {
            waitForVisibilityOf(toastMessage);
            String message = toastMessage.getText();
            log.info("Toast message: {}", message);
            return message;
        } catch (Exception e) {
            log.warn("No toast message found");
            return null;
        }
    }
    
    public void waitForSpinnerToDisappear() {
        waitForInvisibilityOf(spinner);
    }
    
    public void waitForPageToLoad() {
        try {
            log.debug("Waiting for page to load");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            log.warn("Page load timeout: {}", e.getMessage());
        }
    }
    
    public boolean isElementDisplayed(WebElement element) {
        try {
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean acceptAlertIfPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            log.info("Alert accepted");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void refreshPage() {
        log.info("Refreshing page");
        driver.navigate().refresh();
        waitForPageToLoad();
    }
}