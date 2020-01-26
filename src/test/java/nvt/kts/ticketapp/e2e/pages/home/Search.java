package nvt.kts.ticketapp.e2e.pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Search {

    private WebDriver webDriver;

    @FindBy(id = "search")
    private WebElement search;

    public Search(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getSearch() {
        return search;
    }

    public void setSearch(String value) {
        WebElement el = getSearch();
        el.clear();
        el.sendKeys(value);
    }
}
