package nvt.kts.ticketapp.e2e.pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class EventPage {

    private WebDriver driver;

    @FindBy(id = "event-name")
    private WebElement eventName;

    @FindBy(id = "event-category")
    private WebElement eventCategory;

    @FindBy(tagName = "app-event-day")
    private List<WebElement> eventDays;

    @FindBy(xpath = "/html/body/app-root/div/app-event-days/div/mat-card/div[2]/app-event-day[2]/div/div/button")
    public WebElement eventDayBtn;


    public EventPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureEventDayBtnIsDisplayed() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(eventDayBtn));
    }

    public WebElement getEventName() {
        return eventName;
    }

    public WebElement getEventCategory() {
        return eventCategory;
    }

    public List<WebElement> getEventDays() {
        return eventDays;
    }

    public WebElement getEventDayBtn() {
        return eventDayBtn;
    }

    public void setEventDayBtn(WebElement eventDayBtn) {
        this.eventDayBtn = eventDayBtn;
    }
}
