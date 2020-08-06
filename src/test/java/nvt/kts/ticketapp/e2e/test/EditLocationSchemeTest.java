package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.AdminPage;
import nvt.kts.ticketapp.e2e.pages.admin.EditLocationSchemePage;
import nvt.kts.ticketapp.e2e.pages.admin.LocationSchemesListPage;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class EditLocationSchemeTest {

    private WebDriver browser;

    private AdminPage adminPage;
    private LoginPage loginPage;
    private LocationSchemesListPage locationSchemesListPage;
    private EditLocationSchemePage editLocationSchemePage;

    private static final String URL = "http://localhost:4200/login";

    @Before
    public void setUpSelenium() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        browser.navigate().to(URL);

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminPage = PageFactory.initElements(browser, AdminPage.class);
        locationSchemesListPage = PageFactory.initElements(browser, LocationSchemesListPage.class);
        editLocationSchemePage = PageFactory.initElements(browser, EditLocationSchemePage.class);
    }

    private void logIn() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("admin");
        loginPage.setPasswordInput("Admin021!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();
    }

    @Test
    public void editLocationScheme() {
        logIn();

        String expectedUrl = "http://localhost:4200/admin";
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe(expectedUrl));
        assertEquals(expectedUrl, browser.getCurrentUrl());

        adminPage.getEditLocationOption().click();

        String expectedUrl2 = "http://localhost:4200/admin/location-schemes-list";
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe(expectedUrl2));
        assertEquals(expectedUrl2, browser.getCurrentUrl());

        locationSchemesListPage.getLocationSchemeCards().get(2).click();

        editLocationSchemePage.setEditNameInput("edited name");
        editLocationSchemePage.setEditAddressInput("edited address");

        editLocationSchemePage.ensureSaveChangesIsClickable();
        editLocationSchemePage.getSaveChangesButton().click();

        editLocationSchemePage.ensureSnackbarIsVisible();
        assertEquals("Your changes have been saved", editLocationSchemePage.getSnackbar().getText());

        editLocationSchemePage.ensureSnackbarIsInvisible();

        String currentUrl = browser.getCurrentUrl();

        String[] urlParts = currentUrl.split("/");
        String id = urlParts[urlParts.length - 1];

        editLocationSchemePage.getDeleteButton().click();
        editLocationSchemePage.ensureConfirmDeleteButtonIsVisible();
        editLocationSchemePage.getConfirmDeleteButton().click();

        editLocationSchemePage.ensureSnackbarIsVisible();
        if (editLocationSchemePage.getSnackbar().getText().equals("You have successfully deleted the location scheme")) {
            String expectedUrl3 = "http://localhost:4200/admin/location-schemes-list";
            (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe(expectedUrl3));
            assertEquals(expectedUrl3, browser.getCurrentUrl());
        } else {

            String expectedMessage = "Location scheme with id " + id + " can not be deleted because one of its sectors is used in some event.";
            assertEquals(expectedMessage, editLocationSchemePage.getSnackbar().getText());
        }
    }

    @After
    public void closeSelenium() {
        browser.quit();
    }

}
