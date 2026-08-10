package com.szte.skyscope.services.providers;

import com.szte.skyscope.models.City;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.CityProvider;
import com.szte.skyscope.services.JsonReaderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonCity implements CityProvider {

  private final JsonReaderService jsonReaderService;

  @Override
  public City getCity(String name) {
    return Parser.parseJsonToCity(
        jsonReaderService.readJsonFromResources("exampleDatas/cities.json"), name);
  }
}
