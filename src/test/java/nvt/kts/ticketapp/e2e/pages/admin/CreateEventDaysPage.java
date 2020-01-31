package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CreateEventDaysPage {

    private WebDriver webDriver;

    @FindBy(id = "event-name")
    private WebElement eventName;

    @FindBy(id = "event-description")
    private WebElement eventDescription;

    @FindBy(id = "event-category")
    private WebElement eventCategory;

    @FindBy(xpath = "//p[@id=\'create-event-days\']/app-create-event-day/p/mat-card/div[1]/mat-form-field[1]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private List<WebElement> openDatepickerButtons;

    @FindBy(xpath = "//button[@class=\'mat-calendar-next-button mat-icon-button\']")
    private WebElement goToNextMonthButton;

    @FindBy(xpath = "//td[@class=\'mat-calendar-body-cell ng-star-inserted\']")
    private List<WebElement> datesInNextMonth;

    @FindBy(tagName = "mat-select")
    private List<WebElement> selects;

    @FindBy(xpath = "//*[@id=\"10-1\"]")
    private WebElement selectOptionOne;

    @FindBy(xpath = "//*[@id=\"11-2\"]")
    private WebElement selectOptionTwo;

    @FindBy(id = "sector-rect")
    private List<WebElement> sectors;

    @FindBy(className = "sector-price-input")
    private List<WebElement> sectorPriceInputs;

    @FindBy(className = "sector-capacity-input")
    private List<WebElement> sectorCapacityInputs;

    @FindBy(id = "finish-button")
    private WebElement finishButton;

    public CreateEventDaysPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public WebElement getEventName() {
        return eventName;
    }

    public WebElement getEventDescription() {
        return eventDescription;
    }

    public WebElement getEventCategory() {
        return eventCategory;
    }

    public List<WebElement> getOpenDatepickerButtons() {
        return openDatepickerButtons;
    }

    public WebElement getGoToNextMonthButton() {
        return goToNextMonthButton;
    }

    public List<WebElement> getDatesInNextMonth() {
        return datesInNextMonth;
    }

    public List<WebElement> getSelects() {
        return selects;
    }

    public WebElement getSelectOptionOne() {
        return selectOptionOne;
    }

    public WebElement getSelectOptionTwo() {
        return selectOptionTwo;
    }

    public List<WebElement> getSectors() {
        return sectors;
    }

    public List<WebElement> getSectorCapacityInputs() {
        return sectorCapacityInputs;
    }

    public WebElement getFinishButton() {
        return finishButton;
    }

    public void ensureSectorsAreClickable() {
        for (WebElement sector : sectors) {
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(sector));
        }
    }

    public List<WebElement> getSectorPriceInputs() {
        return sectorPriceInputs;
    }

    public void ensurePriceInputsAreVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfAllElements(sectorPriceInputs));
    }

    public void ensureCapacityInputsAreVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfAllElements(sectorCapacityInputs));
    }

    public void ensureFinishButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(finishButton));
    }
}
