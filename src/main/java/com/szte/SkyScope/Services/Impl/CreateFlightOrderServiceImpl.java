package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightPriceRequest;
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
  public FlightOffers getFinalPrice(FlightOffers flightOffer, String token) {
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

  private FlightOffers getFinalPriceFromApi(FlightPriceRequest request, String token) {
    FlightPriceRequest flightPriceRequest = null;
    try {
      flightPriceRequest =
          restClient
              .post()
              .uri(applicationConfig.getAmadeusFinalPrice())
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .body(FlightPriceRequest.class);

    } catch (Exception exception) {
      java.util.logging.Logger.getLogger(FlightServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
    }
    return flightPriceRequest != null
        ? flightPriceRequest.getData().getFlightOffers().getFirst()
        : new FlightOffers();
  }

  private FlightOffers getFinalPriceFromJson() {
    FlightPriceRequest flightPriceRequest =
        Parser.parseFlightPriceRequest(
            jsonReaderService.readJsonFromResources("exampleDatas/finalPrice.json"));
    if (flightPriceRequest == null) {
      return new FlightOffers();
    }
    return flightPriceRequest.getData().getFlightOffers().getFirst();
  }
}
