package com.saic.venus.cucumber.stepdefs.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
  private static final String seleniumServerAddress = "http://localhost:4445/wd/hub";

  private static DriverFactory instance = new DriverFactory();

  private ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(DriverFactory::getWebDriver);

  private DriverFactory() {
    // Do nothing, do not allow initialization of this class
  }

  public static DriverFactory getInstance() {
    return instance;
  }

  public WebDriver getDriver() {
    return driver.get();
  }

  public void removeDriver() {
    driver.get().quit();
    driver.remove();
  }

  private static WebDriver getWebDriver() {
    FirefoxOptions options = new FirefoxOptions();
    options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
    try {
      return new RemoteWebDriver(new URL(seleniumServerAddress), options);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
