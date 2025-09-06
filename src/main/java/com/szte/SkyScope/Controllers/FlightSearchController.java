package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.InputValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FlightSearchController {
    private final InputValidationService inputValidationService;
    private final FlightService flightService;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public FlightSearchController(InputValidationService inputValidationService, FlightService flightService, ApplicationConfig applicationConfig) {
        this.inputValidationService = inputValidationService;
        this.flightService = flightService;
        this.applicationConfig = applicationConfig;
    }

    @PostMapping("/flightsearch")
    public String flightSubmit(@ModelAttribute FlightSearch flightSearch, Model model) {
        model.addAttribute("flightsearch", flightSearch);
        if (!inputValidationService.isValidInputDatas(flightSearch, model)) {
            return "flightSearchPage";
        }
        if (applicationConfig.useApis()) {
            flightService.getIataCodeFromApi(flightSearch.getOriginCity(), flightService.getToken().getAccess_token());
        } else {
            flightService.getIataCodeFromJson(flightSearch.getOriginCity());
        }
        return "index";
    }
}
