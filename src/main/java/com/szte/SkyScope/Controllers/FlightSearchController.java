package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.*;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
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
    private final SearchStore searchStore;
    private final SortResultService sortResultService;
    private final FilterService filterService;

    @Autowired
    public FlightSearchController(InputValidationService inputValidationService,
                                  FlightService flightService, SearchStore searchStore,
                                  SortResultService sortResultService, FilterService filterService) {
        this.inputValidationService = inputValidationService;
        this.flightService = flightService;
        this.searchStore = searchStore;
        this.sortResultService = sortResultService;
        this.filterService = filterService;
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

        try {
            flightService.getFlightOffers(flightSearch, flightService.getToken().getAccess_token())
                    .thenAccept(result -> {
                        setFlightOffersAttributes(result);
                        searchStore.saveSearchResult(searchId, sortResultService.sortOffersByDeffault(result));
                        searchStore.saveOriginalSearchResult(searchId, sortResultService.sortOffersByDeffault(result));
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchStore.saveSearchParameters(flightSearch);
        return "loading";
    }

    @GetMapping("/results/{searchId}")
    @ResponseBody
    public ResponseEntity<?> checkIfOffersAvaliable(@PathVariable String searchId) {
        List<FlightOffers> result = searchStore.getSearchResult(searchId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("IN_PROGRESS");
        }
        return ResponseEntity.ok("READY");
    }

    @GetMapping("/resultsPage/{searchId}")
    public String resultsPage(@PathVariable String searchId, Model model, @ModelAttribute FlightSearch flightSearch, @RequestParam String by) {
        model.addAttribute("flightSearch", searchStore.getSearchParameters());
        model.addAttribute("results",
                FlightOfferFormatter.formatFlightOfferFields(searchStore.getSearchResult(searchId)));
        model.addAttribute("searchId", searchId);
        model.addAttribute("sortBy", by);
        model.addAttribute("allAirline", filterService.getAirlineNames(searchStore.getSearchResult(searchId)));
        model.addAttribute("filterOffers", searchStore.getFilterAttribute());
        model.addAttribute("transferNumbers", filterService.getTransferNumbers(searchStore.getSearchResult(searchId)));
        model.addAttribute("transferDurations", filterService.getTransferDurations(searchStore.getSearchResult(searchId)));
        model.addAttribute("airplaneTypes", filterService.getAirplanes(searchStore.getSearchResult(searchId)));
        model.addAttribute("maximumPrice", filterService.getMaxPrice(searchStore.getSearchResult(searchId)));
        return "flightOffers";
    }

    public void setFlightOffersAttributes(List<FlightOffers> result) {
        flightService.setAircraftType(result, searchStore.getAircraftDictionary());
        flightService.setCarrierNames(result, searchStore.getCarrierDictionary());
        flightService.setAirportNames(result, flightService.getAirportNamesByItsIata(
                searchStore.getLocationDictionary(), flightService.getToken().getAccess_token()));
    }
}
