package nvt.kts.ticketapp.e2e.pages.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver webDriver;

    @FindBy(id = "usernameInput")
    private WebElement usernameInput;

    @FindBy(id = "passwordInput")
    private WebElement passwordInput;

    @FindBy(id = "submitLogin")
    private WebElement submitBtn;

    @FindBy(id="snackbar")
    private WebElement snackbar;


    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureSubmitIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(submitBtn));
    }

    public void ensureUsernameIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(usernameInput));
    }

    public WebElement getSnackbar() {
        return snackbar;
    }

    public WebElement getUsernameInput() {
        return usernameInput;
    }

    public void setUsernameInput(String usernameInput) {
        WebElement el = getUsernameInput();
        el.clear();
        el.sendKeys(usernameInput);
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(String passwordInput) {
        WebElement el = getPasswordInput();
        el.clear();
        el.sendKeys(passwordInput);
    }

    public WebElement getSubmitBtn() {
        return submitBtn;
    }

    public void ensureSnackbarIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(snackbar));
    }
}