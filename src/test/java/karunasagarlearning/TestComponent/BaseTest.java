package karunasagarlearning.TestComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import karunasagarlearning.pageobject.LandingPage;

public class BaseTest {
    
    public WebDriver driver;
    public LandingPage landingpage;
    private static Properties prop;
    private static boolean propertiesLoaded = false;

    @BeforeSuite
    public void globalSetup() throws IOException {
        // Load properties only once
        if (!propertiesLoaded) {
            prop = new Properties();
            String propPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" 
                            + File.separator + "java" + File.separator + "karunasagarlearning" + File.separator 
                            + "resources" + File.separator + "Global.properties";
            
            FileInputStream fis = new FileInputStream(propPath);
            prop.load(fis);
            fis.close();
            propertiesLoaded = true;
            System.out.println("Properties loaded successfully");
        }
        
        // Setup WebDriverManager
        WebDriverManager.chromedriver().setup();
        WebDriverManager.edgedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    public WebDriver initializeDriver() throws IOException {
        // Make sure properties are loaded
        if (prop == null) {
            globalSetup();
        }
        
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
                : prop.getProperty("browser");
        WebDriver driver = null;

        System.out.println("Initializing browser: " + browserName);

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-gpu");
            }
            driver = new ChromeDriver(options);
        } 
        else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } 
        else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }
        else {
            throw new RuntimeException("Browser not supported: " + browserName);
        }
        
        // Window management
        if (browserName.contains("headless")) {
            driver.manage().window().setSize(new Dimension(1440, 900));
        } else {
            driver.manage().window().maximize();
        }
        
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        if (driver == null) {
            System.err.println("Cannot capture screenshot: Driver is null for test: " + testCaseName);
            return null;
        }
        
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String filePath = System.getProperty("user.dir") + File.separator + "reports" + File.separator 
                        + testCaseName + "_" + timestamp + ".png";
        
        File reportsDir = new File(System.getProperty("user.dir") + File.separator + "reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        File destination = new File(filePath);
        FileUtils.copyFile(source, destination);
        System.out.println("Screenshot saved: " + filePath);
        return filePath;
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filepath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filepath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public LandingPage landingPage(@Optional String browser) throws IOException {
        if (browser != null) {
            System.setProperty("browser", browser);
        }
        driver = initializeDriver();
        landingpage = new LandingPage(driver);
        landingpage.goToURL();
        return landingpage;
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            System.out.println("Closing browser and cleaning up");
            driver.quit();
            driver = null;
        }
    }
}