package com.szte.skyScope.stepDefinitions;

import com.szte.skyScope.webDriver.FirefoxWebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class HookStepDefs {

  @After
  public void closeBrowser() {
    FirefoxWebDriver.closeBrowser();
  }

  @Before
  public void initDriver() {
    FirefoxWebDriver.initDriver();
  }
}
