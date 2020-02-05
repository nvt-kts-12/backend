package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditLocationSchemePage {

    private WebDriver webDriver;

    @FindBy(id = "edit-location-scheme-name")
    private WebElement editNameInput;

    @FindBy(id = "edit-location-scheme-address")
    private WebElement editAddressInput;

    @FindBy(id = "save-location-scheme-changes")
    private WebElement saveChangesButton;

    @FindBy(id = "snackbar")
    private WebElement snackbar;

    @FindBy(id = "delete-button")
    private WebElement deleteButton;

    @FindBy(id = "popupOkButton")
    private WebElement confirmDeleteButton;

    public EditLocationSchemePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getEditNameInput() {
        return editNameInput;
    }

    public WebElement getEditAddressInput() {
        return editAddressInput;
    }

    public WebElement getSaveChangesButton() {
        return saveChangesButton;
    }

    public WebElement getSnackbar() {
        return snackbar;
    }

    public WebElement getDeleteButton() {
        return deleteButton;
    }

    public WebElement getConfirmDeleteButton() {
        return confirmDeleteButton;
    }

    public void setEditNameInput(String value) {
        WebElement el = getEditNameInput();
        el.clear();
        el.sendKeys(value);
    }

    public void setEditAddressInput(String value) {
        WebElement el = getEditAddressInput();
        el.clear();
        el.sendKeys(value);
    }

    public void ensureSaveChangesIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(saveChangesButton));
    }

    public void ensureSnackbarIsVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(snackbar));
    }

    public void ensureConfirmDeleteButtonIsVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(confirmDeleteButton));
    }

    public void ensureSnackbarIsInvisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.invisibilityOf(webDriver.findElement(By.id("snackbar"))));
    }
}