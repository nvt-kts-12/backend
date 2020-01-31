package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.AdminPage;
import nvt.kts.ticketapp.e2e.pages.admin.ReportsPage;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.EventPage;
import nvt.kts.ticketapp.e2e.pages.home.Filter;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.home.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.junit4.SpringRunner;

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
        WebDriverWait wait1 = new WebDriverWait (browser, 20);
        WebDriverWait wait2 = new WebDriverWait (browser, 20);
        WebDriverWait wait3 = new WebDriverWait (browser, 20);
        WebDriverWait wait4 = new WebDriverWait (browser, 20);

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
        wait1.until(ExpectedConditions.elementToBeClickable(eventSelect));
        eventSelect.click();
        WebElement eventSelectOption = browser.findElement(By.id("1"));
        eventSelectOption.click();

        WebElement showBySelect = reportsPage.getShowBySelect();
        wait2.until(ExpectedConditions.elementToBeClickable(showBySelect));
        showBySelect.click();
        WebElement showByOption = browser.findElement(By.id("selectOption1"));
        showByOption.click();

        assertTrue(reportsPage.getCanvas().isDisplayed());

        WebElement locationsTab = reportsPage.getLocationsTab();
        locationsTab.click();

        Thread.sleep(1000);

        WebElement locationSelect = reportsPage.getLocationSelect();
        wait3.until(ExpectedConditions.elementToBeClickable(locationSelect));
        locationSelect.click();
        WebElement locationOption = browser.findElement(By.id("2"));
        locationOption.click();

        wait4.until(ExpectedConditions.elementToBeClickable(reportsPage.getCanvas()));
        assertTrue(reportsPage.getCanvas().isDisplayed());
    }

    @After
    public void quit() {
        browser.quit();
    }

}
