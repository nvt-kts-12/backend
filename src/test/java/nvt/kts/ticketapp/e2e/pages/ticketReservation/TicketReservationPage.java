package nvt.kts.ticketapp.e2e.pages.ticketReservation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TicketReservationPage {

    private WebDriver webDriver;

    @FindBy(className = "reserve-button")
    private WebElement reserveButton;

    @FindBy(className = "buy-button")
    private WebElement buyButton;

    @FindBy(id = "sectorRect")
    private List<WebElement> sectorsList;

    @FindBy(id = "popupOkButton")
    private WebElement confirmYesBtn;


    public WebElement getConfirmYesBtn() {
        return confirmYesBtn;
    }

    public void ensureConfirmYesIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(confirmYesBtn));
    }

    public TicketReservationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureSectorsAreClickable() {
        for (WebElement sector : getSectorsList()) {
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(sector));
        }
    }

    public void ensureThatReserveBtnIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(reserveButton));
    }

    public void ensureThatBuyBtnIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(buyButton));
    }

    public List<WebElement> getSectorsList() {
        return sectorsList;
    }

    public WebElement getReserveButton() {
        return reserveButton;
    }

    public WebElement getBuyButton() {
        return buyButton;
    }
}
