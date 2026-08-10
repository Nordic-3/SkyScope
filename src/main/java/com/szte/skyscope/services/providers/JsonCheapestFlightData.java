package com.szte.skyscope.services.providers;

import com.szte.skyscope.models.CheapestDateOffer;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.CheapestFlightDataProvider;
import com.szte.skyscope.services.JsonReaderService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonCheapestFlightData implements CheapestFlightDataProvider {

  private final JsonReaderService jsonReaderService;

  @Override
  public List<CheapestDateOffer> getCheapestDateOffers(FlightSearch flightSearch, String token) {
    return Parser.parseCheapestFlightApi(
        jsonReaderService.readJsonFromResources("exampleDatas/cheaperOffer.json"));
  }
}
