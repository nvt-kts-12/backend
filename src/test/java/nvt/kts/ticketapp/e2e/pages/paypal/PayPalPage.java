package nvt.kts.ticketapp.e2e.pages.paypal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PayPalPage {

    private WebDriver webDriver;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "btnNext")
    private WebElement btnNext;

    @FindBy(id = "cancelLink")
    private WebElement cancelLink;

    @FindBy(id = "createAccount")
    private WebElement btnCreate;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "btnLogin")
    private WebElement btnLogin;

    @FindBy(id = "payment-submit-btn")
    private WebElement btnSubmit;

    @FindBy(id = "btnHome")
    private WebElement btnHome;

    @FindBy(id = "ShipTo")
    private WebElement shipToHeading;

    @FindBy(className = "headerText")
    private WebElement headerText;

    public PayPalPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureHomeIsClickable() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.elementToBeClickable(btnHome));
    }

    public void ensureSubmitIsClickable() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.elementToBeClickable(btnSubmit));
    }

    public void ensureLoginIsClickable() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.elementToBeClickable(btnLogin));
    }

    public void ensureNextIsClickable() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.elementToBeClickable(btnNext));
    }

    public void ensureCreateIsClickable() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.elementToBeClickable(btnCreate));
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public void setEmailInput(String emailInput) {
        WebElement el = getEmailInput();
        el.clear();
        el.sendKeys(emailInput);
    }

    public WebElement getBtnNext() {
        return btnNext;
    }


    public WebElement getCancelLink() {
        return cancelLink;
    }


    public WebElement getBtnCreate() {
        return btnCreate;
    }


    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(String passwordInput) {
        WebElement el = getPasswordInput();
        el.clear();
        el.sendKeys(passwordInput);
    }

    public WebElement getBtnLogin() {
        return btnLogin;
    }


    public WebElement getBtnSubmit() {
        return btnSubmit;
    }


    public WebElement getBtnHome() {
        return btnHome;
    }

}
