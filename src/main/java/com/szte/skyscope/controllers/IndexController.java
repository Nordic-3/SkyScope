package com.szte.skyscope.controllers;

import com.szte.skyscope.models.FlightSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/search")
  public String flightSearch(Model model) {
    model.addAttribute("flightSearch", new FlightSearch());
    return "flightSearchPage";
  }

  @GetMapping("/flighttracker")
  public String flightTracker() {
    return "flightTrack";
  }
}
