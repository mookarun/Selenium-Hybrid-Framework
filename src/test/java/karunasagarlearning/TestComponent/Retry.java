package karunasagarlearning.TestComponent;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    
    private int count = 0;
    private static final int MAX_RETRY_COUNT = 1;  // Change this to configure number of retries
    
    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRY_COUNT) {
            count++;
            System.out.println("Retrying test: " + result.getMethod().getMethodName() 
                             + " - Attempt #" + (count + 1) 
                             + " (Failed with: " + result.getThrowable().getMessage() + ")");
            return true;
        }
        return false;
    }
}