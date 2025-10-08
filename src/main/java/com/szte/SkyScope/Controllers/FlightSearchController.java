package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.SearchData;
import com.szte.SkyScope.Services.*;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  private final Logger logger = Logger.getLogger(FlightSearchController.class.getName());

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
    flightService.setIataCodes(flightSearch, flightService.getToken());
    if (!inputValidationService.isValidIataCodes(flightSearch, model)) {
      return "flightSearchPage";
    }
    String searchId = UUID.randomUUID().toString();
    model.addAttribute("searchId", searchId);
    searchStore.saveSearchDatas(searchId, new SearchData());

    flightService
        .getFlightOffers(flightSearch, flightService.getToken(), searchId)
        .thenAccept(
            result -> {
              flightService.setFlightOffersAttributes(result, searchId, flightService.getToken());
              searchStore
                  .getSearchDatas(searchId)
                  .setSearchResult(sortResultService.sortOffersByDeffault(result));
              searchStore
                  .getSearchDatas(searchId)
                  .setOriginalSearchResult(sortResultService.sortOffersByDeffault(result));
            })
        .exceptionally(
            throwable -> {
              logger.log(Level.SEVERE, throwable.getMessage(), throwable);
              searchStore.getSearchDatas(searchId).setSearchResult(new ArrayList<>());
              return null;
            });

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
    if (result.isEmpty()) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("ERROR");
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
    ifUserChoseCheaperOfferShow(cheaper, searchId);
    setModelAttributes(model, searchId, by);
    checkForCheaperOfferAndNotifyUser(searchId);
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

  public void ifUserChoseCheaperOfferShow(String cheaper, String searchId) {
    if (cheaper != null) {
      flightService.setFlightOffersAttributes(
          searchStore.getSearchDatas(searchId).getCheaperSearchResult(),
          searchId,
          flightService.getToken());
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
  }

  public void checkForCheaperOfferAndNotifyUser(String searchId) {
    if (!searchStore.getSearchDatas(searchId).isCheaperOfferAvailable()
        && searchStore.getSearchDatas(searchId).haveToCheckForCheaperOffer()) {
      cheapestFlightDateService
          .checkForCheaperOfferAndGetIt(
              searchStore.getSearchDatas(searchId).getFlightSearch(),
              flightService.getToken(),
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
  }

  public void setModelAttributes(Model model, String searchId, String by) {
    model.addAttribute("flightSearch", searchStore.getSearchDatas(searchId).getFlightSearch());
    model.addAttribute(
        "results",
        FlightOfferFormatter.formatFlightOfferFields(
            searchStore.getSearchDatas(searchId).getSearchResult()));
    model.addAttribute("searchId", searchId);
    model.addAttribute("sortBy", by);
    model.addAttribute("filterOffers", searchStore.getSearchDatas(searchId).getFilterAttribute());
    model.addAttribute(
        "filterOptions",
        filterService.getFilterOptions(searchStore.getSearchDatas(searchId).getSearchResult()));
  }
}
