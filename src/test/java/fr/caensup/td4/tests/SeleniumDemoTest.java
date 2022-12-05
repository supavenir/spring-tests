package fr.caensup.td4.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.github.bonigarcia.wdm.WebDriverManager;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SeleniumDemoTest {

  private WebDriver driver;

  @LocalServerPort
  int randomServerPort;

  String baseUrl;

  @SuppressWarnings("deprecation")
  @BeforeEach
  void setUp() throws Exception {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
    baseUrl = "http://127.0.0.1:" + randomServerPort;
    navigateTo("/hello");
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
  }

  @AfterEach
  void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
    }
  }

  private void navigateTo(String relativeURL) {
    driver.navigate().to(baseUrl + relativeURL);
  }

  private void fillElement(String name, String content) {
    WebElement elm = driver.findElement(By.name(name));
    elm.sendKeys(content);
  }

  private void btnClick(String cssSelector) {
    driver.findElement(ByCssSelector.cssSelector(cssSelector)).click();
  }

  private void assertElementContainsText(String cssSelector, String text) {
    assertTrue(driver.findElement(ByCssSelector.cssSelector(cssSelector)).getText().contains(text));
  }

  private void assertElementAttributeContainsText(String cssSelector, String attribute,
      String text) {
    assertTrue(driver.findElement(ByCssSelector.cssSelector(cssSelector)).getAttribute(attribute)
        .contains(text));
  }

  public void waitForTextToAppear(String textToAppear, WebElement element, int timeout) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeout));
    wait.until(ExpectedConditions.textToBePresentInElement(element, textToAppear));
  }

  public void waitForTextToAppear(String textToAppear, WebElement element) {
    waitForTextToAppear(textToAppear, element, 3000);
  }

  @Test
  void helloRouteShouldReturnBonjour() {
    assertTrue(driver.getCurrentUrl().contains("hello"));
    assertElementContainsText("body", "Bonjour");
  }

  @Test
  void helloWithJsRouteShouldReturnLength() {
    String msg = "Bonjour";
    navigateTo("/hello/js/" + msg);
    assertTrue(driver.getCurrentUrl().contains("/hello/js/" + msg));
    assertElementAttributeContainsText("#msg", "value", msg);
    btnClick("#bt");
    assertElementContainsText("#length", msg.length() + "");
  }

}
