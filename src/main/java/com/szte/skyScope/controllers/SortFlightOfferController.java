package com.szte.skyScope.controllers;

import com.szte.skyScope.enums.FlightOffersSortOptions;
import com.szte.skyScope.services.SearchStore;
import com.szte.skyScope.services.SortResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SortFlightOfferController {
  private final SortResultService sortResultService;
  private final SearchStore searchStore;

  @Autowired
  public SortFlightOfferController(SortResultService sortResultService, SearchStore searchStore) {
    this.sortResultService = sortResultService;
    this.searchStore = searchStore;
  }

  @GetMapping("/sort/{searchId}")
  public String sort(@PathVariable String searchId, @RequestParam String by, Model model) {
    model.addAttribute("searchId", searchId);
    FlightOffersSortOptions option = FlightOffersSortOptions.getOptionFromValue(by);
    switch (option) {
      case PRICE_ASC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByPriceASC(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
      case PRICE_DSC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByPriceDSC(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
      case FLYTIME_ASC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByFlyTimeAsc(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
      case FLYTIME_DSC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByFlyTimeDsc(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
      case TRANSFERTIME_ASC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByDeffault(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
      case TRANSFERTIME_DSC ->
          searchStore
              .getSearchDatas(searchId)
              .setSearchResult(
                  sortResultService.sortOffersByTransferTimeDsc(
                      searchStore.getSearchDatas(searchId).getSearchResult()));
    }
    return "redirect:/resultsPage/" + searchId + "?by=" + by;
  }
}
