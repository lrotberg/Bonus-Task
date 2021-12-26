import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.concurrent.TimeUnit;

public class SearchPage {

  @FindBy(name = "q")
  protected WebElement searchInput;

  @FindBy(name = "btnK")
  protected WebElement searchButton;

  public WebElement getSearchInput() { return searchInput; }

  public WebElement getSearchButton() { return searchButton; }

  @Step("Search Action")
  public void search(String value, WebDriver driver) {
    getSearchInput().sendKeys(value);
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    getSearchButton().click();
  }
}
