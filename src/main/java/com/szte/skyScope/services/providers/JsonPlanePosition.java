package com.szte.skyScope.services.providers;

import com.szte.skyScope.services.JsonReaderService;
import com.szte.skyScope.services.PlanePositionProvider;

public class JsonPlanePosition implements PlanePositionProvider {

  private final JsonReaderService jsonReaderService;

  public JsonPlanePosition(JsonReaderService jsonReaderService) {
    this.jsonReaderService = jsonReaderService;
  }

  @Override
  public String getAllPLanePositions() {
    return jsonReaderService.readJsonFromResources("exampleDatas/planePositions.json");
  }
}
