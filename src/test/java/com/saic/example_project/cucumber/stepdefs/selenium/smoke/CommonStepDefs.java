package com.saic.venus.cucumber.stepdefs.selenium.smoke;

import com.saic.venus.cucumber.stepdefs.selenium.DriverFactory;
import com.saic.venus.cucumber.stepdefs.selenium.StepDefs;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class CommonStepDefs extends StepDefs {

  @Override
  @After
  protected void after() {
    DriverFactory.getInstance().removeDriver();
  }

  @Given("The app is ready")
  public void the_app_is_ready() {
    theAppIsReadyImpl();
  }

  @Given("I am signed in with username {string} and password {string}")
  public void i_am_signed_in_with_username_and_password(String username, String password) {
    iAmSignedInWithUsernameAndPasswordImpl(username, password, DriverFactory.getInstance().getDriver());
  }

  @Given("I am on page {string}")
  public void i_am_on_page(String page) {
    iAmOnPageImpl(page, DriverFactory.getInstance().getDriver());
  }

  @Then("The element exists with id {string}")
  public void the_element_exists_with_id(String elementId) {
    theElementExistsWithIdImpl(elementId, DriverFactory.getInstance().getDriver());
  }
}
