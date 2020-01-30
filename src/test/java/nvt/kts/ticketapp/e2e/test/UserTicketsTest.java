package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.Snackbar;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.login.LoginPage;
import nvt.kts.ticketapp.e2e.pages.ConfirmDialog;
import nvt.kts.ticketapp.e2e.pages.profile.ProfilePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class UserTicketsTest {

    private WebDriver browser;
    private ProfilePage profilePage;
    private LoginPage loginPage;
    private HomePage homePage;
    private ConfirmDialog confirmDialog;
    private Snackbar snackbar;

    private String loginUrl = "http://localhost:4200/login";
    private String profileUrl = "http://localhost:4200/profile";

    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate
        browser.navigate().to(loginUrl);

        profilePage = PageFactory.initElements(browser, ProfilePage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        homePage = PageFactory.initElements(browser, HomePage.class);
        confirmDialog = PageFactory.initElements(browser, ConfirmDialog.class);
        snackbar = PageFactory.initElements(browser, Snackbar.class);
    }

    private void logInAndGoToProfilePage() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("user");
        loginPage.ensurePasswordIsClickable();
        loginPage.setPasswordInput("User123!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();

        WebElement profileButton = homePage.getProfileButton();
        homePage.ensureProfileButtonClicable();
        profileButton.click();
        assertEquals(profileUrl, browser.getCurrentUrl());

    }

    @After
    public void closeSelenium() {
        browser.quit();
    }


    @Test
    public void testReservations() {

        logInAndGoToProfilePage();

        WebElement reservations = profilePage.getReservations();

        List<WebElement> rows = reservations.findElements(By.xpath("./div/table/tbody/tr"));

        List<String> ids = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.xpath("./td"));

            for (int i = 0; i < cols.size(); i++) {
                String colVal = cols.get(i).getText();

                if (i == 0) {
                    assertFalse(ids.contains(colVal));
                    ids.add(colVal);
                }
                if (i == 7 || i == 8) {
                    continue;
                }

                if (i == 5) {
                    assertNotEquals("", colVal);
                    if (colVal.equals("PARTER")) {
                        assertEquals("", cols.get(7).getText());
                        assertEquals("", cols.get(8).getText());
                    }
                } else {
                    assertNotEquals("", colVal);
                }

            }
        }
    }

    @Test
    public void testBoughtTickets() {

        logInAndGoToProfilePage();

        WebElement boughtTickets = profilePage.getBoughtTickets();

        List<WebElement> rows = boughtTickets.findElements(By.xpath("./div/table/tbody/tr"));

        List<String> ids = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.xpath("./td"));

            for (int i = 0; i < cols.size(); i++) {
                String colVal = cols.get(i).getText();

                if (i == 0) {
                    assertFalse(ids.contains(colVal));
                    ids.add(colVal);
                }
                if (i == 7 || i == 8) {
                    continue;
                }

                if (i == 5) {
                    assertNotEquals("", colVal);
                    if (colVal.equals("PARTER")) {
                        assertEquals("", cols.get(7).getText());
                        assertEquals("", cols.get(8).getText());
                    }
                } else {
                    assertNotEquals("", colVal);
                }

            }
        }
    }

    @Test
    public void cancelReservation_not_confirmed()  {
        logInAndGoToProfilePage();

        WebElement reservations = profilePage.getReservations();

        List<WebElement> rows = reservations.findElements(By.xpath("./div/table/tbody/tr"));

        int numberOfRows = rows.size();

        for (WebElement row : rows) {
            WebElement cancelButton = row.findElement(By.className("cancel-reservation-button"));
            (new WebDriverWait(browser, 10)).until(ExpectedConditions.elementToBeClickable(cancelButton));
            cancelButton.click();

            confirmDialog.ensureCancelButtonClickable();
            confirmDialog.getCancelButton().click();

            int newNumberOfRows = reservations.findElements(By.xpath("./div/table/tbody/tr")).size();
            assertEquals(numberOfRows, newNumberOfRows);

        }
    }

    @Test
    public void cancelReservation_confirmed()  {
        logInAndGoToProfilePage();

        WebElement reservations = profilePage.getReservations();

        List<WebElement> rows = reservations.findElements(By.xpath("./div/table/tbody/tr"));

        int numberOfRows = rows.size();

        if (numberOfRows == 0) {
            return;
        }
        WebElement row = rows.get(0);

        WebElement cancelButton = row.findElement(By.className("cancel-reservation-button"));
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelButton.click();

        confirmDialog.ensureOkButtonClickable();
        confirmDialog.getOkButton().click();

        snackbar.ensureSnackbarIsVisible();
        assertEquals("Reservation successfully canceled!", snackbar.getSnackbar().getText());

    }
}
