package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.FlightPriceRequest;
import com.szte.skyScope.services.CreateFlightOrderProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
public class ApiCreateFlightOrder implements CreateFlightOrderProvider {
  private final ApplicationConfig applicationConfig;

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
      log.error(
          "Error while calling Amadeus API for final price calculation: {}",
          exception.getMessage(),
          exception);
    }
    return finalPrice != null ? finalPrice : new FinalPriceResponse();
  }
}
