package com.szte.skyscope.services.providers;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.City;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.CityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class ApiCity implements CityProvider {
  private final ApplicationConfig applicationConfig;

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
