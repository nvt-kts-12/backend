package nvt.kts.ticketapp.e2e.pages.ticketReservation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TicketReservationPage {

    private WebDriver webDriver;

    @FindBy(id = "sector-layout")
    private WebElement sectorLayout;

    @FindBy(id = "seatsTable")
    private WebElement seatsTable;

    @FindBy(id = "parterTable")
    private WebElement parterTable;

    @FindBy(className = "reserve-button")
    private WebElement reserveButton;

    @FindBy(className = "buy-button")
    private WebElement buyButton;

    @FindBy(id = "sectorRect")
    private List<WebElement> sectorsList;


    //*[@id="mat-dialog-1"]/app-confirm-dialog/div[2]/button[2]
//    @FindBy(xpath = "//*[@id=\"mat-dialog-1\"]/app-confirm-dialog/div[2]/button[2]")
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

    public void ensureFirstSectorIsClickable() {
        if (getSectorsList().size() > 0) {
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(getSectorsList().get(0)));
        }
    }

    public void ensureSectorsAreClickable() {
        for (WebElement sector : getSectorsList()) {
            System.out.println("checking sector ");
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(sector));
        }
    }

    public void ensureThatReserveBtnIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(reserveButton));
    }

    public void ensureThatBuyBtnIsDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(buyButton));
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getSectorLayout() {
        return sectorLayout;
    }

    public void setSectorLayout(WebElement sectorLayout) {
        this.sectorLayout = sectorLayout;
    }

    public List<WebElement> getSectorsList() {
        return sectorsList;
    }

    public void setSectorsList(List<WebElement> sectorsList) {
        this.sectorsList = sectorsList;
    }

    public WebElement getSeatsTable() {
        return seatsTable;
    }

    public void setSeatsTable(WebElement seatsTable) {
        this.seatsTable = seatsTable;
    }

    public WebElement getParterTable() {
        return parterTable;
    }

    public void setParterTable(WebElement parterTable) {
        this.parterTable = parterTable;
    }

    public WebElement getReserveButton() {
        return reserveButton;
    }

    public void setReserveButton(WebElement reserveButton) {
        this.reserveButton = reserveButton;
    }

    public WebElement getBuyButton() {
        return buyButton;
    }

    public void setBuyButton(WebElement buyButton) {
        this.buyButton = buyButton;
    }
}
