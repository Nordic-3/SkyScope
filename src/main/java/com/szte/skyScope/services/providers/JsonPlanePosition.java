package com.szte.skyScope.services.providers;

import com.szte.skyScope.services.JsonReaderService;
import com.szte.skyScope.services.PlanePositionProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonPlanePosition implements PlanePositionProvider {

  private final JsonReaderService jsonReaderService;

  @Override
  public String getAllPLanePositions() {
    return jsonReaderService.readJsonFromResources("exampleDatas/planePositions.json");
  }
}
