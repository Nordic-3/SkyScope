package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.*;
import com.szte.skyscope.services.*;
import com.szte.skyscope.utils.FlightOfferFormatter;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ExtendWith(MockitoExtension.class)
class CreateOrderControllerTest {

  @Mock private CreateFlightOrderService createFlightOrderService;
  @Mock private SearchStore searchStore;
  @Mock private FlightService flightService;
  @Mock private InputValidationService inputValidationService;

  @Mock private Model model;
  @Mock private Principal principal;
  @Mock private HttpServletRequest request;
  @Mock private RedirectAttributes redirectAttributes;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private SearchData searchData;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private FlightOfferDTO selectedOffer;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private FinalPriceResponse validatedOffer;

  private CreateOrderController controller;
  private static final String SEARCH_ID = "search123";
  private final List<FlightOfferDTO> dummySearchResults = Collections.emptyList();

  @BeforeEach
  void setUp() {
    controller =
        new CreateOrderController(
            createFlightOrderService, searchStore, flightService, inputValidationService);
  }

  @Test
  void sumup_Success() {
    String dummyToken = "dummyToken";
    String offerId = "offerId";
    String totalUnformatted = "100.00";
    String totalFormatted = "100.00 EUR";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(dummySearchResults);
    when(searchData.getOfferId()).thenReturn(offerId);

    when(createFlightOrderService.getSelectedOffer(dummySearchResults, offerId))
        .thenReturn(selectedOffer);
    when(flightService.getToken()).thenReturn(dummyToken);
    when(createFlightOrderService.getFinalPrice(selectedOffer, dummyToken))
        .thenReturn(validatedOffer);

    when(validatedOffer.getData().getFlightOffers().getFirst().getPrice().getTotal())
        .thenReturn(totalUnformatted);

    try (MockedStatic<FlightOfferFormatter> mockedFormatter =
        mockStatic(FlightOfferFormatter.class)) {
      mockedFormatter
          .when(() -> FlightOfferFormatter.formatPrice(totalUnformatted))
          .thenReturn(totalFormatted);
      String viewName = controller.sumup(SEARCH_ID, model);
      assertThat(viewName).isEqualTo("sumupPage");
      verify(selectedOffer.getPrice()).setTotal(totalFormatted);
      mockedFormatter.verify(
          () -> FlightOfferFormatter.formatAndSetSingleOfferDuration(selectedOffer));
      verify(createFlightOrderService).setTravellersName(eq(selectedOffer), any());
      verify(model).addAttribute("offer", selectedOffer);
      verify(model).addAttribute("searchId", SEARCH_ID);
    }
  }

  @Test
  void travellerDetails_WhenTravellersNotInModel() {
    String offerId = "offer123";
    String userName = "testUser";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(model.containsAttribute("travellers")).thenReturn(false);
    when(principal.getName()).thenReturn(userName);
    when(searchData.getSearchResult()).thenReturn(dummySearchResults);
    when(searchData.getOfferId()).thenReturn(offerId);
    when(createFlightOrderService.getSelectedOffer(dummySearchResults, offerId))
        .thenReturn(selectedOffer);

    String viewName = controller.travellerDetails(SEARCH_ID, offerId, model, principal);

    assertThat(viewName).isEqualTo("travellerDetails");
    verify(searchData).setOfferId(offerId);
    verify(model).addAttribute("searchId", SEARCH_ID);
    verify(createFlightOrderService)
        .setTravellers(any(TravellerWrapper.class), eq(userName), eq(selectedOffer));
    verify(model).addAttribute(eq("travellers"), any(TravellerWrapper.class));
  }

  @Test
  void travellerDetails_WhenTravellersAlreadyInModel() {
    String offerId = "offer123";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(model.containsAttribute("travellers")).thenReturn(true);

    String viewName = controller.travellerDetails(SEARCH_ID, offerId, model, principal);

    assertThat(viewName).isEqualTo("travellerDetails");
    verify(searchData).setOfferId(offerId);
    verify(model).addAttribute("searchId", SEARCH_ID);
    verify(createFlightOrderService, never()).setTravellers(any(), anyString(), any());
  }

  @Test
  void processTravellerDetails_WithValidationErrors_RedirectsBack() {
    TravellerWrapper travellers = new TravellerWrapper();
    String referer = "/previous-page";
    String errorMsg = "Passport number is missing";
    String offerId = "offerId";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(dummySearchResults);
    when(searchData.getOfferId()).thenReturn(offerId);
    when(createFlightOrderService.getSelectedOffer(dummySearchResults, offerId))
        .thenReturn(selectedOffer);
    when(inputValidationService.validateTravellers(travellers, selectedOffer)).thenReturn(errorMsg);
    when(request.getHeader("Referer")).thenReturn(referer);

    String viewName =
        controller.processTravellerDetails(travellers, SEARCH_ID, request, redirectAttributes);

    assertThat(viewName).isEqualTo("redirect:" + referer);
    verify(createFlightOrderService).setPassportValidations(travellers);
    verify(createFlightOrderService).setContacts(travellers);
    verify(redirectAttributes).addFlashAttribute("error", errorMsg);
    verify(redirectAttributes).addFlashAttribute("travellers", travellers);
  }

  @Test
  void processTravellerDetails_WithoutErrors_RedirectsToSumup() {
    TravellerWrapper travellers = new TravellerWrapper();
    String offerId = "offerId";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(dummySearchResults);
    when(searchData.getOfferId()).thenReturn(offerId);
    when(createFlightOrderService.getSelectedOffer(dummySearchResults, offerId))
        .thenReturn(selectedOffer);
    when(inputValidationService.validateTravellers(travellers, selectedOffer))
        .thenReturn(""); // Nincs hiba

    String viewName =
        controller.processTravellerDetails(travellers, SEARCH_ID, request, redirectAttributes);

    assertThat(viewName).isEqualTo("redirect:/createOrder/sumup/" + SEARCH_ID);
    verify(createFlightOrderService).setPassportValidations(travellers);
    verify(createFlightOrderService).setContacts(travellers);
    verify(searchData).setTravelers(travellers.getTravellers());
  }

  @Test
  void createOrder_Success() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getTravelers()).thenReturn(Collections.emptyList());
    when(searchData.getValidatedOffers()).thenReturn(Collections.emptyList());

    String testToken = "testToken123";
    when(createFlightOrderService.getTestApiToken()).thenReturn(testToken);

    String viewName = controller.createOrder(SEARCH_ID);

    assertThat(viewName).isEqualTo("createOrderSuccess");
    verify(createFlightOrderService).createOrder(any(CreateOrder.class), eq(testToken));
  }
}
