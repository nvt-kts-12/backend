package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.event.EventPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.paypal.PayPalPage;
import nvt.kts.ticketapp.e2e.pages.profile.ProfilePage;
import nvt.kts.ticketapp.e2e.pages.ticketReservation.SectorPage;
import nvt.kts.ticketapp.e2e.pages.ticketReservation.TicketReservationPage;
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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ConfirmReservationTest {

    private WebDriver browser;

    private HomePage homePage;
    private TicketReservationPage ticketReservationPage;
    private SectorPage sectorPage;
    private LoginPage loginPage;
    private EventPage eventPage;
    private ProfilePage profilePage;
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
        profilePage = PageFactory.initElements(browser, ProfilePage.class);
        payPalPage = PageFactory.initElements(browser, PayPalPage.class);
    }

    private void logIn() {
        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("user");
        loginPage.setPasswordInput("User123!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();
    }

    private void doThePayPalWork() {

        profilePage.ensureConfirmYesIsClickable();
        profilePage.getConfirmYesBtn().click();

        payPalPage.ensureNextIsClickable();
        assertTrue(browser.getCurrentUrl().contains("https://www.sandbox.paypal.com"));
        payPalPage.setEmailInput("sb-arjxx785590@personal.example.com");
        payPalPage.getBtnNext().click();
        payPalPage.ensureLoginIsClickable();
        payPalPage.setPasswordInput("0202998742015");
        payPalPage.getBtnLogin().click();

        payPalPage.ensureSubmitIsClickable();
        assertTrue(browser.getCurrentUrl().contains("https://www.sandbox.paypal.com/webapps/hermes"));
        payPalPage.getBtnSubmit().click();

        payPalPage.ensureProfileIsClickable();
        payPalPage.getBtnProfile().click();
    }

    @Test
    public void confirmReservation_Positive() throws InterruptedException {
        logIn();

        homePage.ensureProfileButtonClicable();
        reserveParterPlace();

        homePage.ensureProfileButtonClicable();
        homePage.getProfileButton().click();

        assertEquals("http://localhost:4200/profile", browser.getCurrentUrl());

        profilePage.ensureEditProfileButtonIsClickable();

        WebElement reservations = profilePage.getReservations();
        List<WebElement> rows = reservations.findElements(By.xpath("./div/table/tbody/tr"));
        WebElement row = rows.get(0);
        List<WebElement> cols = row.findElements(By.xpath("./td"));

        List<WebElement> buyButton = cols.get(12).findElements(By.xpath("./button[@id='buy-reservation-button']"));
        (new WebDriverWait(browser, 20)).until(ExpectedConditions.elementToBeClickable(buyButton.get(0)));
        buyButton.get(0).click();

        doThePayPalWork();
    }

    private void goToEventDay() throws InterruptedException {

        List<WebElement> events = homePage.getResultList();
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.visibilityOfAllElements(events));

        if (events.size() == 0) {
            assertEquals("No results", homePage.getNoResults().getText());
        } else {
            WebElement event = events.get(0);
            WebElement moreInfoBtn = event.findElement(By.xpath("./mat-card/button"));

            String[] urlTokens = moreInfoBtn.getAttribute("ng-reflect-router-link").split(",");
            (new WebDriverWait(browser, 10)).until(ExpectedConditions.elementToBeClickable(moreInfoBtn));
            moreInfoBtn.click();

            String expectedUrl = "http://localhost:4200/" + urlTokens[0] + urlTokens[1];
            assertEquals(expectedUrl, browser.getCurrentUrl());

            List<WebElement> eventDays = eventPage.getEventDays();
            (new WebDriverWait(browser, 10)).until(ExpectedConditions.visibilityOfAllElements(eventDays));

            if (eventDays.size() == 0) {
                System.out.println("no event days");
            } else {
                WebElement eventDay = eventDays.get(0);
                WebElement continueBtn = eventDay.findElement(By.xpath("./div/div/button"));
                urlTokens = continueBtn.getAttribute("ng-reflect-router-link").split(",");
                (new WebDriverWait(browser, 10)).until(ExpectedConditions.elementToBeClickable(continueBtn));
                continueBtn.click();

                assertEquals(expectedUrl + "/" + urlTokens[0] + "/" + urlTokens[1], browser.getCurrentUrl());
            }
        }
    }

    private void doTheReservationWork() {
        ticketReservationPage.ensureThatReserveBtnIsDisplayed();
        ticketReservationPage.getReserveButton().click();

        ticketReservationPage.ensureConfirmYesIsClickable();
        ticketReservationPage.getConfirmYesBtn().click();

        homePage.ensurePaginatorSelectIsClickable();
    }

    public void reserveParterPlace() throws InterruptedException {
        goToEventDay();

        List<WebElement> sectorsList = ticketReservationPage.getSectorsList();
        (new WebDriverWait(browser, 10)).until(ExpectedConditions.visibilityOfAllElements(sectorsList));
        ticketReservationPage.ensureSectorsAreClickable();

        for (int i = 0; i < sectorsList.size(); i++) {
            sectorsList.get(i).click();
            sectorPage.ensureCancelIsClickable();

            if (sectorPage.getTitle().getText().toLowerCase().contains("parter")) {
                sectorPage.ensureCancelIsClickable();
                sectorPage.setParterInput(String.valueOf(2));

                sectorPage.ensurePickIsClickable();
                sectorPage.getPickBtn().click();
                break;
            } else {
                sectorPage.getCancelBtn().click();
                if (i == sectorsList.size() - 1) {
                    System.out.println("Haven't found a parter sector");
                    fail();
                }
            }
        }
        doTheReservationWork();
    }


    @After
    public void quit() {
        browser.quit();
    }

}
