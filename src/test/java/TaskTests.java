import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Map;

public class TaskTests {
  private WebDriver driver;
  private SearchPage searchPage;
  private ResultsPage resultsPage;

  private final String base = "https://reqres.in/";
  private RequestSpecification httpRequest;
  private JsonPath jp;

  @BeforeClass
  public void openWebSession() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions opt = new ChromeOptions();
    opt.addArguments("lang=en-GB");
    driver = new ChromeDriver(opt);
    driver.manage().window().maximize();
    driver.get("https://www.google.com/");
    buildPages();
    initRestAssured();
  }

  @AfterClass
  public void closeSession() { driver.quit(); }

  @Test(description = "Test Google Search Results")
  @Description("This test should search for a given value at on Google," +
          " print the results links and then verify the value is present on the first result")
  public void test01_googleSearch() {
    String searchValue = "Java";
    searchPage.search(searchValue, driver);
    resultsPage.printLinks();
    resultsPage.verifyValuePresent(searchValue);
  }

  @Test(description = "Test Rest API")
  @Description("This test should get all user emails from every page and print them")
  public void test02_restAPI() {
    int numOfPages = httpRequest.get("/api/users").jsonPath().getInt("total_pages");
    for (int i = 0 ; i < numOfPages ; i++) {
      printEmails(i + 1);
    }
  }

  @Step("Initialize Rest")
  public void initRestAssured() {
    RestAssured.baseURI = base;
    httpRequest = RestAssured.given();
    httpRequest.header("Content-Type", "application/json");
  }

  @Step("Build Pages")
  public void buildPages() {
    searchPage = PageFactory.initElements(driver, SearchPage.class);
    resultsPage = PageFactory.initElements(driver, ResultsPage.class);
  }

  @Step("Get Page")
  public Response getPage(int page) {
    return RestAssured.given().get("api/users?page=" + page);
  }

  @Step("Get Users")
  private JsonPath getUsers(int page) {
    return getPage(page).getBody().jsonPath();
  }

  @Step("Print Users Emails")
  public void printEmails(int page) {
    jp = getUsers(page);
    System.out.printf("\nPage #%d:", page);
    ArrayList<Map<String, String>> data = jp.get("data");
    for (Map<String, String> user : data) System.out.printf("\n\t%s", user.get("email"));
  }
}