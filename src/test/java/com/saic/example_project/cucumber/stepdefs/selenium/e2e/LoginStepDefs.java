package com.saic.venus.cucumber.stepdefs.selenium.e2e;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.saic.venus.cucumber.stepdefs.selenium.DriverFactory;
import com.saic.venus.cucumber.stepdefs.selenium.StepDefs;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginStepDefs extends StepDefs {

  @After
  public void after() {
    DriverFactory.getInstance().removeDriver();
  }

  @Then("The sign in page is displayed")
  public void theSignInPageIsDisplayed() {
    WebDriver driver = DriverFactory.getInstance().getDriver();
    assertThat(driver.findElements(By.id("submitLoginBtn")).size(), equalTo(1));
  }
}
