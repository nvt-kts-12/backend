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

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getResultList() {
        return events;
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
}
