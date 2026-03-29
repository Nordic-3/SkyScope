package com.szte.skyScope.services.providers;

import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.FlightPriceRequest;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CreateFlightOrderProvider;
import com.szte.skyScope.services.JsonReaderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonCreateFlightOrder implements CreateFlightOrderProvider {
  private final JsonReaderService jsonReaderService;

  @Override
  public FinalPriceResponse getFinalPrice(FlightPriceRequest request, String token) {
    FinalPriceResponse finalPrice =
        Parser.parseFlightPriceRequest(
            jsonReaderService.readJsonFromResources("exampleDatas/finalPrice.json"));
    if (finalPrice == null) {
      return new FinalPriceResponse();
    }
    return finalPrice;
  }
}
