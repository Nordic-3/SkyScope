package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.TravellerWrapper;
import com.szte.SkyScope.Services.*;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreateOrderController {
  private final CreateFlightOrderService createFlightOrderService;
  private final SearchStore searchStore;
  private final FlightService flightService;
  private final InputValidationService inputValidationService;

  @Autowired
  public CreateOrderController(
      CreateFlightOrderService createFlightOrderService,
      SearchStore searchStore,
      FlightService flightService,
      InputValidationService inputValidationService) {
    this.createFlightOrderService = createFlightOrderService;
    this.searchStore = searchStore;
    this.flightService = flightService;
    this.inputValidationService = inputValidationService;
  }

  @GetMapping("/createOrder/bookingDetails/{searchId}")
  public String bookingSumup(@PathVariable String searchId, Model model) {
    FlightOffers validatedOffer =
        createFlightOrderService.getFinalPrice(
            createFlightOrderService.getSelectedOffer(
                searchStore.getSearchDatas(searchId).getSearchResult(),
                searchStore.getSearchDatas(searchId).getOfferId()),
            flightService.getToken());
    flightService.setFlightOffersAttributes(
        Arrays.asList(validatedOffer), searchId, flightService.getToken());
    model.addAttribute(
        "validatedOffer",
        FlightOfferFormatter.formatFlightOfferFields(Arrays.asList(validatedOffer)).getFirst());
    return "bookingSumup";
  }

  @GetMapping("createOrder/travellerDetails/{searchId}")
  public String travellerDetails(
      @PathVariable String searchId,
      @RequestParam String offerId,
      Model model,
      Principal principal) {
    searchStore.getSearchDatas(searchId).setOfferId(offerId);
    model.addAttribute("searchId", searchId);
    if (!model.containsAttribute("travellers")) {
      TravellerWrapper travellers = new TravellerWrapper();
      createFlightOrderService.setTravellers(
          travellers,
          principal.getName(),
          createFlightOrderService.getSelectedOffer(
              searchStore.getSearchDatas(searchId).getSearchResult(),
              searchStore.getSearchDatas(searchId).getOfferId()));
      model.addAttribute("travellers", travellers);
    }
    return "travellerDetails";
  }

  @PostMapping("/travellerDetails/{searchId}")
  public String processTravellerDetails(
      @ModelAttribute TravellerWrapper travellers,
      @PathVariable String searchId,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    createFlightOrderService.setPassportValidations(travellers);
    createFlightOrderService.setContacts(travellers);
    String errors =
        inputValidationService.validateTravellers(
            travellers,
            createFlightOrderService.getSelectedOffer(
                searchStore.getSearchDatas(searchId).getSearchResult(),
                searchStore.getSearchDatas(searchId).getOfferId()));
    if (!errors.isEmpty()) {
      redirectAttributes.addFlashAttribute("error", errors);
      redirectAttributes.addFlashAttribute("travellers", travellers);
      return "redirect:" + request.getHeader("Referer");
    }
    return "redirect:/createOrder/bookingDetails/" + searchId;
  }
}
