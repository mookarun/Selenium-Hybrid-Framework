package karunasagarlearning.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporterNG {
    
    private static ExtentReports extent;
    
    public static ExtentReports getReportObject() {
        if (extent == null) {
            createReportInstance();
        }
        return extent;
    }
    
    private static void createReportInstance() {
        // Create reports directory if it doesn't exist
        String reportsDir = System.getProperty("user.dir") + "//reports";
        new java.io.File(reportsDir).mkdirs();
        
        // Define report path with timestamp
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String reportPath = System.getProperty("user.dir") + "//reports//extent-report_" + timestamp + ".html";
        
        // Create reporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure reporter
        configureSparkReporter(sparkReporter);
        
        // Create extent report instance
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        setSystemInformation();
    }
    
    private static void configureSparkReporter(ExtentSparkReporter sparkReporter) {
        // Basic configuration
        sparkReporter.config().setReportName("Selenium Framework Automation Report");
        sparkReporter.config().setDocumentTitle("Test Execution Results");
        
        // Set theme (DARK or STANDARD)
        sparkReporter.config().setTheme(Theme.DARK);
        
        // Set encoding
        sparkReporter.config().setEncoding("UTF-8");
        
        // REMOVED: setViewOrder - not supported in this version
        
        // Timestamp format
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
    }
    
    private static void setSystemInformation() {
        // Tester information
        extent.setSystemInfo("Tester", "Karuna Sagar M");
        extent.setSystemInfo("Organization", "KarunaSagar Learning");
        extent.setSystemInfo("Framework", "Selenium WebDriver with TestNG");
        
        // Environment information
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        
        // Browser information
        String browser = System.getProperty("browser");
        if (browser == null) {
            browser = System.getenv("browser");
        }
        extent.setSystemInfo("Browser", browser != null ? browser : "Chrome (Default)");
        
        // Additional info
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Build Number", System.getProperty("build.number", "Local"));
        
        // Set host information
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            extent.setSystemInfo("Host Name", localHost.getHostName());
            extent.setSystemInfo("IP Address", localHost.getHostAddress());
        } catch (Exception e) {
            extent.setSystemInfo("Host Name", "Unknown");
        }
    }
    
    /**
     * Flush and close the report
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report generated successfully!");
        }
    }
    
    /**
     * Get report path for the current execution
     */
    public static String getReportPath() {
        return System.getProperty("user.dir") + "//reports//extent-report_" + 
               new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".html";
    }
    
    /**
     * Add custom system information at runtime
     */
    public static void addSystemInfo(String key, String value) {
        if (extent != null) {
            extent.setSystemInfo(key, value);
        }
    }
}