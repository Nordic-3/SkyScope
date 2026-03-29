package com.szte.skyScope.services.impl;

import com.szte.skyScope.models.Plane;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.PlanePositionProvider;
import com.szte.skyScope.services.PlanePositionService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanePositionServiceImpl implements PlanePositionService {
  private final PlanePositionProvider planePositionProvider;

  @Override
  public Plane getPlanePosition(String callsign) {
    Map<String, Plane> planes =
        Parser.parseJsonToMapOfPlanes(planePositionProvider.getAllPLanePositions());
    return planes != null ? planes.get(callsign.strip().toUpperCase()) : new Plane();
  }

  @Override
  public Map<String, Plane> getAllPlanePositions() {
    return Parser.parseJsonToMapOfPlanes(planePositionProvider.getAllPLanePositions());
  }
}
