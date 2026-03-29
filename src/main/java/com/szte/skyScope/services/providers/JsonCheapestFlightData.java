package com.szte.skyScope.services.providers;

import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CheapestFlightDataProvider;
import com.szte.skyScope.services.JsonReaderService;
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
