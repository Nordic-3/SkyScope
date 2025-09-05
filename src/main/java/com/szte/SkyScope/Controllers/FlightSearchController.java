package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.InputValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FlightSearchController {
    private final InputValidationService inputValidationService;

    @Autowired
    public FlightSearchController(InputValidationService inputValidationService) {
        this.inputValidationService = inputValidationService;
    }

    @PostMapping("/flightsearch")
    public String flightSubmit(@ModelAttribute FlightSearch flightSearch, Model model) {
        model.addAttribute("flightsearch", flightSearch);
        if (!inputValidationService.isValidInputDatas(flightSearch, model)) {
            return "flightSearchPage";
        }
        return "index";
    }
}
