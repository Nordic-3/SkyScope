package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.SearchData;
import com.szte.SkyScope.Services.*;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FlightSearchController {
  private final InputValidationService inputValidationService;
  private final FlightService flightService;
  private final SearchStore searchStore;
  private final SortResultService sortResultService;
  private final FilterService filterService;
  private final CheapestFlightDateService cheapestFlightDateService;

  @Autowired
  public FlightSearchController(
      InputValidationService inputValidationService,
      FlightService flightService,
      SearchStore searchStore,
      SortResultService sortResultService,
      FilterService filterService,
      CheapestFlightDateService cheapestFlightDateService) {
    this.inputValidationService = inputValidationService;
    this.flightService = flightService;
    this.searchStore = searchStore;
    this.sortResultService = sortResultService;
    this.filterService = filterService;
    this.cheapestFlightDateService = cheapestFlightDateService;
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
    searchStore.saveSearchDatas(searchId, new SearchData());

    try {
      flightService
          .getFlightOffers(flightSearch, flightService.getToken().getAccess_token(), searchId)
          .thenAccept(
              result -> {
                setFlightOffersAttributes(result, searchId);
                searchStore
                    .getSearchDatas(searchId)
                    .setSearchResult(sortResultService.sortOffersByDeffault(result));
                searchStore
                    .getSearchDatas(searchId)
                    .setOriginalSearchResult(sortResultService.sortOffersByDeffault(result));
              });
    } catch (Exception e) {
      e.printStackTrace();
    }

    searchStore.getSearchDatas(searchId).setFlightSearch(flightSearch);
    return "loading";
  }

  @GetMapping("/results/{searchId}")
  @ResponseBody
  public ResponseEntity<String> checkIfOffersAvaliable(@PathVariable String searchId) {
    List<FlightOffers> result = searchStore.getSearchDatas(searchId).getSearchResult();
    if (result == null) {
      return ResponseEntity.status(HttpStatus.ACCEPTED).body("IN_PROGRESS");
    }
    return ResponseEntity.ok("READY");
  }

  @GetMapping("/resultsPage/{searchId}")
  public String resultsPage(
      @PathVariable String searchId,
      Model model,
      @ModelAttribute FlightSearch flightSearch,
      @RequestParam String by,
      @RequestParam(required = false) String cheaper) {
    if (cheaper != null) {
      setFlightOffersAttributes(
          searchStore.getSearchDatas(searchId).getCheaperSearchResult(), searchId);
      searchStore
          .getSearchDatas(searchId)
          .setSearchResult(
              sortResultService.sortOffersByDeffault(
                  searchStore.getSearchDatas(searchId).getCheaperSearchResult()));
      searchStore
          .getSearchDatas(searchId)
          .setOriginalSearchResult(
              sortResultService.sortOffersByDeffault(
                  searchStore.getSearchDatas(searchId).getCheaperSearchResult()));
      searchStore
          .getSearchDatas(searchId)
          .setFlightSearch(searchStore.getSearchDatas(searchId).getCheaperSearch());
    }
    model.addAttribute("flightSearch", searchStore.getSearchDatas(searchId).getFlightSearch());
    model.addAttribute(
        "results",
        FlightOfferFormatter.formatFlightOfferFields(
            searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute("searchId", searchId);
    model.addAttribute("sortBy", by);
    model.addAttribute(
        "allAirline",
        filterService.getAirlineNames(searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute("filterOffers", searchStore.getSearchDatas(searchId).getFilterAttribute());
    model.addAttribute(
        "transferNumbers",
        filterService.getTransferNumbers(searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute(
        "transferDurations",
        filterService.getTransferDurations(searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute(
        "airplaneTypes",
        filterService.getAirplanes(searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute(
        "maximumPrice",
        filterService.getMaxPrice(searchStore.getSearchDatas(searchId).getSearchResult()));
    if (!searchStore.getSearchDatas(searchId).isCheaperOfferAvailable()
        && searchStore.getSearchDatas(searchId).haveToCheckForCheaperOffer()) {
      cheapestFlightDateService
          .checkForCheaperOfferAndGetIt(
              searchStore.getSearchDatas(searchId).getFlightSearch(),
              flightService.getToken().getAccess_token(),
              searchId,
              sortResultService.sortOffersByPriceASC(
                  searchStore.getSearchDatas(searchId).getSearchResult()))
          .thenAccept(
              result -> {
                if (result != null) {
                  searchStore.getSearchDatas(searchId).setCheaperSearchResult(result);
                  searchStore.getSearchDatas(searchId).setCheaperOfferAvailable(true);
                } else {
                  searchStore.getSearchDatas(searchId).setHaveToCheckForCheaperOffer(false);
                }
              });
    }
    return "flightOffers";
  }

  @GetMapping("/cheaperOffer/{searchId}")
  @ResponseBody
  public ResponseEntity<String> checkIfCheaperOfferAvaliable(@PathVariable String searchId) {
    if (searchStore.getSearchDatas(searchId).isCheaperOfferAvailable()) {
      return ResponseEntity.ok("READY");
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body("IN_PROGRESS");
  }

  public void setFlightOffersAttributes(List<FlightOffers> result, String searchId) {
    flightService.setAircraftType(
        result, searchStore.getSearchDatas(searchId).getAircraftDictionary());
    flightService.setCarrierNames(
        result, searchStore.getSearchDatas(searchId).getCarrierDictionary());
    flightService.setAirportNames(
        result,
        flightService.getAirportNamesByItsIata(
            searchStore.getSearchDatas(searchId).getLocationDictionary(),
            flightService.getToken().getAccess_token()));
  }
}
