package karunasagarlearning.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

public class OrderPage extends AbstractComponent {
    
    private static final Logger log = LoggerFactory.getLogger(OrderPage.class);
    WebDriver driver;
    
    public OrderPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    
    @FindBy(css = ".table-bordered tr td:nth-child(2)")
    List<WebElement> productNamesList;
    
    @FindBy(css = ".table-bordered tr")
    List<WebElement> orderRows;
    
    @FindBy(css = ".table-bordered")
    WebElement ordersTable;
    
    private final By ordersTableLocator = By.cssSelector(".table-bordered");

    public Boolean verifyOrderDisplay(String productName) {
        log.info("Verifying order for product: {}", productName);
        try {
            waitForVisibilityOfElementLocated(ordersTableLocator);
            waitForInvisibilityOf(spinner);
            
            boolean isFound = productNamesList.stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));
            
            if (isFound) {
                log.info("Order found for product: {}", productName);
            } else {
                log.warn("Order not found for product: {}", productName);
            }
            
            return isFound;
            
        } catch (Exception e) {
            log.error("Error verifying order: {}", e.getMessage());
            return false;
        }
    }
    
    public List<String> getOrderedProductNames() {
        waitForVisibilityOfElementLocated(ordersTableLocator);
        return productNamesList.stream()
            .map(WebElement::getText)
            .toList();
    }
    
    public int getOrderCount() {
        waitForVisibilityOfElementLocated(ordersTableLocator);
        int count = orderRows.size() - 1;
        log.info("Order count: {}", count);
        return count;
    }
    
    public boolean hasOrders() {
        try {
            waitForVisibilityOfElementLocated(ordersTableLocator);
            return orderRows.size() > 1;
        } catch (Exception e) {
            log.warn("No orders found");
            return false;
        }
    }
    
    public WebElement getOrderRowForProduct(String productName) {
        waitForVisibilityOfElementLocated(ordersTableLocator);
        
        for (WebElement row : orderRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (!cells.isEmpty()) {
                String productText = cells.get(1).getText();
                if (productText.equalsIgnoreCase(productName)) {
                    log.info("Found order row for product: {}", productName);
                    return row;
                }
            }
        }
        
        log.warn("No order row found for product: {}", productName);
        return null;
    }
    
    public void viewOrderDetails(String productName) {
        WebElement orderRow = getOrderRowForProduct(productName);
        if (orderRow != null) {
            WebElement viewButton = orderRow.findElement(By.cssSelector("button"));
            waitForElementToBeClickable(viewButton).click();
            log.info("Clicked view details for product: {}", productName);
        } else {
            throw new RuntimeException("Cannot view order details - Order not found: " + productName);
        }
    }
    
    public String getOrderStatus(String productName) {
        WebElement orderRow = getOrderRowForProduct(productName);
        if (orderRow != null) {
            List<WebElement> cells = orderRow.findElements(By.tagName("td"));
            if (cells.size() > 3) {
                String status = cells.get(3).getText();
                log.info("Order status for {}: {}", productName, status);
                return status;
            }
        }
        return null;
    }
    
    public void waitForOrderPageToLoad() {
        log.debug("Waiting for Order page to load");
        waitForVisibilityOfElementLocated(ordersTableLocator);
        waitForInvisibilityOf(spinner);
        waitForPageToLoad();
    }
    
    public boolean isOrderPageDisplayed() {
        try {
            return isElementDisplayed(ordersTable);
        } catch (Exception e) {
            return false;
        }
    }
}