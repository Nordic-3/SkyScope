package com.szte.skyScope.controllers;

import com.szte.skyScope.config.KeykloackConfig;
import com.szte.skyScope.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SessionHandleController {
  private final UserService userService;
  private final KeykloackConfig keykloackConfig;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/profile")
  public String profile(Model model, Principal principal) {
    model.addAttribute("username", principal.getName());
    return "profile";
  }

  @PostMapping("/profile/passwordChange")
  public String passwordChange() {
    String redirectUrl =
        String.format(
            keykloackConfig.getKcAction(),
            keykloackConfig.getAuthPasswordChangeUrl(),
            keykloackConfig.getClientId(),
            "http://localhost:8080/profile");
    return "redirect:" + redirectUrl;
  }

  @PostMapping("/profile/delete")
  public String deleteProfile(
      HttpServletRequest httpServletRequest, @AuthenticationPrincipal OidcUser oidcUser) {
    userService.deleteById(oidcUser.getSubject());
    try {
      httpServletRequest.logout();
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
    }
    return "index";
  }

  @GetMapping("/gdpr")
  public String gdpr() {
    return "gdpr";
  }
}
