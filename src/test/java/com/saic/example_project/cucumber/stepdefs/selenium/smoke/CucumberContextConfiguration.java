package com.saic.venus.cucumber.stepdefs.selenium.smoke;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CucumberContextConfiguration {

  @Before
  public void setup_cucumber_spring_context() {
    // Dummy method so cucumber will recognize this class as glue
    // and use its context configuration.
  }
}
