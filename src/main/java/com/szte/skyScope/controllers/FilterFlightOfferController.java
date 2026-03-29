package com.szte.skyScope.controllers;

import com.szte.skyScope.models.ChosenFilters;
import com.szte.skyScope.services.FilterService;
import com.szte.skyScope.services.SearchStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FilterFlightOfferController {
  private final SearchStore searchStore;
  private final FilterService filterService;

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
  public String resetFilters(@PathVariable String searchId) {
    searchStore.getSearchDatas(searchId).setFilterAttribute(new ChosenFilters());
    searchStore
        .getSearchDatas(searchId)
        .setSearchResult(searchStore.getSearchDatas(searchId).getOriginalSearchResult());
    return "redirect:/resultsPage/" + searchId + "?by=transferTimeAsc";
  }
}
