package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.*;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EditEventTest {

    private WebDriver browser;

    private AdminPage adminPage;
    private LoginPage loginPage;
    private EditEventsPage editEventsPage;
    private EditEventPage editEventPage;

    private static final String URL = "http://localhost:4200/login";

    @Before
    public void setUpSelenium() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        browser.navigate().to(URL);

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminPage = PageFactory.initElements(browser, AdminPage.class);
        editEventsPage = PageFactory.initElements(browser, EditEventsPage.class);
        editEventPage = PageFactory.initElements(browser, EditEventPage.class);
    }

    private void logIn() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("admin");
        loginPage.setPasswordInput("Admin021!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();
    }


    @Test
    public void editEvent() throws InterruptedException {

        logIn();

        String expectedUrl = "http://localhost:4200/admin";
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe(expectedUrl));
        assertEquals(expectedUrl, browser.getCurrentUrl());

        adminPage.getEditEventOption().click();

        String expectedUrl2 = "http://localhost:4200/admin/edit-events";
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe(expectedUrl2));
        assertEquals(expectedUrl2, browser.getCurrentUrl());

        editEventsPage.getEditEventButton().click();

        editEventPage.setEditNameInput("edited name");
        editEventPage.setEditDescriptionInput("edited description");

        editEventPage.getEditCategorySelect().click();
        editEventPage.ensureCategoriesAreVisible();
        editEventPage.getEditCategoryOptions().get(0).click();

        editEventPage.ensureSaveChangesIsClickable();
        editEventPage.getSaveEventChangesButton().click();

        editEventPage.ensureSnackbarIsVisible();
        assertEquals("You have successfully edited the event.", editEventPage.getSnackbar().getText());

        editEventPage.ensureSnackbarIsInvisible();
        editEventPage.getChooseNewDateButton().click();
        editEventPage.ensureNextMonthButtonIsClickable();
        editEventPage.getGoToNextMonthButton().click();
        editEventPage.getDatesInNextMonth().get(5).click();

        editEventPage.getChooseNewExpireDateButton().click();
        editEventPage.ensureNextMonthButtonIsClickable();
        editEventPage.getGoToNextMonthButton().click();
        editEventPage.getDatesInNextMonth().get(3).click();

        editEventPage.getEventDayStateSelect().click();
        editEventPage.getEditStateOptions().get(0).click();

        editEventPage.ensureSaveEventDayChangesIsClickable();
        editEventPage.getSaveEventDayChanges().click();

        editEventPage.ensureSnackbarIsVisible();
        editEventPage.ensureSnackbarTextHasChanged();
        assertEquals("You have successfully edited the event day",editEventPage.getSnackbar().getText());
    }
}
