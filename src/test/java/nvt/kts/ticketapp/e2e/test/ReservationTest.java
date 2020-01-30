package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.EventPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.ticketReservation.SectorPage;
import nvt.kts.ticketapp.e2e.pages.ticketReservation.TicketReservationPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ReservationTest {
    private WebDriver browser;

    private HomePage homePage;
    private TicketReservationPage ticketReservationPage;
    private SectorPage sectorPage;
    private LoginPage loginPage;
    private EventPage eventPage;

    private static final String BASE_URL = "http://localhost:4200/login";

    @Before
    public void setUpSelenium() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        browser.navigate().to(BASE_URL);

        ticketReservationPage = PageFactory.initElements(browser, TicketReservationPage.class);
        sectorPage = PageFactory.initElements(browser, SectorPage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        homePage = PageFactory.initElements(browser, HomePage.class);
        eventPage = PageFactory.initElements(browser, EventPage.class);
    }

    private void logIn() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("user");
        loginPage.setPasswordInput("User123!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();
    }

    private void goToEventDay() {
        homePage.ensurePaginatorSelectIsClickable();
        homePage.ensureEventBtnIsClickable();

        homePage.getEventBtn().click();

        eventPage.ensureEventDayBtnIsDisplayed();
        eventPage.getEventDayBtn().click();
    }

    @Test
    public void reserveGrandstandSeat_Positive() throws InterruptedException {
        logIn();
        goToEventDay();

        assertEquals("http://localhost:4200/event/2/event-day/3", browser.getCurrentUrl());
        Thread.sleep(5000);

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        ticketReservationPage.ensureSectorsAreClickable();

        if (sectorsList.size() > 0) {
            sectorsList.get(0).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("grandstand")) {
                sectorPage.ensureCancelIsClickable();
                List<WebElement> freeSeats = sectorPage.getSeats();

                if (freeSeats.size() > 0) {
                    freeSeats.get(0).click();
                    sectorPage.ensurePickIsClickable();
                    sectorPage.getPickBtn().click();
                }
            }
        }
        ticketReservationPage.ensureThatReserveBtnIsDisplayed();
        ticketReservationPage.getReserveButton().click();

        ticketReservationPage.ensureConfirmYesIsClickable();
        ticketReservationPage.getConfirmYesBtn().click();

        homePage.ensurePaginatorSelectIsClickable();
    }

    @Test
    public void reserveParter_Positive() throws InterruptedException {
        logIn();
        goToEventDay();

        assertEquals("http://localhost:4200/event/2/event-day/3", browser.getCurrentUrl());
        Thread.sleep(5000);

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        ticketReservationPage.ensureSectorsAreClickable();

        if (sectorsList.size() > 0) {
            sectorsList.get(2).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("parter")) {
                sectorPage.ensureCancelIsClickable();
                sectorPage.setParterInput(String.valueOf(2));

                sectorPage.ensurePickIsClickable();
                sectorPage.getPickBtn().click();
            }
        }
        ticketReservationPage.ensureThatReserveBtnIsDisplayed();
        ticketReservationPage.getReserveButton().click();

        ticketReservationPage.ensureConfirmYesIsClickable();
        ticketReservationPage.getConfirmYesBtn().click();

        homePage.ensurePaginatorSelectIsClickable();
    }

    @Test
    public void reserveParterAndGrandstand_Positive() throws InterruptedException {
        logIn();
        goToEventDay();

        assertEquals("http://localhost:4200/event/2/event-day/3", browser.getCurrentUrl());
        Thread.sleep(5000);

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        ticketReservationPage.ensureSectorsAreClickable();

        if (sectorsList.size() > 0) {
            sectorsList.get(2).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("parter")) {
                sectorPage.ensureCancelIsClickable();
                sectorPage.setParterInput(String.valueOf(2));

                sectorPage.ensurePickIsClickable();
                sectorPage.getPickBtn().click();
            }

            sectorsList.get(0).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("grandstand")) {
                sectorPage.ensureCancelIsClickable();
                List<WebElement> freeSeats = sectorPage.getSeats();

                if (freeSeats.size() > 0) {
                    freeSeats.get(0).click();
                    sectorPage.ensurePickIsClickable();
                    sectorPage.getPickBtn().click();
                }
            }
        }
        ticketReservationPage.ensureThatReserveBtnIsDisplayed();
        ticketReservationPage.getReserveButton().click();

        ticketReservationPage.ensureConfirmYesIsClickable();
        ticketReservationPage.getConfirmYesBtn().click();

        homePage.ensurePaginatorSelectIsClickable();
    }


    @After
    public void quit() {
        browser.quit();
    }

}
