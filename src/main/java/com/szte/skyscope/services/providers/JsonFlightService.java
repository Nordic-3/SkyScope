package com.szte.skyscope.services.providers;

import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.FlightServiceProvider;
import com.szte.skyscope.services.JsonReaderService;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonFlightService implements FlightServiceProvider {
  private final JsonReaderService jsonReaderService;

  @Override
  public String getFlightOffers(FlightSearch flightSearch, String token) {
    return jsonReaderService.readJsonFromResources("exampleDatas/FlightOffers.json");
  }

  @Override
  public Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token) {
    return Parser.getIcaoCodesFromJson(
        jsonReaderService.readJsonFromResources("exampleDatas/icaoCodes.json"));
  }
}
