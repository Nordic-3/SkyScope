package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.DTOs.UserCreationDTO;
import com.szte.SkyScope.Models.RegisterUser;
import com.szte.SkyScope.Services.InputValidationService;
import com.szte.SkyScope.Services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SessionHandleController {
  private final InputValidationService inputValidationService;
  private final UserService userService;

  @Autowired
  public SessionHandleController(
      InputValidationService inputValidationService, UserService userService) {
    this.inputValidationService = inputValidationService;
    this.userService = userService;
  }

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("registerUser", new RegisterUser());
    return "login";
  }

  @PostMapping("/signup")
  public String signup(
      @ModelAttribute("registerUser") RegisterUser registerUser,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request) {
    String validationError = inputValidationService.validatePasswordAndEmail(registerUser);
    if (validationError.isEmpty()) {
      userService.saveUser(
          new UserCreationDTO(
              "", "", "", registerUser.getEmail(), "", "", registerUser.getPassword()));
      try {
        authWithHttpServletRequest(request, registerUser.getEmail(), registerUser.getPassword());
      } catch (ServletException exception) {
        Logger.getLogger(SessionHandleController.class.getName())
            .log(Level.SEVERE, exception.toString(), exception);
        return "login";
      }
      return "redirect:/login?success";
    }
    redirectAttributes.addFlashAttribute("validationError", validationError);
    return "redirect:/login?invalidPassword";
  }

  public void authWithHttpServletRequest(
      HttpServletRequest request, String username, String password) throws ServletException {
    request.login(username, password);
  }
}
