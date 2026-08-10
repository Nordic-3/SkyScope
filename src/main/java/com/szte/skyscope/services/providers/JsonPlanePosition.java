package com.szte.skyscope.services.providers;

import com.szte.skyscope.services.JsonReaderService;
import com.szte.skyscope.services.PlanePositionProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonPlanePosition implements PlanePositionProvider {

  private final JsonReaderService jsonReaderService;

  @Override
  public String getAllPLanePositions() {
    return jsonReaderService.readJsonFromResources("exampleDatas/planePositions.json");
  }
}
