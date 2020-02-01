package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.AdminPage;
import nvt.kts.ticketapp.e2e.pages.admin.ReportsPage;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


@RunWith(SpringRunner.class)
public class ReportsTest {

    WebDriver browser;

    HomePage homePage;
    LoginPage loginPage;
    ReportsPage reportsPage;
    AdminPage adminPage;
    private String url = "http://localhost:4200/";


    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver","C:\\Users\\marko23\\Downloads\\chromedriver.exe");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate

        browser.navigate().to(this.url);

        homePage = PageFactory.initElements(browser, HomePage.class);
        loginPage = PageFactory.initElements(browser,LoginPage.class);
        reportsPage = PageFactory.initElements(browser,ReportsPage.class);
        adminPage = PageFactory.initElements(browser,AdminPage.class);
    }



    @Test
    public void reportsTest() throws InterruptedException {

        List<WebElement> eventSelects;
        List<WebElement> showBySelects;
        List<WebElement> locationSelects;

        homePage.getLoginButton().click();
        assertEquals("http://localhost:4200/login",
                browser.getCurrentUrl());

        loginPage.ensureUsernameIsClickable();
        loginPage.setUsernameInput("admin");
        loginPage.setPasswordInput("Admin021!");
        loginPage.ensureSubmitIsClickable();
        loginPage.getSubmitBtn().click();

        adminPage.ensureReportsButtonIsDisplayed();
        adminPage.getViewReportsButton().click();

        WebElement eventSelect = reportsPage.getEventSelect();
        WebDriverWait wait1 = new WebDriverWait (browser, 20);
        wait1.until(ExpectedConditions.elementToBeClickable(eventSelect));
        eventSelect.click();
        eventSelects = browser.findElements(By.className("eventOptions"));
        if(!eventSelects.isEmpty()){
            eventSelects.get(1).click();
        }else{
            System.out.println("No events in database");
        }

        WebElement showBySelect = reportsPage.getShowBySelect();
        WebDriverWait wait2 = new WebDriverWait (browser, 20);
        wait2.until(ExpectedConditions.elementToBeClickable(showBySelect));
        showBySelect.click();
        showBySelects = browser.findElements(By.className("showByOptions"));
        if(!showBySelects.isEmpty()){
            showBySelects.get(1).click();
        }else{
            System.out.println("No show by options presented");
        }

        assertTrue(reportsPage.getCanvas().isDisplayed());

        WebElement locationsTab = reportsPage.getLocationsTab();
        locationsTab.click();

        Thread.sleep(1000);

        WebElement locationSelect = reportsPage.getLocationSelect();
        WebDriverWait wait3 = new WebDriverWait (browser, 20);
        wait3.until(ExpectedConditions.elementToBeClickable(locationSelect));
        locationSelect.click();
        locationSelects = browser.findElements(By.className("locationOptions"));
        if(!locationSelects.isEmpty()){
            locationSelects.get(1).click();
        }else{
            System.out.println("No locations in database");
        }

        WebDriverWait wait4 = new WebDriverWait (browser, 20);
        wait4.until(ExpectedConditions.elementToBeClickable(reportsPage.getCanvas()));
        assertTrue(reportsPage.getCanvas().isDisplayed());


    }

    @After
    public void quit() {
        browser.quit();
    }

}
