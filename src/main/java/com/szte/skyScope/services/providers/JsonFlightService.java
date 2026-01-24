package com.szte.skyScope.services.providers;

import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.FlightServiceProvider;
import com.szte.skyScope.services.JsonReaderService;
import java.util.Map;

public class JsonFlightService implements FlightServiceProvider {
  private final JsonReaderService jsonReaderService;

  public JsonFlightService(JsonReaderService jsonReaderService) {
    this.jsonReaderService = jsonReaderService;
  }

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
