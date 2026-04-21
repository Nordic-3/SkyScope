package com.szte.skyScope.stepDefinitions;

import com.szte.skyScope.config.KeykloackConfig;
import com.szte.skyScope.config.TestSettings;
import com.szte.skyScope.helper.WebElementHelper;
import com.szte.skyScope.webDriver.FirefoxWebDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.openqa.selenium.By;

@RequiredArgsConstructor
public class HookStepDefs {
  private final KeykloackConfig keykloackConfig;
  private final TestSettings testSettings;
  private final WebElementHelper webElementHelper =
      new WebElementHelper(FirefoxWebDriver.getDriver());

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
    System.out.println(testSettings.getEmail());
    var usersResource =
        keykloackConfig.keycloakAdminClient().realm(keykloackConfig.getRealm()).users();
    if (usersResource.search(testSettings.getEmail()).isEmpty()) {
      createUser(usersResource);
      System.out.println("fut");
    }
  }

  @After
  public void deleteCookeies() {
    FirefoxWebDriver.getDriver().get("http://localhost:8080/logout");
    webElementHelper.clickButton(By.cssSelector("button[type='submit']"));
    FirefoxWebDriver.deleteCookies();
  }

  private void createUser(UsersResource usersResource) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(testSettings.getEmail());
    user.setEmail(testSettings.getEmail());
    user.setFirstName(testSettings.getFirstName());
    user.setLastName(testSettings.getLastName());
    user.setEnabled(true);
    try (var response = usersResource.create(user)) {
      if (response.getStatus() == 201) {
        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(testSettings.getPassword());
        cred.setTemporary(false);

        usersResource.get(userId).resetPassword(cred);
      }
    }
  }
}
