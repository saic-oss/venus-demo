package com.saic.venus.cucumber.stepdefs.selenium.e2e;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.saic.venus.config.SeleniumProperties;
import com.saic.venus.cucumber.stepdefs.selenium.DriverFactory;
import com.saic.venus.cucumber.stepdefs.selenium.StepDefs;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonStepDefs extends StepDefs {
  private final Logger log = LoggerFactory.getLogger(CommonStepDefs.class);

  @Autowired
  private SeleniumProperties seleniumProperties;

  @Override
  @After
  public void after() {
    DriverFactory.getInstance().removeDriver();
  }

  @Given("The app is ready")
  public void theAppIsReady() {
    theAppIsReadyImpl();
  }

  @Given("I am signed in with username {string} and password {string}")
  public void iAmSignedInWithUsernameUserAndPasswordUser(String username, String password) {
    iAmSignedInWithUsernameAndPasswordImpl(username, password, DriverFactory.getInstance().getDriver());
  }

  @Given("I am on page {string}")
  public void iAmOnPage(String page) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    iAmOnPageImpl(page, driver);
  }

  @When("I navigate to page {string}")
  public void iManuallyNavigateToPage(String page) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    iAmOnPageImpl(page, driver);
  }

  @When("I click the element with id {string}")
  public void iClickTheElementWithId(String elementId) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    iClickTheElementWithIdImpl(elementId, driver);
  }

  @When("I click the element with name {string}")
  public void iClickTheElementWithName(String elementName) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebElement element = driver.findElement(By.name(elementName));
    element.click();
  }

  @When("I fill out input with id {string} with content")
  public void iFillOutInputWithIdStringWithContentString(String inputId, String content) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    iFillOutInputWithIdStringWithContentStringImpl(inputId, content, driver);
  }

  @When("I clear the input with id {string}")
  public void iClearTheInputWithIdString(String inputId) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    iClearTheInputWithIdStringImpl(inputId, driver);
  }

  @When("I wait until element with tagName {string} is gone")
  public void iWaitUntilElementWithIdIsUnobscured(String tagName) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebDriverWait wait = new WebDriverWait(driver, 5);
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName(tagName)));
  }

  @When("I refresh the page")
  public void iRefreshThePage() {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    driver.navigate().refresh();
  }

  @Then("I wait until page {string} is displayed")
  public void iWaitUntilPageIsDisplayed(String page) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebDriverWait wait = new WebDriverWait(driver, 2);
    wait.until(ExpectedConditions.urlToBe(String.format("%s%s", seleniumProperties.getApplication().getRootAddress(), page)));
  }

  @Then("The page {string} is displayed")
  public void thePageIsDisplayed(String page) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    assertThat(driver.getCurrentUrl(), equalTo(String.format("%s%s", seleniumProperties.getApplication().getRootAddress(), page)));
  }

  @Then("The element exists with id {string}")
  public void theElementExistsWithName(String elementId) {
    theElementExistsWithIdImpl(elementId, DriverFactory.getInstance().getDriver());
  }

  @Then("The input with id {string} contains")
  public void theInputWithIdStringContainsString(String inputId, String contents) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    assertThat(driver.findElement(By.id(inputId)).getAttribute("value"), equalTo(contents));
  }

  @Then("The button with id {string} is disabled")
  public void theButtonWithIdStringIsDisabled(String buttonId) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebElement element = driver.findElement(By.id(buttonId));
    assertThat(element.getAttribute("disabled"), equalTo("true"));
  }

  @Then("The button with id {string} is not disabled")
  public void theButtonWithIdStringIsNotDisabled(String buttonId) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebElement element = driver.findElement(By.id(buttonId));
    assertThat(element.getAttribute("disabled"), equalTo(null));
  }

  @When("I wait until input with id {string} exists")
  public void iWaitUntilInputWithIdStringExists(String inputId) {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    WebDriverWait wait = new WebDriverWait(driver, 3);
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(new ByAll(By.id(inputId), By.tagName("input")), 0));
  }
}
