package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.InputValidationService;
import com.szte.SkyScope.Services.SearchResultStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Controller
public class FlightSearchController {
    private final InputValidationService inputValidationService;
    private final FlightService flightService;
    private final SearchResultStore searchResultStore;

    @Autowired
    public FlightSearchController(InputValidationService inputValidationService, FlightService flightService, SearchResultStore searchResultStore) {
        this.inputValidationService = inputValidationService;
        this.flightService = flightService;
        this.searchResultStore = searchResultStore;
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
        String searchId = UUID.randomUUID().toString();
        model.addAttribute("searchId", searchId);

        // redirect to loading page, search flights
        flightService.getFlightOffers(flightSearch, flightService.getToken().getAccess_token())
                .thenAccept(result -> searchResultStore.save(searchId,  result));
        return "loading";
    }

    @GetMapping("/results/{searchId}")
    @ResponseBody
    public ResponseEntity<?> checkIfOffersAvaliable(@PathVariable String searchId) {
        List<FlightOffers> result = searchResultStore.get(searchId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("IN_PROGRESS");
        }
        return ResponseEntity.ok("READY");
    }

    @GetMapping("/resultsPage/{searchId}")
    public String resultsPage(@PathVariable String searchId, Model model) {
        model.addAttribute("results", searchResultStore.get(searchId));
        return "flightOffers";
    }
}
