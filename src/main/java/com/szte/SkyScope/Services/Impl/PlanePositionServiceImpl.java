package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.Plane;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.JsonReaderService;
import com.szte.SkyScope.Services.PlanePositionService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PlanePositionServiceImpl implements PlanePositionService {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();
  private final JsonReaderService jsonReaderService;

  @Autowired
  public PlanePositionServiceImpl(
      ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  public Plane getPlanePositionFromJson(String callsign) {
    return getPlaneFromDataSource(
        callsign, jsonReaderService.readJsonFromResources("exampleDatas/planePositions.json"));
  }

  @Override
  public Plane getPlanePositionFromApi(String callsign) {
    return getPlaneFromDataSource(callsign, openSkyApiCall());
  }

  @Override
  public Map<String, Plane> getAllPlanePositions() {
    if (!applicationConfig.useApis() || applicationConfig.getOpenskyApiKey().equals("noApi")) {
      return Parser.parseJsonToMapOfPlanes(
          jsonReaderService.readJsonFromResources("exampleDatas/planePositions.json"));
    }
    return Parser.parseJsonToMapOfPlanes(openSkyApiCall());
  }

  private Plane getPlaneFromDataSource(String callsign, String dataSource) {
    Map<String, Plane> planes = Parser.parseJsonToMapOfPlanes(dataSource);
    return planes != null ? planes.get(callsign.strip().toUpperCase()) : new Plane();
  }

  private String openSkyApiCall() {
    try {
      return restClient
          .get()
          .uri(applicationConfig.getOpenskyApiKey())
          .retrieve()
          .body(String.class);
    } catch (Exception exception) {
      Logger.getLogger(PlanePositionServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return "";
    }
  }
}
