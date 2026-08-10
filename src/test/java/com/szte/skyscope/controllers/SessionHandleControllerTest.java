package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.KeykloackConfig;
import com.szte.skyscope.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class SessionHandleControllerTest {

  @Mock private UserService userService;

  @Mock private KeykloackConfig keykloackConfig;

  @Mock private Model model;

  @Mock private Principal principal;

  @Mock private HttpServletRequest httpServletRequest;

  @Mock private OidcUser oidcUser;

  private SessionHandleController controller;

  @BeforeEach
  void setUp() {
    controller = new SessionHandleController(userService, keykloackConfig);
  }

  @Test
  void login_ReturnsLoginView() {
    String viewName = controller.login();

    assertThat(viewName).isEqualTo("login");
  }

  @Test
  void profile_ReturnsProfileView_AndAddsUsernameToModel() {
    String dummyUsername = "testUser";
    when(principal.getName()).thenReturn(dummyUsername);

    String viewName = controller.profile(model, principal);

    assertThat(viewName).isEqualTo("profile");
    verify(model).addAttribute("username", dummyUsername);
  }

  @Test
  void passwordChange_RedirectsToKeycloakUrl() {
    when(keykloackConfig.getKcAction()).thenReturn("%s?client_id=%s&redirect_uri=%s");
    when(keykloackConfig.getAuthPasswordChangeUrl()).thenReturn("http://auth/url");
    when(keykloackConfig.getClientId()).thenReturn("my-client-id");

    String expectedRedirectUrl =
        "redirect:http://auth/url?client_id=my-client-id&redirect_uri=http://localhost:8080/profile";

    String viewName = controller.passwordChange();

    assertThat(viewName).isEqualTo(expectedRedirectUrl);
  }

  @Test
  void deleteProfile_Success_DeletesUserAndLogsOut() throws ServletException {
    String subject = "user-subject-123";
    when(oidcUser.getSubject()).thenReturn(subject);

    String viewName = controller.deleteProfile(httpServletRequest, oidcUser);

    assertThat(viewName).isEqualTo("index");
    verify(userService).deleteById(subject);
    verify(httpServletRequest).logout();
  }

  @Test
  void deleteProfile_WhenLogoutThrowsException_CatchesAndReturnsIndex() throws ServletException {
    String subject = "user-subject-123";
    when(oidcUser.getSubject()).thenReturn(subject);

    doThrow(new ServletException("Logout failed")).when(httpServletRequest).logout();

    String viewName = controller.deleteProfile(httpServletRequest, oidcUser);

    assertThat(viewName).isEqualTo("index");
    verify(userService).deleteById(subject);
    verify(httpServletRequest).logout();
  }

  @Test
  void gdpr_ReturnsGdprView() {
    String viewName = controller.gdpr();

    assertThat(viewName).isEqualTo("gdpr");
  }
}
