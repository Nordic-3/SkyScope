package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.ChosenFilters;
import com.szte.SkyScope.Services.FilterService;
import com.szte.SkyScope.Services.SearchStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FilterFlightOfferController {
  private final SearchStore searchStore;
  private final FilterService filterService;

  @Autowired
  public FilterFlightOfferController(SearchStore searchStore, FilterService filterService) {
    this.searchStore = searchStore;
    this.filterService = filterService;
  }

  @PostMapping("/filter/{searchId}")
  public String filterOffers(
      @ModelAttribute("filterOffers") ChosenFilters filters,
      @PathVariable String searchId,
      @RequestParam String by) {
    searchStore.getSearchDatas(searchId).setFilterAttribute(filters);
    searchStore
        .getSearchDatas(searchId)
        .setSearchResult(
            filterService.filterOffers(
                searchStore.getSearchDatas(searchId).getSearchResult(), filters));
    return "redirect:/resultsPage/" + searchId + "?by=" + by;
  }

  @PostMapping("filter/reset/{searchId}")
  public String resetFilters(@PathVariable String searchId, @RequestParam String by) {
    searchStore.getSearchDatas(searchId).setFilterAttribute(new ChosenFilters());
    searchStore
        .getSearchDatas(searchId)
        .setSearchResult(searchStore.getSearchDatas(searchId).getOriginalSearchResult());
    return "redirect:/resultsPage/" + searchId + "?by=" + by;
  }
}
