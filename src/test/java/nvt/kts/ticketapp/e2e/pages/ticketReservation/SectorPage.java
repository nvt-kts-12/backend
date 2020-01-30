package nvt.kts.ticketapp.e2e.pages.ticketReservation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SectorPage {

    private WebDriver webDriver;

    @FindBy(className = "mat-dialog-title")
    private WebElement title;

    @FindBy(className = "freeSeat")
    private List<WebElement> seats;

    @FindBy(id = "cancelBtn")
    private WebElement cancelBtn;

    @FindBy(id = "pickBtn")
    private WebElement pickBtn;

    @FindBy(id = "parterInput")
    private WebElement parterInput;

    public void ensureCancelIsClickable(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(cancelBtn));
    }

    public void ensurePickIsClickable(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(pickBtn));
    }
    public WebElement getCancelBtn() {
        return cancelBtn;
    }

    public WebElement getPickBtn() {
        return pickBtn;
    }

    public SectorPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getSeats() {
        return seats;
    }

    public WebElement getTitle() {
        return title;
    }

    public WebElement getParterInput() {
        return parterInput;
    }

    public void setParterInput(String parterInput) {
        WebElement el = getParterInput();
        el.clear();
        el.sendKeys(parterInput);
    }
}
