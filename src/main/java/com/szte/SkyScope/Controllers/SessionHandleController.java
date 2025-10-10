package com.szte.SkyScope.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionHandleController {

  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
