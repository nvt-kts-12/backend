package nvt.kts.ticketapp.e2e.pages.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditUserPage {

    private WebDriver driver;

    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;
    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;
    @FindBy(id = "emailInput")
    private WebElement emailInput;
    @FindBy(id = "updateButton")
    private WebElement updateButton;

    public EditUserPage(WebDriver driver) {
        this.driver = driver;
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

    public WebElement getUpdateButton() {
        return updateButton;
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

    public void ensureUpdateButtonIsClickable() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(updateButton));
    }


}
