package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.event.EventPage;
import nvt.kts.ticketapp.e2e.pages.home.Filter;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.home.Search;
import nvt.kts.ticketapp.e2e.helper.DateAndLocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SearchAndFilterTest {

    private WebDriver browser;
    private HomePage homePage;
    private Search search;
    private Filter filter;
    private EventPage eventPage;

    private String url = "http://localhost:4200/";


    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate

        browser.navigate().to(this.url);

        homePage = PageFactory.initElements(browser, HomePage.class);
        search = PageFactory.initElements(browser, Search.class);
        filter = PageFactory.initElements(browser, Filter.class);
        eventPage = PageFactory.initElements(browser, EventPage.class);
    }

    @After
    public void closeSelenium() {
        browser.quit();
    }

    @Test
    public void searchTest() throws InterruptedException {
        String term = "tezina";

        search.setSearch(term);

        while(true) {
            Thread.sleep(1000);
            List<WebElement> events = homePage.getResultList();

            if(events.size() == 0) {
                // check for no results
                assertEquals("No results", homePage.getNoResults().getText());
            } else {
                for(WebElement event: events) {
                    String title = event.findElement(By.xpath("./mat-card/h1")).getText();
                    assertTrue(title.toLowerCase().contains(term));
                }
                if (!homePage.getPaginatorNextButton().isEnabled()) {
                    break;
                }
                homePage.ensurePaginatorNextButtonIsDisplayed();
                homePage.getPaginatorNextButton().click();
            }
        }
    }

    @Test
    public void filterTypeTest() throws InterruptedException {

        List<WebElement> typeRadioButtons = filter.getTypeRadioButtons();

        for(WebElement button : typeRadioButtons) {

            String buttonLabel = button.findElement(By.className("mat-radio-label")).getText().toUpperCase();
            if (buttonLabel.equals("ALL")) {
                continue;
            }


            WebElement elementToClick = button.findElement(By.xpath("./label/div[1]/div[1]"));

            Actions actions = new Actions(browser);             // added these actions because method used in line that is commented down below is not working fsr

            actions.moveToElement(elementToClick).click().perform();
//            button.findElement(By.xpath("./label/div[1]/div[1]")).click();
            Thread.sleep(1000);
            List<WebElement> events = homePage.getResultList();

            if(events.size() == 0) {
                // check for no results
                assertEquals("No results", homePage.getNoResults().getText());
            } else {
                while(true) {
                    for(WebElement event: events) {
                        String type = event.findElement(By.xpath("./mat-card/p")).getText();
                        assertEquals(buttonLabel, type);
                    }
                    if (!homePage.getPaginatorNextButton().isEnabled()) {
                        break;
                    }
                    homePage.ensurePaginatorNextButtonIsDisplayed();
                    homePage.getPaginatorNextButton().click();
                }
            }
        }
    }

    @Test
    public void filterDateTest() throws InterruptedException {

        filter.getDateButton().click();

        filter.getToday().click();
        Thread.sleep(1000);
        String date = filter.getDateInput().getAttribute("ng-reflect-model");

        String[] tokens = date.split("-");
        String year = tokens[0];
        String month = tokens[1];
        String day = tokens[2];

        Thread.sleep(1000);
        List<WebElement> events = homePage.getResultList();

        if(events.size() == 0) {
            // check for no results
            assertEquals("No results", homePage.getNoResults().getText());
        } else {
            while(true) {

                for(WebElement event: events) {
                    List<WebElement> datesAndLocations = event.findElements(By.xpath("./mat-card/div/p"));

                    String foundDate = "";
                    for(WebElement dateAndLocation : datesAndLocations) {
                        String[] tokens2 = dateAndLocation.getText().split("-")[0].strip().split("/");
                        String day2 = tokens2[0];
                        String month2 = tokens2[1];
                        String year2 = tokens2[2];
                        if (day.equals(day2) && month.equals(month2) && year.equals(year2)) {
                            foundDate = date;
                        }
                    }

                    assertEquals(date, foundDate);
                }
                if (!homePage.getPaginatorNextButton().isEnabled()) {
                    break;
                }
                homePage.ensurePaginatorNextButtonIsDisplayed();
                homePage.getPaginatorNextButton().click();
            }
        }
    }

    @Test
    public void filterLocationTest() throws InterruptedException {

        filter.getLocationSelect().click();

        filter.ensureLocationSelectOptionsDisplayed();
        WebElement locationElement = filter.getLocationSelectOptions().get(1);
        locationElement.click();

        String location = locationElement.getText();

        Thread.sleep(1000);
        List<WebElement> events = homePage.getResultList();

        if(events.size() == 0) {
            // check for no results
            assertEquals("No results", homePage.getNoResults().getText());
        } else {
            while(true) {

                for(WebElement event: events) {
                    List<WebElement> datesAndLocations = event.findElements(By.xpath("./mat-card/div/p"));

                    String foundLocation = "";
                    for(WebElement dateAndLocation : datesAndLocations) {
                        String loc = dateAndLocation.getText().split("-")[1].strip();
                        if (location.equals(loc)) {
                            foundLocation = location;
                        }
                    }

                    assertEquals(location, foundLocation);
                }
                if (!homePage.getPaginatorNextButton().isEnabled()) {
                    break;
                }
                homePage.ensurePaginatorNextButtonIsDisplayed();
                homePage.getPaginatorNextButton().click();
            }
        }
    }

    @Test
    public void moreInformationAboutEvent() throws InterruptedException {

        List<WebElement> events = homePage.getResultList();

        if(events.size() == 0) {
            // check for no results
            assertEquals("No results", homePage.getNoResults().getText());
        } else {
            while(true) {

                for(int i = 0; i <  events.size(); i++) {

                    (new WebDriverWait(browser, 10)).until(ExpectedConditions.visibilityOfAllElements(events));
                    events = homePage.getResultList();
                    WebElement event = events.get(i);

                    String title = event.findElement(By.xpath("./mat-card/h1")).getText();
                    List<WebElement> datesAndLocations = event.findElements(By.xpath("./mat-card/div/p"));

                    List<DateAndLocation> dateAndLocationList = new ArrayList<>();
                    for (WebElement dateAndLocation : datesAndLocations) {
                        String[] tokens = dateAndLocation.getText().split("-");
                        String date = tokens[0].strip();
                        String location = tokens[1].strip();
                        dateAndLocationList.add(new DateAndLocation(date,location));
                    }

                    String category = event.findElement(By.xpath("./mat-card/p")).getText();

                    WebElement moreInformationButton = event.findElement(By.xpath("./mat-card/button"));
                    String[] urlTokens = moreInformationButton.getAttribute("ng-reflect-router-link").split(",");


                    (new WebDriverWait(browser, 10)).until(ExpectedConditions.elementToBeClickable(moreInformationButton));

                    moreInformationButton.click();
                    assertEquals(this.url + urlTokens[0] + urlTokens[1], browser.getCurrentUrl());

                    Thread.sleep(2000);
                    // check main info about event
                    assertEquals(title, eventPage.getEventName().getText());
                    assertEquals(category, eventPage.getEventCategory().getText());

                    List<WebElement> eventDays = eventPage.getEventDays();

                    assertEquals(dateAndLocationList.size(), eventDays.size());

                    // check info of every event day
                    for (int j = 0; j < eventDays.size(); j++) {

                        WebElement eventDay = eventDays.get(j);
                        String eventDayDate = eventDay.findElement(By.xpath("./div/div/h3[1]")).getText();
                        String eventDayLocation = eventDay.findElement(By.xpath("./div/div/h3[2]")).getText();

                        assertEquals(dateAndLocationList.get(j).getDate(), eventDayDate);
                        assertTrue(eventDayLocation.contains(dateAndLocationList.get(j).getLocation()));

                        List<WebElement> tableRows = eventDay.findElements(By.xpath("./div/div/table/tbody/tr"));

                        for (WebElement row : tableRows) {
                            for(WebElement td : row.findElements(By.xpath("./td"))) {
                                assertNotEquals("", td.getText());
                            }
                        }
                    }

                    browser.navigate().back();
                    assertEquals(this.url, browser.getCurrentUrl());

                }
                if (!homePage.getPaginatorNextButton().isEnabled()) {
                    break;
                }
                homePage.ensurePaginatorNextButtonIsDisplayed();
                homePage.getPaginatorNextButton().click();
            }
        }
    }
}
