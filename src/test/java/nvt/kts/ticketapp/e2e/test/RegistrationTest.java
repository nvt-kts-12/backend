package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.auth.RegistrationPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class RegistrationTest {
    WebDriver browser;

    RegistrationPage registrationPage;

    private String url = "http://localhost:4200/register";


    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver","C:\\Users\\marko23\\Downloads\\chromedriver.exe");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate
        browser.navigate().to(this.url);

        registrationPage = PageFactory.initElements(browser,RegistrationPage.class);
    }

    @Test
    public void allFieldsEmpty(){
        registrationPage.setUsernameInput("");
        registrationPage.setPasswordInput("");
        registrationPage.setFirstNameInput("");
        registrationPage.setLastNameInput("");
        registrationPage.setEmailInput("");
        registrationPage.getRegSubmitButton().click();

        assertEquals("Username is required", browser.findElement(By.id("mat-error-0")).getText());
        assertEquals("Password field is required", browser.findElement(By.id("mat-error-3")).getText());
        assertEquals("First name is required", browser.findElement(By.id("mat-error-1")).getText());
        assertEquals("Last name is required", browser.findElement(By.id("mat-error-2")).getText());
        assertEquals("Email is required", browser.findElement(By.id("mat-error-4")).getText());

        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
    }

    @Test
    public void registerTest_userAlreadyExist() throws InterruptedException {
        registrationPage.setUsernameInput("admin");
        registrationPage.setPasswordInput("Admin123!!");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();

        registrationPage.ensureSnackbarIsDisplayed();
        WebElement error = registrationPage.getSnackbar();
        assertEquals("Username already exist",error.getText());

    }

    @Test
    public void requiredFiled_username() throws InterruptedException {
        registrationPage.setUsernameInput("");
        registrationPage.setPasswordInput("TestPassword021!");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();
        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
        assertEquals("Username is required", browser.findElement(By.id("mat-error-0")).getText());
    }

    @Test
    public void requiredFiled_password(){
        registrationPage.setUsernameInput("user123");
        registrationPage.setPasswordInput("");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();

        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
        assertEquals("Password field is required", browser.findElement(By.id("mat-error-3")).getText());
    }

    @Test
    public void requiredFiled_firstName(){
        registrationPage.setUsernameInput("user123");
        registrationPage.setPasswordInput("User123!");
        registrationPage.setFirstNameInput("");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();

        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
        assertEquals("First name is required", browser.findElement(By.id("mat-error-1")).getText());
    }

    @Test
    public void requiredFiled_lastName() {
        registrationPage.setUsernameInput("user123");
        registrationPage.setPasswordInput("User123!");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();

        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
        assertEquals("Last name is required", browser.findElement(By.id("mat-error-2")).getText());
    }

    @Test
    public void requiredFiled_email() throws InterruptedException {
        registrationPage.setUsernameInput("user123");
        registrationPage.setPasswordInput("User123!");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("");
        registrationPage.getRegSubmitButton().click();

        WebDriverWait wait = new WebDriverWait (browser, 20);
        wait.until(ExpectedConditions.visibilityOf(browser.findElement(By.id("mat-error-4"))));

        assertFalse(registrationPage.getRegSubmitButton().isEnabled());
        assertEquals("Email is required", browser.findElement(By.id("mat-error-4")).getText());
    }

    @Test
    public void passwordFormNotValid(){
        registrationPage.setUsernameInput("test123");
        registrationPage.setPasswordInput("password");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email.com");
        registrationPage.getRegSubmitButton().click();

        registrationPage.ensureIncorrectPassErrIsDisplayed();
        WebElement errorRequired = registrationPage.getMatErrorIncorrectPassword();
        assertEquals("Password must contain minimum 8 characters, at least one uppercase letter," +
                     " one lowercase letter, one number and one special character",errorRequired.getText());
    }

    @Test
    public void emailFormNotValid(){
        registrationPage.setUsernameInput("test123");
        registrationPage.setPasswordInput("Password123!");
        registrationPage.setFirstNameInput("TestFirst");
        registrationPage.setLastNameInput("TestLast");
        registrationPage.setEmailInput("test2@email");
        registrationPage.getRegSubmitButton().click();

        registrationPage.ensureIncorrectEmailErrIsDisplayed();
        WebElement errorRequired = registrationPage.getMatErrorIncorrectEmail();
        assertEquals("Not a valid email",errorRequired.getText());
    }

    @After
    public void quit() {
        browser.quit();
    }
}
