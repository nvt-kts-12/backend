package nvt.kts.ticketapp.e2e.pages.profile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {

    private WebDriver driver;

    @FindBy(id="reservations")
    private WebElement reservations;

    @FindBy(id="boughtTickets")
    private WebElement boughtTickets;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getReservations() {
        return reservations;
    }

    public WebElement getBoughtTickets() {
        return boughtTickets;
    }

}
