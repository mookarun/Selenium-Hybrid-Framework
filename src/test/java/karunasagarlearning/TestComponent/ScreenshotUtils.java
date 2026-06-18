package karunasagarlearning.TestComponent;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {
    
    public static String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
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
        return filePath;
    }
}