package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.CreateFlightOrderService;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.SearchStore;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreateOrderController {
  private final CreateFlightOrderService createFlightOrderService;
  private final SearchStore searchStore;
  private final FlightService flightService;

  @Autowired
  public CreateOrderController(
      CreateFlightOrderService createFlightOrderService,
      SearchStore searchStore,
      FlightService flightService) {
    this.createFlightOrderService = createFlightOrderService;
    this.searchStore = searchStore;
    this.flightService = flightService;
  }

  @GetMapping("/createOrder/{searchId}")
  public String bookingSumup(
      @PathVariable String searchId, Model model, @RequestParam String offerId) {
    FlightOffers validatedOffer =
        createFlightOrderService.getFinalPrice(
            createFlightOrderService.getSelectedOffer(
                searchStore.getSearchDatas(searchId).getSearchResult(), offerId),
            flightService.getToken());
    flightService.setFlightOffersAttributes(
        Arrays.asList(validatedOffer), searchId, flightService.getToken());
    model.addAttribute(
        "validatedOffer",
        FlightOfferFormatter.formatFlightOfferFields(Arrays.asList(validatedOffer)).getFirst());
    return "bookingSumup";
  }
}
