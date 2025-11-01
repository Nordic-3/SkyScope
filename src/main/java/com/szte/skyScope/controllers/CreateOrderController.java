package com.szte.skyScope.controllers;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.*;
import com.szte.skyScope.services.*;
import com.szte.skyScope.utils.FlightOfferFormatter;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
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

  @GetMapping("/createOrder/sumup/{searchId}")
  public String sumup(@PathVariable String searchId, Model model) {
    FlightOfferDTO selectedOffer =
        createFlightOrderService.getSelectedOffer(
            searchStore.getSearchDatas(searchId).getSearchResult(),
            searchStore.getSearchDatas(searchId).getOfferId());
    FinalPriceResponse validatedOffer =
        createFlightOrderService.getFinalPrice(selectedOffer, flightService.getToken());
    selectedOffer
        .getPrice()
        .setTotal(
            FlightOfferFormatter.formatPrice(
                validatedOffer.getData().getFlightOffers().getFirst().getPrice().getTotal()));
    FlightOfferFormatter.formatAndSetSingleOfferDuration(selectedOffer);
    createFlightOrderService.setTravellersName(
        selectedOffer, searchStore.getSearchDatas(searchId).getTravelers());
    model.addAttribute("offer", selectedOffer);
    model.addAttribute("searchId", searchId);
    searchStore
        .getSearchDatas(searchId)
        .setValidatedOffers(validatedOffer.getData().getFlightOffers());
    return "sumupPage";
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
    searchStore.getSearchDatas(searchId).setTravelers(travellers.getTravellers());
    return "redirect:/createOrder/sumup/" + searchId;
  }

  @GetMapping("/createOrder/create/{searchId}")
  public String createOrer(@PathVariable String searchId) {
    CreateOrder createOrder = new CreateOrder();
    createOrder.getData().setTravelers(searchStore.getSearchDatas(searchId).getTravelers());
    createOrder
        .getData()
        .setFlightOffers(searchStore.getSearchDatas(searchId).getValidatedOffers());
    createFlightOrderService.createOrder(createOrder, createFlightOrderService.getTestApiToken());
    return "createOrderSuccess";
  }
}
