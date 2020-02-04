package nvt.kts.ticketapp.e2e.test;

import nvt.kts.ticketapp.e2e.pages.auth.LoginPage;
import nvt.kts.ticketapp.e2e.pages.home.HomePage;
import nvt.kts.ticketapp.e2e.pages.user.EditUserPage;
import nvt.kts.ticketapp.e2e.pages.user.UserProfilePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;

public class EditProfileTest {
    WebDriver browser;


    LoginPage loginPage;
    UserProfilePage userProfilePage;
    EditUserPage editUserPage;
    HomePage homePage;
    private String url = "http://localhost:4200/login";

    @Before
    public void setupSelenium() {
        // instantiate browser
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        browser = new ChromeDriver();
        // maximize window
        browser.manage().window().maximize();
        // navigate
        browser.navigate().to(this.url);

        userProfilePage = PageFactory.initElements(browser, UserProfilePage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        editUserPage = PageFactory.initElements(browser, EditUserPage.class);
        homePage = PageFactory.initElements(browser,HomePage.class);
    }

    @Test
    public void EditProfileTest() throws InterruptedException {

        loginPage.getUsernameInput().sendKeys("user");
        loginPage.getPasswordInput().sendKeys("User123!");
        loginPage.getSubmitBtn().click();

        Thread.sleep(1000);

        assertEquals("http://localhost:4200/",browser.getCurrentUrl());
        homePage.getProfileIcon().click();

        userProfilePage.ensureUsernameIsVisible();
        assertEquals("user",userProfilePage.getUserName().getText());

        userProfilePage.getEditProfileButton().click();

        //update process
        editUserPage.setFirstNameInput("UserFirstName023");
        editUserPage.setLastNameInput("UserLastName023");
        editUserPage.setEmailInput("user023@gmail.com");
        editUserPage.getUpdateButton().click();

        userProfilePage.ensureUsernameIsVisible();
        userProfilePage.ensureSnackbarMessageIsVisible();
        assertEquals("UserFirstName023",userProfilePage.getFirstName().getText());
        assertEquals("UserLastName023",userProfilePage.getLastName().getText());
        assertEquals("user023@gmail.com",userProfilePage.getEmail().getText());
        assertEquals("You have successfully updated your profile",userProfilePage.getSnackbarMessage().getText());

    }

    @After
    public void quit() {
        browser.quit();
    }
}
