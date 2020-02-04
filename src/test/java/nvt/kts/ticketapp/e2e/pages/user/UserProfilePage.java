package nvt.kts.ticketapp.e2e.pages.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserProfilePage {

    private WebDriver driver;

    @FindBy(id = "username")
    private WebElement userName;

    @FindBy(id = "firstName")
    private WebElement firstName;

    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id="editProfileButton")
    private WebElement editProfileButton;

    @FindBy(id="snackbar")
    private  WebElement snackbarMessage;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getUserName() {
        return userName;
    }

    public WebElement getFirstName() {
        return firstName;
    }

    public WebElement getSnackbarMessage() {
        return snackbarMessage;
    }

    public WebElement getLastName() {
        return lastName;
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getEditProfileButton() {
        return editProfileButton;
    }

    public void ensureEditProfileButtonIsClickable() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(editProfileButton));
    }

    public void ensureUsernameIsVisible() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(userName));
    }

    public void ensureSnackbarMessageIsVisible() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(snackbarMessage));
    }
}
