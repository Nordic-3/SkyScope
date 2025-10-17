package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Enums.TravellerTypes;
import com.szte.SkyScope.Models.*;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.CreateFlightOrderService;
import com.szte.SkyScope.Services.JsonReaderService;
import java.util.List;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CreateFlightOrderServiceImpl implements CreateFlightOrderService {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();
  private final JsonReaderService jsonReaderService;

  @Autowired
  public CreateFlightOrderServiceImpl(
      ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  public FinalPriceResponse getFinalPrice(FlightOffers flightOffer, String token) {
    if (applicationConfig.useApis() && !applicationConfig.getAmadeusFinalPrice().equals("noApi")) {
      return getFinalPriceFromApi(
          new FlightPriceRequest(new FlightPriceRequest.Data(flightOffer)), token);
    }
    return getFinalPriceFromJson();
  }

  @Override
  public FlightOffers getSelectedOffer(List<FlightOffers> flightOffers, String offerId) {
    return flightOffers.stream()
        .filter(flightOffer -> flightOffer.getId().equals(offerId))
        .toList()
        .getFirst();
  }

  @Override
  public void setTravellers(TravellerWrapper travellers, String email, FlightOffers flightOffers) {
    flightOffers
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

  private FinalPriceResponse getFinalPriceFromApi(
      FlightPriceRequest request, String token) {
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
      java.util.logging.Logger.getLogger(FlightServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
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
