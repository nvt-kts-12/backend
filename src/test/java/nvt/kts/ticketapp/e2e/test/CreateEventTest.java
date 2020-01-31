package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.AdminPage;
import nvt.kts.ticketapp.e2e.pages.admin.CreateEventDaysPage;
import nvt.kts.ticketapp.e2e.pages.admin.CreateEventPage;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CreateEventTest {

    private WebDriver browser;

    private AdminPage adminPage;
    private LoginPage loginPage;
    private CreateEventPage createEventPage;
    private CreateEventDaysPage createEventDaysPage;

    private static final String URL = "http://localhost:4200/login";

    @Before
    public void setUpSelenium() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        browser.navigate().to(URL);

        adminPage = PageFactory.initElements(browser, AdminPage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        createEventPage = PageFactory.initElements(browser, CreateEventPage.class);
        createEventDaysPage = PageFactory.initElements(browser, CreateEventDaysPage.class);
    }

    private void logIn() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("admin");
        loginPage.setPasswordInput("Admin021!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();
    }

    @Test
    public void CreateEvent() throws InterruptedException {

        logIn();

        (new WebDriverWait(browser, 10)).until(ExpectedConditions.urlToBe("http://localhost:4200/admin"));

        String expectedUrl = "http://localhost:4200/admin";
        assertEquals(expectedUrl, browser.getCurrentUrl());

        adminPage.ensureCreateEventIsClickable();
        adminPage.getCreateEventOption().click();

        String expectedUrl2 = "http://localhost:4200/admin/create-event";
        assertEquals(expectedUrl2, browser.getCurrentUrl());

        String name = "new event name";
        String description = "new description of event";
        String category = "SPORT";

        createEventPage.setEventNameInput(name);
        createEventPage.setEventDescriptionInput(description);

        createEventPage.getEventCategorySelect().click();
        createEventPage.ensureEventCategorySelectOptionsDisplayed();
        WebElement categoryElementOne = createEventPage.getEventCategorySelectOptions().get(0);
        categoryElementOne.click();

        assertEquals(createEventPage.getEventCategorySelect().getText(), category);

        createEventPage.getChooseDatesButton().click();
        createEventPage.ensureNextMonthButtonIsClickable();
        createEventPage.getGoToNextMonthButton().click();

        WebElement dateButton = createEventPage.getDatesInNextMonth().get(9);
        String clickedDateOne = dateButton.getText();
        dateButton.click();

        createEventPage.getChooseDatesButton().click();
        createEventPage.ensureNextMonthButtonIsClickable();
        createEventPage.getGoToNextMonthButton().click();

        WebElement dateButton2 = createEventPage.getDatesInNextMonth().get(10);
        String clickedDateTwo = dateButton2.getText();
        dateButton2.click();

        createEventPage.ensureDatesListIsVisible();

        assertEquals(createEventPage.getPickedDates().size(), 2);

        String pickedDateOne = createEventPage.getPickedDates().get(0).getText().split("-")[2];
        String pickedDateTwo = createEventPage.getPickedDates().get(1).getText().split("-")[2];

        assertEquals(clickedDateOne, pickedDateOne);
        assertEquals(clickedDateTwo, pickedDateTwo);

        createEventPage.ensureNextButtonIsClickable();
        createEventPage.getNextButton().click();

        String expectedUrl3 = "http://localhost:4200/admin/create-event-days";
        assertEquals(expectedUrl3, browser.getCurrentUrl());

        assertEquals(createEventDaysPage.getEventName().getText(), name);
        assertEquals(createEventDaysPage.getEventDescription().getText(), description);
        assertEquals(createEventDaysPage.getEventCategory().getText(), category);

        createEventDaysPage.getOpenDatepickerButtons().get(0).click();
        createEventDaysPage.getGoToNextMonthButton().click();
        createEventDaysPage.ensureGoToPreviousMonthButtonIsClickable();
        createEventDaysPage.getDatesInNextMonth().get(5).click();

        createEventDaysPage.getOpenDatepickerButtons().get(1).click();
        createEventDaysPage.getGoToNextMonthButton().click();
        createEventDaysPage.ensureGoToPreviousMonthButtonIsClickable();
        createEventDaysPage.getDatesInNextMonth().get(6).click();

        createEventDaysPage.getSelects().get(0).click();
        createEventDaysPage.getSelectOptionOne().click();

        createEventDaysPage.getSelects().get(1).click();
        createEventDaysPage.getSelectOptionTwo().click();

        createEventDaysPage.getSectors().get(0).click();
        createEventDaysPage.getSectors().get(createEventDaysPage.getSectors().size()).click();
    }
}
