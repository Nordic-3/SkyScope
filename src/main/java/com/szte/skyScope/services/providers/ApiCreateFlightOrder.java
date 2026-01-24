package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.FlightPriceRequest;
import com.szte.skyScope.services.CreateFlightOrderProvider;
import com.szte.skyScope.services.impl.CreateFlightOrderServiceImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ApiCreateFlightOrder implements CreateFlightOrderProvider {
  private final ApplicationConfig applicationConfig;

  public ApiCreateFlightOrder(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Override
  public FinalPriceResponse getFinalPrice(FlightPriceRequest request, String token) {
    FinalPriceResponse finalPrice = null;
    try {
      finalPrice =
          RestClient.create()
              .post()
              .uri(applicationConfig.getAmadeusFinalPrice())
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .body(FinalPriceResponse.class);

    } catch (Exception exception) {
      Logger.getLogger(CreateFlightOrderServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
    }
    return finalPrice != null ? finalPrice : new FinalPriceResponse();
  }
}
