package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReportsPage {

    private WebDriver driver;

    @FindBy(id ="eventsTab")
    private WebElement eventsTab;

    @FindBy(id = "mat-tab-label-0-1")
    private WebElement locationsTab;

    @FindBy(id = "eventSelect")
    private WebElement eventSelect;

    @FindBy(id = "showBySelect")
    private WebElement showBySelect;

    @FindBy(id="locationSelect")
    private WebElement locationSelect;

    @FindBy(xpath = "//*[@id=\"canvas\"]")
    private WebElement canvas;

    public ReportsPage(WebDriver driver){
        this.driver=driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement getLocationSelect() {
        return locationSelect;
    }

    public WebElement getEventsTab() {
        return eventsTab;
    }

    public WebElement getLocationsTab() {
        return locationsTab;
    }

    public WebElement getEventSelect() {
        return eventSelect;
    }

    public WebElement getShowBySelect() {
        return showBySelect;
    }


    public WebElement getCanvas() {
        return canvas;
    }

    public void ensureEventsTabIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(eventsTab));
    }
    public void ensureLocationsTabIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(locationsTab));
    }
    public void ensureEventSelectIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(eventSelect));
    }
    public void ensureShowBySelectIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(showBySelect));
    }
    public void ensureCanvasIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(canvas));
    }


}
