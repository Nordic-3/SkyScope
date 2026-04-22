package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.enums.TravellerTypes;
import com.szte.skyScope.models.*;
import com.szte.skyScope.services.CachedApiCalls;
import com.szte.skyScope.services.CreateFlightOrderProvider;
import com.szte.skyScope.services.CreateFlightOrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFlightOrderServiceImpl implements CreateFlightOrderService {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();
  private final CachedApiCalls cachedApiCalls;
  private final CreateFlightOrderProvider createFlightOrderProvider;

  @Override
  public FinalPriceResponse getFinalPrice(FlightOfferDTO flightOffer, String token) {
    return createFlightOrderProvider.getFinalPrice(
        new FlightPriceRequest(new FlightPriceRequest.Data(flightOffer)), token);
  }

  @Override
  public FlightOfferDTO getSelectedOffer(List<FlightOfferDTO> flightOffers, String offerId) {
    return flightOffers.stream()
        .filter(flightOffer -> flightOffer.getId().equals(offerId))
        .toList()
        .getFirst();
  }

  @Override
  public void setTravellers(TravellerWrapper travellers, String email, FlightOfferDTO flightOffer) {
    flightOffer
        .getTravelerPricings()
        .forEach(
            travelerPricing ->
                travellers
                    .getTravellers()
                    .add(
                        new Traveller(
                            travelerPricing.getTravelerId(),
                            TravellerTypes.getValueFromType(travelerPricing.getTravelerType()))));
    travellers.getTravellers().getFirst().getContact().setEmailAddress(email);
  }

  @Override
  public void setPassportValidations(TravellerWrapper travellers) {
    travellers.getTravellers().stream()
        .flatMap(traveller -> traveller.getDocuments().stream())
        .forEach(passport -> passport.setValidityCountry(passport.getIssuanceCountry()));
  }

  @Override
  public void setContacts(TravellerWrapper travellers) {
    travellers
        .getTravellers()
        .forEach(
            traveller1 ->
                traveller1.setContact(travellers.getTravellers().getFirst().getContact()));
  }

  @Override
  public void createOrder(CreateOrder createOrderBody, String token) {
    if (!applicationConfig.isUseApis()
        || applicationConfig.getAmadeusCreateOrder().equals("noApi")) {
      return;
    }
    try {
      restClient
          .post()
          .uri(applicationConfig.getAmadeusCreateOrder())
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .body(createOrderBody)
          .retrieve()
          .body(String.class);
    } catch (Exception exception) {
      log.error("Error while creating flight order: {}", exception.getMessage(), exception);
    }
  }

  @Override
  public String getTestApiToken() {
    return cachedApiCalls.getTestAmadeusApiCred().getAccess_token();
  }

  @Override
  public void setTravellersName(FlightOfferDTO selectedOffer, List<Traveller> travelers) {
    selectedOffer
        .getTravelerPricings()
        .forEach(
            travelerPricing -> {
              Traveller traveller =
                  travelers.stream()
                      .filter(t -> t.getId().equals(travelerPricing.getTravelerId()))
                      .toList()
                      .getFirst();
              travelerPricing.setTraveller(
                  traveller.getName().getLastName() + " " + traveller.getName().getFirstName());
            });
  }
}
