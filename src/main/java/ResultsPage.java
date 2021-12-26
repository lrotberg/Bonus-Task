import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class ResultsPage {

  @FindBy(xpath = "//div[@id='rso']//a/h3/..")
  protected List<WebElement> listLinks;

  @FindBy(css = "a[class='l']")
  protected List<WebElement> additionalLinks;

  private List<WebElement> getListLinks() { return listLinks; }

  private List<WebElement> getAdditionalLinks() { return additionalLinks; }

  @Step("Get Link List")
  public List<WebElement> getList() {
    List<WebElement> finalList = new ArrayList<>(getListLinks());
    finalList.addAll(1, getAdditionalLinks());

    return finalList;
  }

  @Step("Print Links")
  public void printLinks() {
    for (WebElement link : getList()) {
      String text = link.getText();
      if(!text.isEmpty()) System.out.println(link.getAttribute("href"));
    }
  }

  @Step("Verify Value Present in the first result")
  public void verifyValuePresent(String value) {
    Assert.assertTrue(getList().get(0).getText().contains(value));
  }
}
