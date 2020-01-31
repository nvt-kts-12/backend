package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminPage {

    private WebDriver webDriver;

    @FindBy(id = "create_event")
    private WebElement createEventOption;

    @FindBy(id = "edit_event")
    private WebElement editEventOption;

    @FindBy(id = "create_location")
    private WebElement createLocationOption;

    @FindBy(id = "edit_location")
    private WebElement editLocationOption;

    public AdminPage(WebDriver webDriver) { this.webDriver = webDriver; }

    public void ensureCreateEventIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(createEventOption));
    }

    public void ensureEditEventIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(editEventOption));
    }

    public void ensureCreateLocationIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(createLocationOption));
    }

    public void ensureEditLocationIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(editLocationOption));
    }

    public WebElement getCreateEventOption() {
        return createEventOption;
    }

    public WebElement getEditEventOption() {
        return editEventOption;
    }

    public WebElement getCreateLocationOption() {
        return createLocationOption;
    }

    public WebElement getEditLocationOption() {
        return editLocationOption;
    }
}
