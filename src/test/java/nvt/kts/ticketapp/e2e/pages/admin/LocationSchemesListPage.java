package nvt.kts.ticketapp.e2e.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LocationSchemesListPage {

    private WebDriver webDriver;

    @FindBy(xpath = "//mat-card[@class=\"scheme mat-card ng-star-inserted\"]")
    private List<WebElement> locationSchemeCards;

    public LocationSchemesListPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getLocationSchemeCards() {
        return locationSchemeCards;
    }
}
