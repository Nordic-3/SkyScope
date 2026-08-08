package com.szte.skyscope.services.impl;

import com.szte.skyscope.models.Plane;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.PlanePositionProvider;
import com.szte.skyscope.services.PlanePositionService;
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
