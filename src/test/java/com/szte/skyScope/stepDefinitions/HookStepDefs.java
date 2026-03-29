package com.szte.skyScope.stepDefinitions;

import com.szte.skyScope.dtos.UserCreationDTO;
import com.szte.skyScope.services.UserService;
import com.szte.skyScope.webDriver.FirefoxWebDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class HookStepDefs {

  private final UserService userService;

  @Autowired
  public HookStepDefs(UserService userService) {
    this.userService = userService;
  }

  @BeforeAll
  public static void startBrowser() {
    FirefoxWebDriver.initDriver();
  }

  @AfterAll
  public static void closeBrowser() {
    FirefoxWebDriver.closeBrowser();
  }

  @Before(value = "@fill-payment-page or @profile-page-tests or @login or @traveller-detail-form")
  public void setupTestUser() {
    if (userService.getUserByEmail("automataTest@test.hu").isEmpty()) {
      userService.saveUser(
          new UserCreationDTO("automataTest@test.hu", "automatatest", "automatatest", "", true));
    }
  }

  @After
  public void deleteCookeies() {
    FirefoxWebDriver.deleteCookies();
  }
}
