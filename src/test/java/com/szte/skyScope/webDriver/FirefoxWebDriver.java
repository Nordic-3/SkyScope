package com.szte.skyScope.webDriver;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxWebDriver {
  @Getter private static final WebDriver driver = new FirefoxDriver();

  public static void navigateTo(String url) {
    driver.get(url);
  }

  public static void closeBrowser() {
    driver.quit();
  }
}
