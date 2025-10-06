package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.Plane;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.JsonReaderService;
import com.szte.SkyScope.Services.PlanePositionService;
import java.util.Map;
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
    String response =
        restClient.get().uri(applicationConfig.getOpenskyApiKey()).retrieve().body(String.class);
    return getPlaneFromDataSource(callsign, response);
  }

  private Plane getPlaneFromDataSource(String callsign, String dataSource) {
    Map<String, Plane> planes = Parser.parseJsonToMapOfPlanes(dataSource);
    return planes != null ? planes.get(callsign.strip().toUpperCase()) : new Plane();
  }
}
