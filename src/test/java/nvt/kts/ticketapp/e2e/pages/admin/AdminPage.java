package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminPage {

    private WebDriver driver;

    @FindBy(id = "create_event")
    private WebElement createEventButton;

    @FindBy(id = "edit_event")
    private WebElement editEventButton;

    @FindBy(id = "create_location")
    private WebElement addLocationButton;

    @FindBy(id = "edit_location")
    private WebElement editLocationButton;

    @FindBy(id = "bar_chart")
    private WebElement viewReportsButton;


    public AdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement getCreateEventButton() {
        return createEventButton;
    }

    public WebElement getEditEventButton() {
        return editEventButton;
    }

    public WebElement getAddLocationButton() {
        return addLocationButton;
    }

    public WebElement getEditLocationButton() {
        return editLocationButton;
    }

    public WebElement getViewReportsButton() {
        return viewReportsButton;
    }

    public void ensureReportsButtonIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(viewReportsButton));
    }
    public void ensureCreateEventButtonIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(createEventButton));
    }
    public void ensureEditEventButtonIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(editEventButton));
    }
    public void ensureAddLocationButtonIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(addLocationButton));
    }
    public void ensureEditLocationButtonIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(editLocationButton));
    }
}
