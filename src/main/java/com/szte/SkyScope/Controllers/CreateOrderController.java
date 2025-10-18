package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Models.*;
import com.szte.SkyScope.Services.*;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
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

  @GetMapping("/createOrder/payment/{searchId}")
  public String payment(@PathVariable String searchId, Model model) {
    FlightOffers selectedOffer =
        createFlightOrderService.getSelectedOffer(
            searchStore.getSearchDatas(searchId).getSearchResult(),
            searchStore.getSearchDatas(searchId).getOfferId());
    FinalPriceResponse validatedOffer =
        createFlightOrderService.getFinalPrice(selectedOffer, flightService.getToken());
    model.addAttribute(
        "price",
        FlightOfferFormatter.formatPrice(
            validatedOffer.getData().getFlightOffers().getFirst().getPrice().getTotal()));
    model.addAttribute("searchId", searchId);
    if (!model.containsAttribute("card")) {
      model.addAttribute("card", new BankCard());
    }
    searchStore
        .getSearchDatas(searchId)
        .setValidatedOffers(validatedOffer.getData().getFlightOffers());
    return "payment";
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
    return "redirect:/createOrder/payment/" + searchId;
  }

  @PostMapping("/createOrder/pay/{searchId}")
  public String pay(
      @ModelAttribute BankCard card,
      @PathVariable String searchId,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request) {
    String errors = inputValidationService.validateCardDetails(card);
    if (!errors.isEmpty()) {
      redirectAttributes.addFlashAttribute("error", errors);
      redirectAttributes.addFlashAttribute("card", card);
      return "redirect:" + request.getHeader("Referer");
    }
    boolean isPaymentSuccessFull = true;
    if (isPaymentSuccessFull) {
      CreateOrder createOrder = new CreateOrder();
      createOrder.getData().setTravelers(searchStore.getSearchDatas(searchId).getTravelers());
      createOrder
          .getData()
          .setFlightOffers(searchStore.getSearchDatas(searchId).getValidatedOffers());
      createFlightOrderService.createOrder(createOrder, createFlightOrderService.getTestApiToken());
    }
    return "redirect:/createOrder/payment/" + searchId + "?success";
  }
}
