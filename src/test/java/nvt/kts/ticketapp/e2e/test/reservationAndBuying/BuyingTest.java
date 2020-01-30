package nvt.kts.ticketapp.e2e.test.reservationAndBuying;

import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.EventPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.paypal.PayPalPage;
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

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
public class BuyingTest {
    private WebDriver browser;

    private TicketReservationPage ticketReservationPage;
    private HomePage homePage;
    private SectorPage sectorPage;
    private LoginPage loginPage;
    private EventPage eventPage;
    private PayPalPage payPalPage;

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
        payPalPage = PageFactory.initElements(browser, PayPalPage.class);
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

    private void doThePayPalWork() throws InterruptedException {
        ticketReservationPage.ensureThatBuyBtnIsDisplayed();
        ticketReservationPage.getBuyButton().click();

        Thread.sleep(1000);
        ticketReservationPage.ensureConfirmYesIsClickable();
        ticketReservationPage.getConfirmYesBtn().click();

        Thread.sleep(10000);
        assertTrue(browser.getCurrentUrl().contains("https://www.sandbox.paypal.com"));
        payPalPage.ensureNextIsClickable();
        payPalPage.setEmailInput("sb-arjxx785590@personal.example.com");
        payPalPage.getBtnNext().click();
        payPalPage.ensureLoginIsClickable();
        payPalPage.setPasswordInput("0202998742015");
        payPalPage.getBtnLogin().click();

        Thread.sleep(10000);
        assertTrue(browser.getCurrentUrl().contains("https://www.sandbox.paypal.com/webapps/hermes"));
        payPalPage.ensureSubmitIsClickable();
        payPalPage.getBtnSubmit().click();

        payPalPage.ensureHomeIsClickable();
        payPalPage.getBtnHome().click();
    }

    @Test()
    public void buyGrandstandSeat_Positive() throws InterruptedException {
        logIn();
        goToEventDay();

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
        doThePayPalWork();
    }

    @Test()
    public void buyParter_Positive() throws InterruptedException {
        logIn();
        goToEventDay();

        Thread.sleep(5000);

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        ticketReservationPage.ensureSectorsAreClickable();

        if (sectorsList.size() > 0) {
            sectorsList.get(2).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("parter")) {
                sectorPage.ensureCancelIsClickable();
                sectorPage.setParterInput(String.valueOf(1));

                sectorPage.ensurePickIsClickable();
                sectorPage.getPickBtn().click();
            }
        }
        doThePayPalWork();
    }

    @Test()
    public void buyParterAndGrandstand() throws InterruptedException {
        logIn();
        goToEventDay();

        Thread.sleep(5000);

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        ticketReservationPage.ensureSectorsAreClickable();

        if (sectorsList.size() > 0) {
            sectorsList.get(2).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("parter")) {
                sectorPage.ensureCancelIsClickable();
                sectorPage.setParterInput(String.valueOf(1));

                sectorPage.ensurePickIsClickable();
                sectorPage.getPickBtn().click();
            }

        }

        ticketReservationPage.ensureSectorsAreClickable();
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
        doThePayPalWork();
    }



    @After
    public void quit() {
        browser.quit();
    }
}
