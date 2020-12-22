package com.saic.venus.cucumber.stepdefs.selenium;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.saic.venus.config.SeleniumProperties;
import javax.annotation.Nonnull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StepDefs {
  private final Logger log = LoggerFactory.getLogger(StepDefs.class);

  @Autowired
  private SeleniumProperties seleniumProperties;

  protected abstract void after();

  protected void iAmOnPageImpl(String page, @Nonnull WebDriver driver) {
    driver.get(String.format("%s%s", seleniumProperties.getApplication().getRootAddress(), page));
  }

  protected void iClearTheInputWithIdStringImpl(String inputId, @Nonnull WebDriver driver) {
    WebElement element = driver.findElement(By.id(inputId));
    element.clear();
    element.sendKeys("a", Keys.BACK_SPACE, Keys.TAB);
  }

  protected void iClickTheElementWithIdImpl(String elementId, @Nonnull WebDriver driver) {
    WebElement element = driver.findElement(By.id(elementId));
    element.click();
  }

  protected void iFillOutInputWithIdStringWithContentStringImpl(String inputId, String content, @Nonnull WebDriver driver) {
    WebElement element = driver.findElement(By.id(inputId));
    element.sendKeys(content);
  }

  protected void iWaitUntilElementWithIdStringExistsImpl(String elementId, @Nonnull WebDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, 5);
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.id(elementId), 0));
  }

  protected void theAppIsReadyImpl() {
    int maxTries = 10;
    int numTries = 0;
    while (numTries < maxTries) {
      try {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        iAmOnPageImpl("", driver);
        break;
      } catch (WebDriverException e) {
        log.debug("Selenium failed to get page. Retrying up to max tries.", e);
        numTries++;
      }
    }
  }

  protected void iAmSignedInWithUsernameAndPasswordImpl(String username, String password, WebDriver driver) {
    iAmOnPageImpl("", driver);
    iClickTheElementWithIdImpl("account-menu", driver);
    iClickTheElementWithIdImpl("login", driver);
    iFillOutInputWithIdStringWithContentStringImpl("username", username, driver);
    iFillOutInputWithIdStringWithContentStringImpl("password", password, driver);
    iClickTheElementWithIdImpl("submitLoginBtn", driver);
  }

  protected void theElementExistsWithIdImpl(String elementId, WebDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, 3);
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.id(elementId), 0));
    assertThat(driver.findElements(By.id(elementId)).size() > 0, equalTo(true));
  }
}
