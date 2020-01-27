package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.home.Filter;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.home.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class SearchAndFilterTest {

    private WebDriver browser;
    private HomePage homePage;
    private Search search;
    private Filter filter;

    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate
        browser.navigate().to("http://localhost:4200");

        homePage = PageFactory.initElements(browser, HomePage.class);
        search = PageFactory.initElements(browser, Search.class);
        filter = PageFactory.initElements(browser, Filter.class);
    }

    @After
    public void closeSelenium() {
        browser.quit();
    }

    @Test
    public void searchTest() throws InterruptedException {
        String term = "film";

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

            button.findElement(By.xpath("./label/div[1]/div[1]")).click();
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
}
