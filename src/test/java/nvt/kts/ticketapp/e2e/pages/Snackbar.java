package nvt.kts.ticketapp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Snackbar {

    private WebDriver driver;

    @FindBy(id = "snackbar")
    private WebElement snackbar;

    public Snackbar(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getSnackbar() {
        return snackbar;
    }

    public void ensureSnackbarIsVisible() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(snackbar));
    }
}
