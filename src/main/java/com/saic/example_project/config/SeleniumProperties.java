package com.saic.venus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "selenium", ignoreUnknownFields = false)
public class SeleniumProperties {
  private final Application application = new Application();

  public Application getApplication() {
    return application;
  }

  public static class Application {
    private String rootAddress;

    public String getRootAddress() {
      return rootAddress;
    }

    public void setRootAddress(String rootAddress) {
      this.rootAddress = rootAddress;
    }
  }
}
