package com.szte.skyScope.stepDefinitions;

import com.szte.skyScope.config.KeykloackConfig;
import com.szte.skyScope.config.TestSettings;
import com.szte.skyScope.webDriver.FirefoxWebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

@RequiredArgsConstructor
public class HookStepDefs {
  private final KeykloackConfig keykloackConfig;
  private final TestSettings testSettings;

  @Before
  public void startBrowser() {
    FirefoxWebDriver.initDriver(testSettings.isHeadless(), testSettings.getDefaultTimeout());
  }

  @Before(value = "@fill-payment-page or @profile-page-tests or @login or @traveller-detail-form")
  public void setupTestUser() {
    var usersResource =
        keykloackConfig.keycloakAdminClient().realm(keykloackConfig.getRealm()).users();
    if (usersResource.search(testSettings.getEmail()).isEmpty()) {
      createUser(usersResource);
    }
  }

  @After
  public void deleteCookeies() {
    FirefoxWebDriver.closeBrowser();
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
