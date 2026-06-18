package karunasagarlearning.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import karunasagarlearning.AbstractComponents.AbstractComponent;

/**
 * RegistrationPage - Page Object Model for User Registration
 *
 * This class represents the registration page of the application and provides
 * methods to interact with all registration form elements including:
 * - Personal Information (First Name, Last Name, Email, Mobile Number)
 * - Occupation Dropdown Selection
 * - Gender Radio Button Selection
 * - Password and Confirm Password Input
 * - Age Verification Checkbox (18+ years)
 *
 * The class extends AbstractComponent to inherit common Selenium utilities
 * like wait conditions, element visibility, and JavaScript operations.
 *
 * @author Karun Sagar
 * @version 1.0
 */
public class RegistrationPage extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(RegistrationPage.class);
    WebDriver driver;

    public RegistrationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // ============================================================================
    // FORM FIELD LOCATORS - Using @FindBy for element identification
    // ============================================================================

    /** First Name input field - id: firstName */
    @FindBy(id = "firstName")
    WebElement firstNameField;

    /** Last Name input field - id: lastName */
    @FindBy(id = "lastName")
    WebElement lastNameField;

    /** Email address input field - id: userEmail (type: email) */
    @FindBy(id = "userEmail")
    WebElement emailField;

    /** Mobile Number input field - id: userMobile (type: text) */
    @FindBy(id = "userMobile")
    WebElement mobileNumberField;

    /** Occupation dropdown - formcontrolname: occupation */
    @FindBy(css = "select[formcontrolname='occupation']")
    WebElement occupationDropdown;

    /** Male gender radio button - value: Male, formcontrolname: gender */
    @FindBy(css = "input[value='Male'][formcontrolname='gender']")
    WebElement maleRadioButton;

    /** Female gender radio button - value: Female, formcontrolname: gender */
    @FindBy(css = "input[value='Female'][formcontrolname='gender']")
    WebElement femaleRadioButton;

    /** Password input field - id: userPassword (type: password) */
    @FindBy(id = "userPassword")
    WebElement passwordField;

    /** Confirm Password input field - id: confirmPassword (type: password) */
    @FindBy(id = "confirmPassword")
    WebElement confirmPasswordField;

    /** Age verification checkbox - must be 18 years or older */
    @FindBy(css = "input[type='checkbox'][formcontrolname='required']")
    WebElement ageCheckbox;

    /** Register button - id: login (type: submit) */
    @FindBy(id = "login")
    WebElement registerButton;

    /** Success message toast notification */
    @FindBy(css = ".toast-message")
    WebElement successMessageText;

    /** Error message display for validation errors */
    @FindBy(css = ".alert, .error-message, .validation-error, .toast-message")
    WebElement errorMessageText;

    /** Navigation link to login page - "Already have an account? Login here" */
    @FindBy(css = "a.text-reset")
    WebElement loginLink;

    /** Loading spinner that appears during registration processing */
    @FindBy(css = ".ng-animating")
    WebElement loadingSpinner;

    // ============================================================================
    // LOCATOR VARIABLES - For By locator patterns
    // ============================================================================

    /** Registration form container */
    private final By registrationForm = By.cssSelector("form");

    /** Success message display locator */
    private final By successMessage = By.cssSelector(".toast-message");

    public void goToRegistrationURL() {
        log.info("Navigating to Registration URL: https://rahulshettyacademy.com/client/auth/register");
        driver.get("https://rahulshettyacademy.com/client/auth/register");
        waitForVisibilityOf(firstNameField);
        log.info("Successfully navigated to registration page");
    }

    /**
     * Enter the first name in the registration form
     *
     * @param firstName - The first name to be entered
     * @throws RuntimeException if first name field is not accessible
     */
    public void enterFirstName(String firstName) {
        log.debug("Entering first name: {}", firstName);
        waitForVisibilityOf(firstNameField);
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        log.debug("First name entered successfully");
    }

    /**
     * Enter the last name in the registration form
     *
     * @param lastName - The last name to be entered
     * @throws RuntimeException if last name field is not accessible
     */
    public void enterLastName(String lastName) {
        log.debug("Entering last name: {}", lastName);
        waitForVisibilityOf(lastNameField);
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        log.debug("Last name entered successfully");
    }

    /**
     * Enter the email address in the registration form
     *
     * @param email - The email address to be entered
     * @throws RuntimeException if email field is not accessible
     */
    public void enterEmail(String email) {
        log.debug("Entering email: {}", email);
        waitForVisibilityOf(emailField);
        emailField.clear();
        emailField.sendKeys(email);
        log.debug("Email entered successfully");
    }

    /**
     * Enter the mobile number in the registration form
     * Note: This field is optional and may not be present in all registration forms
     *
     * @param mobileNumber - The mobile number to be entered
     * @throws Warning if mobile number field is not found (logs warning and continues)
     */
    public void enterMobileNumber(String mobileNumber) {
        log.debug("Entering mobile number: {}", mobileNumber);
        try {
            waitForVisibilityOf(mobileNumberField);
            mobileNumberField.clear();
            mobileNumberField.sendKeys(mobileNumber);
            log.debug("Mobile number entered successfully");
        } catch (Exception e) {
            log.warn("Mobile number field not found or not visible: {}", e.getMessage());
        }
    }

    /**
     * Select occupation from the dropdown using the value attribute
     * Available options: "1: Doctor", "2: Student", "3: Engineer", "4: Scientist"
     *
     * @param occupationValue - The value attribute of the occupation option
     * @throws RuntimeException if occupation dropdown is not accessible or value not found
     */
    public void selectOccupation(String occupationValue) {
        log.debug("Selecting occupation: {}", occupationValue);
        try {
            waitForVisibilityOf(occupationDropdown);
            Select selectOccupation = new Select(occupationDropdown);
            selectOccupation.selectByValue(occupationValue);
            log.info("Occupation selected: {}", occupationValue);
        } catch (Exception e) {
            log.error("Failed to select occupation: {}", e.getMessage());
            throw new RuntimeException("Could not select occupation: " + occupationValue, e);
        }
    }

    /**
     * Select occupation from the dropdown using visible text
     * Available options: "Doctor", "Student", "Engineer", "Scientist"
     * This is more user-friendly than using values
     *
     * @param occupationText - The visible text of the occupation option
     * @throws RuntimeException if occupation dropdown is not accessible or text not found
     */
    public void selectOccupationByVisibleText(String occupationText) {
        log.debug("Selecting occupation by text: {}", occupationText);
        try {
            waitForVisibilityOf(occupationDropdown);
            Select selectOccupation = new Select(occupationDropdown);
            selectOccupation.selectByVisibleText(occupationText);
            log.info("Occupation selected: {}", occupationText);
        } catch (Exception e) {
            log.error("Failed to select occupation: {}", e.getMessage());
            throw new RuntimeException("Could not select occupation: " + occupationText, e);
        }
    }

    /**
     * Select gender from the radio buttons
     *
     * @param genderValue - Either "Male" or "Female" (case-insensitive)
     * @throws RuntimeException if invalid gender value or element not clickable
     */
    public void selectGender(String genderValue) {
        log.debug("Selecting gender: {}", genderValue);
        try {
            if (genderValue.equalsIgnoreCase("Male")) {
                waitForElementToBeClickable(maleRadioButton).click();
                log.info("Male gender selected");
            } else if (genderValue.equalsIgnoreCase("Female")) {
                waitForElementToBeClickable(femaleRadioButton).click();
                log.info("Female gender selected");
            } else {
                log.warn("Invalid gender value: {}", genderValue);
                throw new RuntimeException("Invalid gender value: " + genderValue);
            }
        } catch (Exception e) {
            log.error("Failed to select gender: {}", e.getMessage());
            throw new RuntimeException("Could not select gender: " + genderValue, e);
        }
    }

    /**
     * Enter password in the password field
     *
     * @param password - The password to be entered
     * @throws RuntimeException if password field is not accessible
     */
    public void enterPassword(String password) {
        log.debug("Entering password");
        waitForVisibilityOf(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
        log.debug("Password entered successfully");
    }

    /**
     * Enter confirmation password in the confirm password field
     *
     * @param confirmPassword - The confirmation password to be entered
     * @throws RuntimeException if confirm password field is not accessible
     */
    public void enterConfirmPassword(String confirmPassword) {
        log.debug("Entering confirm password");
        waitForVisibilityOf(confirmPasswordField);
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
        log.debug("Confirm password entered successfully");
    }

    /**
     * Accept/Check the age verification checkbox (Must be 18 years or older)
     * This is a required step before submitting the registration form
     *
     * @throws RuntimeException if checkbox is not accessible or cannot be clicked
     */
    public void acceptAgeCheckbox() {
        log.debug("Accepting age checkbox (18 years or older)");
        try {
            if (!ageCheckbox.isSelected()) {
                waitForElementToBeClickable(ageCheckbox).click();
                log.info("Age checkbox accepted");
            } else {
                log.debug("Age checkbox already checked");
            }
        } catch (Exception e) {
            log.warn("Could not accept age checkbox: {}", e.getMessage());
            throw new RuntimeException("Could not accept age checkbox", e);
        }
    }

    /**
     * Check if the age verification checkbox is currently selected
     *
     * @return true if checkbox is selected, false otherwise
     */
    public boolean isAgeCheckboxSelected() {
        try {
            return ageCheckbox.isSelected();
        } catch (Exception e) {
            log.warn("Could not verify age checkbox selection: {}", e.getMessage());
            return false;
        }
    }

    // ============================================================================
    // COMPLETE REGISTRATION WORKFLOWS - High-level registration methods
    // ============================================================================

    /**
     * Register a user with basic information only
     * This method fills: First Name, Last Name, Email, Password, Confirm Password, Age Checkbox
     *
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param email - User's email address
     * @param password - User's password
     * @param confirmPassword - Confirmation of the password
     * @throws RuntimeException if any registration step fails
     */
    public void registerUser(String firstName, String lastName, String email, String password, String confirmPassword) {
        log.info("Registering user with email: {}", email);
        try {
            enterFirstName(firstName);
            enterLastName(lastName);
            enterEmail(email);
            enterPassword(password);
            enterConfirmPassword(confirmPassword);
            acceptAgeCheckbox();
            submitRegistration();
            log.info("User registration completed");
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Register a user with basic information and mobile number
     * This method fills: First Name, Last Name, Email, Mobile Number, Password, Confirm Password, Age Checkbox
     *
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param email - User's email address
     * @param mobileNumber - User's mobile number
     * @param password - User's password
     * @param confirmPassword - Confirmation of the password
     * @throws RuntimeException if any registration step fails
     */
    public void registerUserWithMobile(String firstName, String lastName, String email,
                                       String mobileNumber, String password, String confirmPassword) {
        log.info("Registering user with email: {} and mobile: {}", email, mobileNumber);
        try {
            enterFirstName(firstName);
            enterLastName(lastName);
            enterEmail(email);
            enterMobileNumber(mobileNumber);
            enterPassword(password);
            enterConfirmPassword(confirmPassword);
            acceptAgeCheckbox();
            submitRegistration();
            log.info("User registration with mobile completed");
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Register a user with COMPLETE information including occupation and gender (using value)
     * This method fills ALL form fields
     *
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param email - User's email address
     * @param mobileNumber - User's mobile number
     * @param occupationValue - Occupation value (e.g., "1: Doctor", "2: Student")
     * @param gender - Gender ("Male" or "Female")
     * @param password - User's password
     * @param confirmPassword - Confirmation of the password
     * @throws RuntimeException if any registration step fails
     */
    public void registerUserComplete(String firstName, String lastName, String email, String mobileNumber,
                                     String occupationValue, String gender, String password, String confirmPassword) {
        log.info("Registering user with complete details - Email: {}, Gender: {}, Occupation: {}",
                 email, gender, occupationValue);
        try {
            enterFirstName(firstName);
            enterLastName(lastName);
            enterEmail(email);
            enterMobileNumber(mobileNumber);
            selectOccupation(occupationValue);
            selectGender(gender);
            enterPassword(password);
            enterConfirmPassword(confirmPassword);
            acceptAgeCheckbox();
            submitRegistration();
            log.info("Complete user registration successful");
        } catch (Exception e) {
            log.error("Error during complete user registration: {}", e.getMessage());
            throw new RuntimeException("Complete registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Register a user with COMPLETE information using occupation visible text
     * This method is more user-friendly as it uses profession names instead of values
     * Available occupations: "Doctor", "Student", "Engineer", "Scientist"
     *
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param email - User's email address
     * @param mobileNumber - User's mobile number
     * @param occupationText - Occupation text (e.g., "Doctor", "Engineer")
     * @param gender - Gender ("Male" or "Female")
     * @param password - User's password
     * @param confirmPassword - Confirmation of the password
     * @throws RuntimeException if any registration step fails
     */
    public void registerUserByVisibleText(String firstName, String lastName, String email, String mobileNumber,
                                          String occupationText, String gender, String password, String confirmPassword) {
        log.info("Registering user with occupation text: {}", occupationText);
        try {
            enterFirstName(firstName);
            enterLastName(lastName);
            enterEmail(email);
            enterMobileNumber(mobileNumber);
            selectOccupationByVisibleText(occupationText);
            selectGender(gender);
            enterPassword(password);
            enterConfirmPassword(confirmPassword);
            acceptAgeCheckbox();
            submitRegistration();
            log.info("User registration with visible text occupation successful");
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Submit the registration form
     * This clicks the Register button and waits for the spinner to disappear
     *
     * @throws RuntimeException if Register button is not clickable
     */
    public void submitRegistration() {
        log.info("Submitting registration");
        try {
            waitForElementToBeClickable(registerButton);
            scrollToElement(registerButton);
            registerButton.click();
            waitForInvisibilityOf(spinner);
            log.debug("Registration button clicked");
        } catch (Exception e) {
            log.error("Failed to submit registration: {}", e.getMessage());
            throw new RuntimeException("Could not submit registration", e);
        }
    }

    // ============================================================================
    // VERIFICATION AND RESPONSE METHODS - Get messages and verify actions
    // ============================================================================

    /**
     * Get the success message displayed after successful registration
     * Waits briefly for the message to appear on the page
     *
     * @return Success message text, or empty string if not found
     */
    public String getSuccessMessage() {
        log.info("Retrieving success message");
        try {
            Thread.sleep(2000); // Wait for success message to appear

            WebElement successElement = waitForVisibilityOf(successMessage);
            String message = successElement.getText();
            log.info("Success message: {}", message);
            return message;

        } catch (Exception e) {
            log.warn("Success message not found: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get the error message displayed during registration
     * Used for validation error messages or system errors
     *
     * @return Error message text, or empty string if not found
     */
    public String getErrorMessage() {
        log.info("Retrieving error message");
        try {
            Thread.sleep(2000); // Wait for error message to appear

            WebElement errorElement = waitForVisibilityOf(By.cssSelector(".toast-message, .alert, .error-message, .validation-error"));
            String message = errorElement.getText();
            log.warn("Error message: {}", message);
            return message;

        } catch (Exception e) {
            log.warn("Error message not found: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Navigate to the login page from the registration page
     * Clicks on "Already have an account? Login here" link
     * Falls back to direct URL navigation if link is not clickable
     */
    public void goToLoginPage() {
        log.info("Navigating to login page");
        try {
            waitForElementToBeClickable(loginLink);
            loginLink.click();
            log.debug("Successfully navigated to login page");
        } catch (Exception e) {
            log.warn("Login link not clickable, attempting direct navigation");
            driver.get("https://rahulshettyacademy.com/client");
        }
    }

    // ============================================================================
    // UTILITY AND FORM STATE METHODS - Check form state and clear fields
    // ============================================================================

    /**
     * Clear all fields in the registration form
     * Used for resetting form or Re-entering information
     */
    public void clearAllFields() {
        log.debug("Clearing all registration fields");
        try {
            firstNameField.clear();
            lastNameField.clear();
            emailField.clear();
            mobileNumberField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            log.debug("All fields cleared");
        } catch (Exception e) {
            log.warn("Error clearing fields: {}", e.getMessage());
        }
    }

    /**
     * Check if the registration form is displayed on the page
     * Verifies visibility of key form fields
     *
     * @return true if form is visible, false otherwise
     */
    public boolean isRegistrationFormDisplayed() {
        try {
            return isElementDisplayed(firstNameField) &&
                   isElementDisplayed(emailField) &&
                   isElementDisplayed(passwordField);
        } catch (Exception e) {
            log.warn("Registration form not displayed");
            return false;
        }
    }

    /**
     * Check if the Register button is enabled and can be clicked
     *
     * @return true if button is enabled, false otherwise
     */
    public boolean isRegisterButtonEnabled() {
        try {
            return registerButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify if two passwords match
     * Used for validation before submission
     *
     * @param password1 - First password
     * @param password2 - Second password (usually confirm password)
     * @return true if passwords match, false otherwise
     */
    public boolean arePasswordsMatching(String password1, String password2) {
        boolean matches = password1.equals(password2);
        if (!matches) {
            log.warn("Passwords do not match");
        }
        return matches;
    }

    /**
     * Check if the email field is empty
     *
     * @return true if field is empty, false otherwise
     */
    public boolean isEmailFieldEmpty() {
        return emailField.getAttribute("value").isEmpty();
    }

    /**
     * Check if the password field is empty
     *
     * @return true if field is empty, false otherwise
     */
    public boolean isPasswordFieldEmpty() {
        return passwordField.getAttribute("value").isEmpty();
    }

    // ============================================================================
    // GETTER METHODS - Retrieve values from form fields
    // ============================================================================

    /**
     * Get the value currently entered in the first name field
     *
     * @return First name value
     */
    public String getFirstNameFieldValue() {
        return firstNameField.getAttribute("value");
    }

    /**
     * Get the value currently entered in the last name field
     *
     * @return Last name value
     */
    public String getLastNameFieldValue() {
        return lastNameField.getAttribute("value");
    }

    /**
     * Get the value currently entered in the email field
     *
     * @return Email value
     */
    public String getEmailFieldValue() {
        return emailField.getAttribute("value");
    }

    /**
     * Get the value currently entered in the mobile number field
     *
     * @return Mobile number value
     */
    public String getMobileNumberFieldValue() {
        return mobileNumberField.getAttribute("value");
    }

    /**
     * Get the currently selected occupation from the dropdown
     *
     * @return Selected occupation text
     */
    public String getSelectedOccupation() {
        try {
            Select occupationSelect = new Select(occupationDropdown);
            return occupationSelect.getFirstSelectedOption().getText();
        } catch (Exception e) {
            log.warn("Could not get selected occupation: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get the currently selected gender from the radio buttons
     *
     * @return Selected gender ("Male" or "Female" or empty string)
     */
    public String getSelectedGender() {
        try {
            if (maleRadioButton.isSelected()) {
                return "Male";
            } else if (femaleRadioButton.isSelected()) {
                return "Female";
            }
            return "";
        } catch (Exception e) {
            log.warn("Could not get selected gender: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get all available occupation options from the dropdown
     * Excludes the default "Choose your occupation" option
     *
     * @return List of available occupation names
     */
    public java.util.List<String> getAvailableOccupations() {
        try {
            Select occupationSelect = new Select(occupationDropdown);
            return occupationSelect.getOptions().stream()
                .skip(1) // Skip "Choose your occupation" option
                .map(WebElement::getText)
                .toList();
        } catch (Exception e) {
            log.warn("Could not get available occupations: {}", e.getMessage());
            return java.util.List.of();
        }
    }

    /**
     * Check if the Male radio button is currently selected
     *
     * @return true if Male is selected, false otherwise
     */
    public boolean isMaleRadioButtonSelected() {
        try {
            return maleRadioButton.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if the Female radio button is currently selected
     *
     * @return true if Female is selected, false otherwise
     */
    public boolean isFemaleRadioButtonSelected() {
        try {
            return femaleRadioButton.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================================================
    // WAIT AND SYNCHRONIZATION METHODS - Wait for page elements and actions
    // ============================================================================

    /**
     * Wait for the registration page to fully load
     * Ensures all required elements are visible before proceeding
     */
    public void waitForRegistrationPageToLoad() {
        log.debug("Waiting for registration page to load");
        waitForVisibilityOf(firstNameField);
        waitForVisibilityOf(registerButton);
        waitForPageToLoad();
        log.debug("Registration page loaded successfully");
    }

    /**
     * Wait for the registration process to complete
     * Waits for spinner to disappear and success message to appear
     */
    public void waitForRegistrationToComplete() {
        log.debug("Waiting for registration to complete");
        waitForInvisibilityOf(spinner);
        try {
            waitForVisibilityOfElementLocated(successMessage, 10);
        } catch (Exception e) {
            log.warn("Success message not visible, checking for errors");
        }
    }
}

