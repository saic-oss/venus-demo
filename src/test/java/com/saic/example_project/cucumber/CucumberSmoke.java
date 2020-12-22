package com.saic.venus.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/features/smoke", glue = { "com.saic.venus.cucumber.stepdefs.selenium.smoke" })
public class CucumberSmoke {}
