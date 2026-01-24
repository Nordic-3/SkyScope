package com.szte.skyScope.services.providers;

import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CachedApiCallsProvider;
import com.szte.skyScope.services.JsonReaderService;

public class JsonCachedCalls implements CachedApiCallsProvider {
  private final JsonReaderService jsonReaderService;

  public JsonCachedCalls(JsonReaderService jsonReaderService) {
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  public String getIataCode(String city, String token) {
    return Parser.getIataFromJson(
        jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"), city);
  }

  @Override
  public String getAirportName(String iata, String token) {
    String json = jsonReaderService.readJsonFromResources("exampleDatas/airportNames.json");
    return Parser.getCityNameFromAirportAndCityApi(json, iata)
        + ", "
        + Parser.getAirportNameFromJson(json, iata);
  }

  @Override
  public AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl) {
    return new AmadeusApiCred();
  }
}
