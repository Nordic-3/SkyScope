package com.szte.skyScope.controllers;

import com.szte.skyScope.dtos.UserCreationDTO;
import com.szte.skyScope.services.InputValidationService;
import com.szte.skyScope.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SessionHandleController {
  private final InputValidationService inputValidationService;
  private final UserService userService;
  private final RequestCache requestCache;

  @Autowired
  public SessionHandleController(
      InputValidationService inputValidationService,
      UserService userService,
      RequestCache requestCache) {
    this.inputValidationService = inputValidationService;
    this.userService = userService;
    this.requestCache = requestCache;
  }

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("registerUser", new UserCreationDTO("", "", "", "", false));
    return "login";
  }

  @PostMapping("/signup")
  public String signup(
      @ModelAttribute("registerUser") UserCreationDTO userCreationDTO,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request,
      HttpServletResponse response) {
    String validationError = inputValidationService.validatePasswordAndEmail(userCreationDTO);
    if (validationError.isEmpty()) {
      userService.saveUser(userCreationDTO);
      try {
        authWithHttpServletRequest(request, userCreationDTO.email(), userCreationDTO.password());
      } catch (ServletException exception) {
        Logger.getLogger(SessionHandleController.class.getName())
            .log(Level.SEVERE, exception.toString(), exception);
        return "login";
      }
      return "redirect:" + requestCache.getRequest(request, response).getRedirectUrl();
    }
    redirectAttributes.addFlashAttribute("validationError", validationError);
    return "redirect:/login?invalidPassword";
  }

  @GetMapping("/profile")
  public String profile(Model model, Principal principal) {
    model.addAttribute("user", new UserCreationDTO(principal.getName(), "", "", "", false));
    model.addAttribute("confirmPassword", "");
    return "profile";
  }

  @PostMapping("/profile/passwordChange")
  public String passwordChange(
      @ModelAttribute UserCreationDTO user,
      RedirectAttributes redirectAttributes,
      Principal principal) {
    UserCreationDTO updateUser =
        new UserCreationDTO(
            principal.getName(), user.password(), user.rePassword(), user.oldPassword(), true);
    String errors =
        inputValidationService.validatePasswordAndEmail(updateUser)
            + inputValidationService.validateOldPassword(
                user, userService.getUserByEmail(principal.getName()).get().getPassword());
    if (!errors.isEmpty()) {
      redirectAttributes.addFlashAttribute("validationError", errors);
      return "redirect:/profile";
    }
    userService.updateUser(updateUser);
    redirectAttributes.addFlashAttribute("success", true);
    return "redirect:/profile";
  }

  @PostMapping("/profile/delete")
  public String deleteProfile(
      @RequestParam("confirmPassword") String password,
      Principal principal,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String errors =
        inputValidationService.validateOldPassword(
            new UserCreationDTO("", "", "", password, true),
            userService.getUserByEmail(principal.getName()).get().getPassword());
    if (!errors.isEmpty()) {
      redirectAttributes.addFlashAttribute("validationError", errors);
      return "redirect:/profile";
    }
    userService.deleteByEmail(principal.getName());
    logout(request, response);
    return "index";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler()
        .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    return "index";
  }

  @GetMapping("/gdpr")
  public String gdpr() {
    return "gdpr";
  }

  public void authWithHttpServletRequest(
      HttpServletRequest request, String username, String password) throws ServletException {
    request.login(username, password);
  }
}
