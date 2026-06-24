package com.szte.skyScope.webDriver;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.Getter;

public class FirefoxWebDriver {
  @Getter private static final Playwright playwright = Playwright.create();
  @Getter private static Browser browser;
  @Getter private static Page page;

  public static void initDriver(boolean isHeadless) {
    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
    page = browser.newPage();
  }

  public static void navigateTo(String url) {
    page.navigate(url);
  }

  public static void closeBrowser() {
    page.close();
  }
}
