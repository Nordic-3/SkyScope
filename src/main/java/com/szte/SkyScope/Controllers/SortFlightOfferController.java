package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Enums.FlightOffersSortOptions;
import com.szte.SkyScope.Services.SortResultService;
import com.szte.SkyScope.Services.SearchStore;
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
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByPriceASC(
                                searchStore.getSearchResult(searchId)));
                break;
            case PRICE_DSC:
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByPriceDSC(
                                searchStore.getSearchResult(searchId)
                        ));
                break;
            case FLYTIME_ASC:
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByFlyTimeAsc(
                                searchStore.getSearchResult(searchId)
                        ));
                break;
            case FLYTIME_DSC:
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByFlyTimeDsc(
                                searchStore.getSearchResult(searchId)
                        ));
                break;
            case TRANSFERTIME_ASC:
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByDeffault(
                                searchStore.getSearchResult(searchId)
                        ));
                break;
            case TRANSFERTIME_DSC:
                searchStore.saveSearchResult(
                        searchId,
                        sortResultService.sortOffersByTransferTimeDsc(
                                searchStore.getSearchResult(searchId)
                        ));
                break;
        }
        return "redirect:/resultsPage/" + searchId;
    }
}
