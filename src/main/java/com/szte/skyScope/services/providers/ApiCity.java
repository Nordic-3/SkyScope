package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.City;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CityProvider;
import org.springframework.web.client.RestClient;

public class ApiCity implements CityProvider {
  private final ApplicationConfig applicationConfig;

  public ApiCity(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Override
  public City getCity(String name) {
    String response =
        RestClient.create()
            .get()
            .uri(applicationConfig.getGeoNamesApiKey(), name)
            .retrieve()
            .body(String.class);
    return Parser.parseJsonToCity(response, "geonames");
  }
}
