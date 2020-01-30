package nvt.kts.ticketapp.e2e.pages.event;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EventPage {

    private WebDriver driver;

    @FindBy(id="event-name")
    private WebElement eventName;

    @FindBy(id="event-category")
    private WebElement eventCategory;

    @FindBy(tagName = "app-event-day")
    private List<WebElement> eventDays;

    public EventPage(WebDriver driver) {
        this.driver = driver;
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
}
