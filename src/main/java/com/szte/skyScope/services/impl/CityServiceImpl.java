package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.City;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CityService;
import com.szte.skyScope.services.JsonReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CityServiceImpl implements CityService {

  private final RestClient restClient = RestClient.create();
  private final ApplicationConfig applicationConfig;
  private final JsonReaderService jsonReaderService;

  @Autowired
  public CityServiceImpl(ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  @Cacheable("city")
  public City getCityFromApi(String name) {
    if (applicationConfig.getGeoNamesApiKey().equals("noApi")) {
      return getCityFromJson(name);
    }
    String response =
        restClient
            .get()
            .uri(applicationConfig.getGeoNamesApiKey(), name)
            .retrieve()
            .body(String.class);
    return Parser.parseJsonToCity(response, "geonames");
  }

  @Override
  public City getCityFromJson(String name) {
    return Parser.parseJsonToCity(
        jsonReaderService.readJsonFromResources("exampleDatas/cities.json"), name);
  }
}
