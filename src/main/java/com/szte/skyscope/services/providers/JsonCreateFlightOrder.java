package com.szte.skyscope.services.providers;

import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.FlightPriceRequest;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.CreateFlightOrderProvider;
import com.szte.skyscope.services.JsonReaderService;
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
