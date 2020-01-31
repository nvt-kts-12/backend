package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.admin.AdminPage;
import nvt.kts.ticketapp.e2e.pages.admin.ReportsPage;
import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTest {

    WebDriver browser;

    LoginPage loginPage;
    HomePage homePage;
    private String url = "http://localhost:4200/login";


    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver","C:\\Users\\marko23\\Downloads\\chromedriver.exe");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate
        browser.navigate().to(this.url);

        loginPage = PageFactory.initElements(browser,LoginPage.class);
        homePage = PageFactory.initElements(browser,HomePage.class);

    }
    
    @Test
    public void LoginTest_EnableButton() throws InterruptedException {
        loginPage.getUsernameInput().sendKeys("user");
        assertFalse(loginPage.getSubmitBtn().isEnabled());
        loginPage.getPasswordInput().sendKeys("User123!");
        assertTrue(loginPage.getSubmitBtn().isEnabled());

        Thread.sleep(1000);
    }

    @Test
    public void LoginTest_OnlyPasswordSent() throws InterruptedException {
        loginPage.getUsernameInput().sendKeys("");
        loginPage.getPasswordInput().sendKeys("1234");
        WebElement errorDiv = browser.findElement(By.id("mat-error-0"));
        String errorMessage = errorDiv.getText();
        assertEquals(errorMessage,"Username is required");
        assertFalse(loginPage.getSubmitBtn().isEnabled());
    }

    @Test
    public void LoginTest_OnlyUsernameSent() throws InterruptedException {
        loginPage.getUsernameInput().sendKeys("user");
        loginPage.getPasswordInput().sendKeys("");
        loginPage.getSubmitBtn().click();
        Thread.sleep(500);
        WebElement errorDiv = browser.findElement(By.id("mat-error-1"));
        String errorMessage = errorDiv.getText();
        assertEquals(errorMessage,"Password field is required");
        assertFalse(loginPage.getSubmitBtn().isEnabled());
    }

    @Test
    public void LoginTest_BadCredentials() throws InterruptedException {
        loginPage.getUsernameInput().sendKeys("aaaa");
        loginPage.getPasswordInput().sendKeys("aaa123!");
        loginPage.getSubmitBtn().click();

        loginPage.ensureSnackbarIsDisplayed();
        WebElement error = loginPage.getSnackbar();
        assertEquals("Bad credentials",error.getText());

    }

    @Test
    public void LoginTest_OK() throws InterruptedException {
        loginPage.getUsernameInput().sendKeys("user");
        loginPage.getPasswordInput().sendKeys("User123!");
        loginPage.getSubmitBtn().click();

        Thread.sleep(1000);

        assertEquals("http://localhost:4200/",browser.getCurrentUrl());
    }

}
