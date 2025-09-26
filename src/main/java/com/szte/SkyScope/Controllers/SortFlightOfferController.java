package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Enums.FlightOffersSortOptions;
import com.szte.SkyScope.Services.SearchStore;
import com.szte.SkyScope.Services.SortResultService;
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

  @GetMapping("/order/{searchId}")
  public String order(@PathVariable String searchId, @RequestParam String by, Model model) {
    model.addAttribute("searchId", searchId);
    FlightOffersSortOptions option = FlightOffersSortOptions.getOptionFromValue(by);
    switch (option) {
      case PRICE_ASC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByPriceASC(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
      case PRICE_DSC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByPriceDSC(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
      case FLYTIME_ASC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByFlyTimeAsc(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
      case FLYTIME_DSC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByFlyTimeDsc(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
      case TRANSFERTIME_ASC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByDeffault(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
      case TRANSFERTIME_DSC:
        searchStore
            .getSearchDatas(searchId)
            .setSearchResult(
                sortResultService.sortOffersByTransferTimeDsc(
                    searchStore.getSearchDatas(searchId).getSearchResult()));
        break;
    }
    return "redirect:/resultsPage/" + searchId + "?by=" + by;
  }
}
