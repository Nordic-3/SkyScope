package com.szte.skyScope.services.providers;

import com.szte.skyScope.models.City;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CityProvider;
import com.szte.skyScope.services.JsonReaderService;

public class JsonCity implements CityProvider {

  private final JsonReaderService jsonReaderService;

  public JsonCity(JsonReaderService jsonReaderService) {
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  public City getCity(String name) {
    return Parser.parseJsonToCity(
        jsonReaderService.readJsonFromResources("exampleDatas/cities.json"), name);
  }
}
