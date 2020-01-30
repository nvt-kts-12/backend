package nvt.kts.ticketapp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConfirmDialog {

    private WebDriver driver;

    @FindBy(id = "popupCancelButton")
    private WebElement cancelButton;

    @FindBy(id = "popupOkButton")
    private WebElement okButton;

    public ConfirmDialog(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureCancelButtonClickable() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(cancelButton));
    }

    public void ensureOkButtonClickable() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(okButton));
    }

    public WebElement getCancelButton() {
        return cancelButton;
    }

    public WebElement getOkButton() {
        return okButton;
    }
}
