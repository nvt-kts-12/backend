package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CreateEventPage {

    private WebDriver webDriver;

    @FindBy(id = "event_name_input")
    private WebElement eventNameInput;

    @FindBy(id = "event_description_input")
    private WebElement eventDescriptionInput;

    @FindBy(id = "event_category_select")
    private WebElement eventCategorySelect;

    @FindBy(tagName = "mat-option")
    private List<WebElement> eventCategorySelectOptions;

    @FindBy(xpath = "//*[@id=\"choose_date_form\"]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private WebElement chooseDatesButton;

    @FindBy(xpath = "//button[@class=\'mat-calendar-next-button mat-icon-button\']")
    private WebElement goToNextMonthButton;

    @FindBy(xpath = "//td[@class=\'mat-calendar-body-cell ng-star-inserted\']")
    private List<WebElement> datesInNextMonth;

    @FindBy(xpath = "//div[@class=\'dates\']/mat-card")
    private List<WebElement> pickedDates;

    @FindBy(id = "next_button")
    private WebElement nextButton;

    @FindBy(id = "dates-list")
    private WebElement datesList;

    public CreateEventPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getEventNameInput() {
        return eventNameInput;
    }

    public WebElement getEventDescriptionInput() {
        return eventDescriptionInput;
    }

    public WebElement getEventCategorySelect() {
        return eventCategorySelect;
    }

    public WebElement getChooseDatesButton() {
        return chooseDatesButton;
    }

    public WebElement getNextButton() {
        return nextButton;
    }

    public WebElement getGoToNextMonthButton() {
        return goToNextMonthButton;
    }

    public List<WebElement> getDatesInNextMonth() {
        return datesInNextMonth;
    }

    public List<WebElement> getPickedDates() {
        return pickedDates;
    }

    public List<WebElement> getEventCategorySelectOptions() {
        return eventCategorySelectOptions;
    }

    public WebElement getDatesList() {
        return datesList;
    }

    public void setEventNameInput(String value) {
        WebElement el = getEventNameInput();
        el.clear();
        el.sendKeys(value);
    }

    public void setEventDescriptionInput(String value) {
        WebElement el = getEventDescriptionInput();
        el.clear();
        el.sendKeys(value);
    }

    public void ensureNextMonthButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(goToNextMonthButton));
    }

    public void ensureEventCategorySelectOptionsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfAllElements(eventCategorySelectOptions));
    }

    public void ensureNextButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(nextButton));
    }

    public void ensureDatesListIsVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(datesList));
    }
}
