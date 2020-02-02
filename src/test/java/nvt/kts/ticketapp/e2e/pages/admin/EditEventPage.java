package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class EditEventPage {

    private WebDriver webDriver;

    @FindBy(id = "edit-event-name-input")
    private WebElement editNameInput;

    @FindBy(id = "edit-event-description-input")
    private WebElement editDescriptionInput;

    @FindBy(id = "edit-event-category-select")
    private WebElement editCategorySelect;

    @FindBy(id = "save-event-changes")
    private WebElement saveEventChangesButton;

    @FindBy(xpath = "//mat-option[@class=\"edit-event-category-option mat-option ng-star-inserted\"]")
    private List<WebElement> editCategoryOptions;

    @FindBy(xpath = "//*[@id=\"edit-event-day-date\"]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private WebElement chooseNewDateButton;

    @FindBy(xpath = "//*[@id=\"edit-event-day-expire\"]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private WebElement chooseNewExpireDateButton;

    @FindBy(xpath = "//button[@class=\'mat-calendar-next-button mat-icon-button\']")
    private WebElement goToNextMonthButton;

    @FindBy(xpath = "//td[@class=\'mat-calendar-body-cell ng-star-inserted\']")
    private List<WebElement> datesInNextMonth;

    @FindBy(id = "snackbar")
    private WebElement snackbar;

    @FindBy(id = "edit-event-day-state")
    private WebElement eventDayStateSelect;

    @FindBy(xpath = "//mat-option[@class=\"edit-state-option mat-option ng-star-inserted\"]")
    private List<WebElement> editStateOptions;

    @FindBy(id = "event-day-save-changes")
    private WebElement saveEventDayChanges;

    @FindBy(id = "edit-event-days-title")
    private WebElement editEventDaysTitle;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-backdrop mat-overlay-transparent-backdrop cdk-overlay-backdrop-showing\"")
    private WebElement overlay;

    public EditEventPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getEditNameInput() {
        return editNameInput;
    }

    public WebElement getEditDescriptionInput() {
        return editDescriptionInput;
    }

    public WebElement getEditCategorySelect() {
        return editCategorySelect;
    }

    public void setEditNameInput(String value) {
        WebElement el = getEditNameInput();
        el.clear();
        el.sendKeys(value);
    }

    public void setEditDescriptionInput(String value) {
        WebElement el = getEditDescriptionInput();
        el.clear();
        el.sendKeys(value);
    }

    public List<WebElement> getEditCategoryOptions() {
        return editCategoryOptions;
    }

    public WebElement getSaveEventChangesButton() {
        return saveEventChangesButton;
    }

    public WebElement getSnackbar() {
        return snackbar;
    }

    public WebElement getGoToNextMonthButton() {
        return goToNextMonthButton;
    }

    public List<WebElement> getDatesInNextMonth() {
        return datesInNextMonth;
    }

    public WebElement getChooseNewDateButton() {
        return chooseNewDateButton;
    }

    public WebElement getChooseNewExpireDateButton() {
        return chooseNewExpireDateButton;
    }

    public WebElement getEventDayStateSelect() {
        return eventDayStateSelect;
    }

    public List<WebElement> getEditStateOptions() {
        return editStateOptions;
    }

    public WebElement getSaveEventDayChanges() {
        return saveEventDayChanges;
    }

    public WebElement getEditEventDaysTitle() {
        return editEventDaysTitle;
    }

    public void ensureSaveChangesIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(saveEventChangesButton));
    }

    public void ensureCategoriesAreVisible () {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfAllElements(editCategoryOptions));
    }

    public void ensureSnackbarIsVisible() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(snackbar));
    }

    public void ensureNextMonthButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(goToNextMonthButton));
    }

    public void ensureSaveEventDayChangesIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(saveEventDayChanges));
    }
}
