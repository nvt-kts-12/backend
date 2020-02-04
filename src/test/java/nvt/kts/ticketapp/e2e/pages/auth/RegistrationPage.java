package nvt.kts.ticketapp.e2e.pages.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationPage {


    private WebDriver webDriver;

    @FindBy(id = "usernameInput")
    private WebElement usernameInput;

    @FindBy(id = "passwordInput")
    private WebElement passwordInput;

    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;

    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;

    @FindBy(id = "emailInput")
    private WebElement emailInput;

    @FindBy(id = "regSubmit")
    private WebElement regSubmitButton;

    @FindBy(id="snackbar")
    private WebElement snackbar;

    @FindBy(xpath = "/html/body/app-root/div/app-register/div/mat-form-field[1]/div/div[3]/div")
    private WebElement matErrorUsernameRequired;

    @FindBy(xpath = "//*[@id=\"mat-error-3\"]")
    private WebElement matErrorIncorrectPassword;

    @FindBy(xpath = "//*[@id=\"mat-error-4\"]")
    private WebElement matErrorIncorrectEmail;

    public RegistrationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getMatErrorIncorrectEmail() {
        return matErrorIncorrectEmail;
    }

    public WebElement getMatErrorUsernameRequired() {
        return matErrorUsernameRequired;
    }

    public WebElement getSnackbar() {
        return snackbar;
    }

    public WebElement getUsernameInput() {
        return usernameInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getMatErrorIncorrectPassword() {
        return matErrorIncorrectPassword;
    }

    public WebElement getFirstNameInput() {
        return firstNameInput;
    }

    public WebElement getLastNameInput() {
        return lastNameInput;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getRegSubmitButton() {
        return regSubmitButton;
    }

    public void setUsernameInput(String usernameInput) {
        WebElement el = getUsernameInput();
        el.clear();
        el.sendKeys(usernameInput);
    }

    public void setPasswordInput(String passwordInput) {
        WebElement el = getPasswordInput();
        el.clear();
        el.sendKeys(passwordInput);
    }

    public void setFirstNameInput(String firstNameInput) {
        WebElement el = getFirstNameInput();
        el.clear();
        el.sendKeys(firstNameInput);
    }

    public void setLastNameInput(String lastNameInput) {
        WebElement el = getLastNameInput();
        el.clear();
        el.sendKeys(lastNameInput);
    }

    public void setEmailInput(String emailInput) {
        WebElement el = getEmailInput();
        el.clear();
        el.sendKeys(emailInput);
    }

    public void ensureUsernameInputIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(regSubmitButton));
    }

    public void ensureSubmitIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(regSubmitButton));
    }

    public void ensureSnackbarIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(snackbar));
    }

    public void ensureMatErrorIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(matErrorUsernameRequired));
    }

    public void ensureIncorrectPassErrIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(matErrorIncorrectPassword));
    }

    public void ensureIncorrectEmailErrIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(matErrorIncorrectEmail));
    }

}
