package karunasagarlearning.TestComponent;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import karunasagarlearning.resources.ExtentReporterNG;

// ADD THIS IMPORT
import karunasagarlearning.TestComponent.ScreenshotUtils;

public class Listeners implements ITestListener {
    
    private static ExtentReports extent = ExtentReporterNG.getReportObject();
    private ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("=========================================");
        System.out.println("Test Started: " + testName);
        System.out.println("=========================================");
        
        ExtentTest test = extent.createTest(testName);
        
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        test.assignAuthor("Karuna Sagar M");
        test.assignDevice(System.getProperty("os.name"));
        
        extentTest.set(test);
        extentTest.get().log(Status.INFO, "Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("✅ Test Passed: " + testName);
        
        extentTest.get().log(Status.PASS, "Test executed successfully");
        extentTest.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("❌ Test Failed: " + testName);
        System.out.println("Error: " + result.getThrowable().getMessage());
        
        extentTest.get().fail(result.getThrowable());
        extentTest.get().log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        
        try {
            Object testInstance = result.getInstance();
            WebDriver driver = null;
            
            try {
                java.lang.reflect.Method getDriverMethod = testInstance.getClass().getMethod("getDriver");
                driver = (WebDriver) getDriverMethod.invoke(testInstance);
            } catch (NoSuchMethodException e) {
                try {
                    java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
                    driverField.setAccessible(true);
                    driver = (WebDriver) driverField.get(testInstance);
                } catch (Exception ex) {
                    try {
                        java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField("driver");
                        driverField.setAccessible(true);
                        driver = (WebDriver) driverField.get(testInstance);
                    } catch (Exception ex2) {
                        System.err.println("Could not access WebDriver: " + ex2.getMessage());
                    }
                }
            }
            
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.getScreenshot(testName, driver);
                extentTest.get().addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
                extentTest.get().log(Status.FAIL, "Screenshot captured: " + screenshotPath);
            } else {
                extentTest.get().log(Status.WARNING, "Could not capture screenshot - WebDriver is null");
            }
        } catch (Exception e) {
            extentTest.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("⚠️ Test Skipped: " + testName);
        
        extentTest.get().log(Status.SKIP, "Test skipped: " + result.getThrowable());
        extentTest.get().skip("Test Execution Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("⚠️ Test failed but within success percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=========================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("Total Tests: " + context.getAllTestMethods().length);
        System.out.println("=========================================");
        
        extent.setSystemInfo("Test Suite", context.getName());
        extent.setSystemInfo("Start Time", new java.util.Date().toString());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=========================================");
        System.out.println("Test Suite Finished: " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("=========================================");
        
        extent.setSystemInfo("End Time", new java.util.Date().toString());
        extent.setSystemInfo("Total Tests", String.valueOf(context.getAllTestMethods().length));
        extent.setSystemInfo("Passed Tests", String.valueOf(context.getPassedTests().size()));
        extent.setSystemInfo("Failed Tests", String.valueOf(context.getFailedTests().size()));
        extent.setSystemInfo("Skipped Tests", String.valueOf(context.getSkippedTests().size()));
        
        ExtentReporterNG.flushReport();
        System.out.println("Extent Report generated at: " + ExtentReporterNG.getReportPath());
    }
}