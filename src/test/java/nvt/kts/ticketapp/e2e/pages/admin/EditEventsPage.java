package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EditEventsPage {

    private WebDriver webDriver;

    @FindBy(xpath = "//*[@id=\"event-list\"]/event-component[2]/mat-card/button")
    private WebElement editEventButton;

    public EditEventsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getEditEventButton() {
        return editEventButton;
    }
}
