package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.InputValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FlightSearchController {
    private final InputValidationService inputValidationService;
    private final FlightService flightService;

    @Autowired
    public FlightSearchController(InputValidationService inputValidationService, FlightService flightService) {
        this.inputValidationService = inputValidationService;
        this.flightService = flightService;
    }

    @PostMapping("/flightsearch")
    public String flightSubmit(@ModelAttribute FlightSearch flightSearch, Model model) {
        model.addAttribute("flightsearch", flightSearch);
        if (!inputValidationService.isValidInputDatas(flightSearch, model)) {
            return "flightSearchPage";
        }
        flightService.setIataCodes(flightSearch, flightService.getToken().getAccess_token());
        if (!inputValidationService.isValidIataCodes(flightSearch, model)) {
            return "flightSearchPage";
        }
        // redirect to loading page, search flights
        return "index";
    }
}
