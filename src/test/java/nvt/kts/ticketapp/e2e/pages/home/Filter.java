package nvt.kts.ticketapp.e2e.pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Filter {

    private WebDriver webDriver;

    @FindBy(xpath = "//*[@id='filter-type-radio-group']/mat-radio-button")
    private List<WebElement> typeRadioButtons;

    @FindBy(xpath = "//*[@id='filter-date-form']//input")
    private WebElement dateInput;

    @FindBy(xpath = "//*[@id=\"filter-date-form\"]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private WebElement dateButton;

    @FindBy(xpath = "//td[@class=\'mat-calendar-body-cell mat-calendar-body-active ng-star-inserted\']")
    private WebElement today;

    @FindBy(id = "filter-location-select")
    private WebElement locationSelect;

    @FindBy(tagName = "mat-option")
    private List<WebElement> locationSelectOptions;

    public Filter(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getTypeRadioButtons() {
        return typeRadioButtons;
    }

    public WebElement getDateButton() {
        return dateButton;
    }

    public WebElement getToday() {
        return today;
    }

    public WebElement getDateInput() {
        return dateInput;
    }

    public WebElement getLocationSelect() {
        return locationSelect;
    }

    public List<WebElement> getLocationSelectOptions() {
        return locationSelectOptions;
    }

    public void ensureLocationSelectOptionsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfAllElements(locationSelectOptions));
    }
}
