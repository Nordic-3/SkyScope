package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.enums.TravellerTypes;
import com.szte.skyScope.models.*;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CachedApiCalls;
import com.szte.skyScope.services.CreateFlightOrderService;
import com.szte.skyScope.services.JsonReaderService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CreateFlightOrderServiceImpl implements CreateFlightOrderService {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();
  private final JsonReaderService jsonReaderService;
  private final Logger logger = Logger.getLogger(CreateFlightOrderServiceImpl.class.getName());
  private final CachedApiCalls cachedApiCalls;

  @Autowired
  public CreateFlightOrderServiceImpl(
      ApplicationConfig applicationConfig,
      JsonReaderService jsonReaderService,
      CachedApiCalls cachedApiCalls) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
    this.cachedApiCalls = cachedApiCalls;
  }

  @Override
  public FinalPriceResponse getFinalPrice(FlightOfferDTO flightOffer, String token) {
    if (applicationConfig.useApis() && !applicationConfig.getAmadeusFinalPrice().equals("noApi")) {
      return getFinalPriceFromApi(
          new FlightPriceRequest(new FlightPriceRequest.Data(flightOffer)), token);
    }
    return getFinalPriceFromJson();
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
    if (!applicationConfig.useApis() || applicationConfig.getAmadeusCreateOrder().equals("noApi")) {
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
      logger.log(Level.SEVERE, exception.getMessage(), exception);
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

  private FinalPriceResponse getFinalPriceFromApi(FlightPriceRequest request, String token) {
    FinalPriceResponse finalPrice = null;
    try {
      finalPrice =
          restClient
              .post()
              .uri(applicationConfig.getAmadeusFinalPrice())
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .body(FinalPriceResponse.class);

    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return finalPrice != null ? finalPrice : new FinalPriceResponse();
  }

  private FinalPriceResponse getFinalPriceFromJson() {
    FinalPriceResponse finalPrice =
        Parser.parseFlightPriceRequest(
            jsonReaderService.readJsonFromResources("exampleDatas/finalPrice.json"));
    if (finalPrice == null) {
      return new FinalPriceResponse();
    }
    return finalPrice;
  }
}
