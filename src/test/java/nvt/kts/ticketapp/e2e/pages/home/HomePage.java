package nvt.kts.ticketapp.e2e.pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {

    private WebDriver webDriver;

    @FindBy(xpath="//*[@id=\"event-list\"]/*[@id=\"no-results\"]")
    private WebElement noResults;

    @FindBy(xpath = "//*[@id=\"event-list\"]/event-component")
    private List<WebElement> events;

    @FindBy(xpath = "//*[@id=\"paginator\"]/div/div/div[2]/button[2]")
    private WebElement paginatorNextButton;

    @FindBy(id = "profile-button")
    private WebElement profileButton;

    @FindBy(className = "mat-select-arrow-wrapper")
    private WebElement paginatorSelect;

    @FindBy(xpath = "//*[@id=\"event-list\"]/event-component[2]/mat-card/button")
    private WebElement eventBtn;


    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getResultList() {
        return events;
    }

    public void ensureEventBtnIsClickable(){
        (new WebDriverWait(webDriver,10)).until(ExpectedConditions.elementToBeClickable(eventBtn));
    }

    public void ensurePaginatorSelectIsClickable(){
        (new WebDriverWait(webDriver,10)).until(ExpectedConditions.elementToBeClickable(paginatorSelect));
    }

    public void ensurePaginatorNextButtonIsDisplayed() {
        (new WebDriverWait(webDriver,10)).until(ExpectedConditions.elementToBeClickable(paginatorNextButton));
    }

    public WebElement getPaginatorNextButton() {
        return paginatorNextButton;
    }

    public WebElement getNoResults() {
        return noResults;
    }

    public WebElement getProfileButton() {
        return profileButton;
    }

    public void ensureProfileButtonClicable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(profileButton));
    }

    public WebElement getEventBtn() {
        return eventBtn;
    }

    public void setEventBtn(WebElement eventBtn) {
        this.eventBtn = eventBtn;
    }
}
