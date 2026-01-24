package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.FlightPriceRequest;
import com.szte.skyScope.models.Passport;
import com.szte.skyScope.models.Traveller;
import com.szte.skyScope.models.TravellerWrapper;
import com.szte.skyScope.services.impl.CreateFlightOrderServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateFlightOrderServiceTest {
  @Mock private CachedApiCalls cachedApiCalls;
  @Mock private CreateFlightOrderProvider createFlightOrderProvider;
  @InjectMocks private CreateFlightOrderServiceImpl createFlightOrderService;

  @Test
  void getFinalPrice() {
    FlightOfferDTO flightOffer = new FlightOfferDTO();
    flightOffer.getPrice().setTotal("150.00");

    FinalPriceResponse expected = new FinalPriceResponse();
    when(createFlightOrderProvider.getFinalPrice(any(FlightPriceRequest.class), eq("test-token")))
        .thenReturn(expected);

    FinalPriceResponse result = createFlightOrderService.getFinalPrice(flightOffer, "test-token");
    assertThat(result).isSameAs(expected);

    ArgumentCaptor<FlightPriceRequest> captor = ArgumentCaptor.forClass(FlightPriceRequest.class);
    verify(createFlightOrderProvider).getFinalPrice(captor.capture(), eq("test-token"));

    FlightPriceRequest flightPrice = captor.getValue();
    assertThat(flightPrice).isNotNull();
    assertThat(flightPrice.getData()).isNotNull();
    assertThat(flightPrice.getData().getFlightOffers()).isNotNull();
    assertThat(flightPrice.getData().getFlightOffers().size()).isEqualTo(1);
    assertThat(flightPrice.getData().getFlightOffers().getFirst()).isSameAs(flightOffer);
  }

  @Test
  void getSelectedOffer() {
    FlightOfferDTO offer1 = new FlightOfferDTO();
    offer1.setId("1");
    FlightOfferDTO offer2 = new FlightOfferDTO();
    offer2.setId("2");
    List<FlightOfferDTO> offers = List.of(offer1, offer2);
    FlightOfferDTO result = createFlightOrderService.getSelectedOffer(offers, "2");
    assertThat(result).isSameAs(offer2);
  }

  @Test
  void setTravellers() {
    TravellerWrapper travellerWrapper = new TravellerWrapper();

    FlightOfferDTO flightOffer = new FlightOfferDTO();
    FlightOfferDTO.TravelerPricing travellerPricing = new FlightOfferDTO.TravelerPricing();
    travellerPricing.setTravelerId("1");
    travellerPricing.setTravelerType("ADULT");
    flightOffer.setTravelerPricings(List.of(travellerPricing));

    createFlightOrderService.setTravellers(travellerWrapper, "test@test.com", flightOffer);

    assertThat(travellerWrapper.getTravellers()).hasSize(1);
    Traveller added = travellerWrapper.getTravellers().getFirst();
    assertThat(added.getId()).isEqualTo("1");
    assertThat(added.getType()).isEqualTo("Felnőtt");
    assertThat(added.getContact().getEmailAddress()).isEqualTo("test@test.com");
  }

  @Test
  void setPassportValidations() {
    TravellerWrapper wrapper = new TravellerWrapper();
    Traveller traveller = new Traveller();
    Passport passport = new Passport();
    passport.setIssuanceCountry("hu");

    traveller.setDocuments(List.of(passport));
    wrapper.setTravellers(List.of(traveller));

    createFlightOrderService.setPassportValidations(wrapper);

    assertThat(wrapper.getTravellers().getFirst().getDocuments().getFirst().getValidityCountry())
        .isEqualTo("HU");
  }

  @Test
  void setContacts() {
    TravellerWrapper wrapper = new TravellerWrapper();
    Traveller traveller = new Traveller();
    traveller.getContact().setEmailAddress("first@example.com");
    Traveller traveller2 = new Traveller();
    traveller2.getContact().setEmailAddress("second@example.com");
    wrapper.setTravellers(List.of(traveller, traveller2));

    createFlightOrderService.setContacts(wrapper);

    assertThat(wrapper.getTravellers().get(1).getContact().getEmailAddress())
        .isEqualTo("first@example.com");
  }

  @Test
  void getTestApiToken() {
    AmadeusApiCred cred = new AmadeusApiCred();
    cred.setAccess_token("abc123");
    when(cachedApiCalls.getTestAmadeusApiCred()).thenReturn(cred);
    String token = createFlightOrderService.getTestApiToken();
    assertThat(token).isEqualTo("abc123");
  }

  @Test
  void setTravellersName() {
    FlightOfferDTO selectedOffer = new FlightOfferDTO();
    FlightOfferDTO.TravelerPricing travellerPricing = new FlightOfferDTO.TravelerPricing();
    travellerPricing.setTravelerId("1");
    selectedOffer.setTravelerPricings(List.of(travellerPricing));

    Traveller traveller = new Traveller();
    traveller.setId("1");
    Traveller.Name name = new Traveller.Name();
    name.setFirstName("John");
    name.setLastName("Test");
    traveller.setName(name);

    createFlightOrderService.setTravellersName(selectedOffer, List.of(traveller));

    assertThat(selectedOffer.getTravelerPricings().getFirst().getTraveller()).isEqualTo("Test John");
  }
}
