package karunasagarlearning.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class StandAloneTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://rahulshettyacademy.com/client");
		
		String Productname = "ZARA COAT 3";
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		driver.findElement(By.id("userEmail")).sendKeys("karnasagar36@gmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Mookarun123!");
		driver.findElement(By.id("login")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));
		List<WebElement> products =  driver.findElements(By.cssSelector(".mb-3"));
		
		WebElement prod = products.stream().filter(product -> product.findElement(By.tagName("b"))
		.getText().equals(Productname)).findFirst().orElse(null);
		WebElement addToCartButton = prod.findElement(By.cssSelector(".card-body button:last-of-type"));
		Actions actions = new Actions(driver);
		actions.moveToElement(addToCartButton).perform();
		wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		
		WebElement cartclick = driver.findElement(By.cssSelector("[routerlink='/dashboard/cart']"));
		actions.moveToElement(cartclick).perform();
		wait.until(ExpectedConditions.elementToBeClickable(cartclick)).click();
		
		List<WebElement> CartProduct = driver.findElements(By.cssSelector(".cartSection h3"));
		Boolean textmatch = CartProduct.stream().anyMatch(cartproduct-> cartproduct.getText().equalsIgnoreCase(Productname));
		Assert.assertTrue(textmatch);
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("toast-container")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));
		
		WebElement checkoutbutton = driver.findElement(By.cssSelector(".totalRow .btn-primary"));
		actions.moveToElement(checkoutbutton).perform();
		wait.until(ExpectedConditions.elementToBeClickable(checkoutbutton)).click();
		
		driver.findElement(By.cssSelector(".form-group .text-validated")).sendKeys("Ind");
		String countryname = "India";
		
		List<WebElement> countrylist = driver.findElements(By.cssSelector(".ta-results .ta-item"));
		countrylist.stream().filter(ctrlist -> ctrlist.getText().equalsIgnoreCase(countryname)).findFirst().orElse(null).click();
		
		WebElement placeorderbutton = driver.findElement(By.cssSelector(".action__submit"));
		actions.moveToElement(placeorderbutton).perform();
		wait.until(ExpectedConditions.elementToBeClickable(placeorderbutton)).click();
		
		String orderconfirmmsg = driver.findElement(By.cssSelector(".hero-primary")).getText();		
		Assert.assertTrue(orderconfirmmsg.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
		driver.quit();
		
	}
	

}
