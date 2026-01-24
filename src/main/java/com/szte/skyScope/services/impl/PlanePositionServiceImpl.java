package com.szte.skyScope.services.impl;

import com.szte.skyScope.models.Plane;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.PlanePositionProvider;
import com.szte.skyScope.services.PlanePositionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanePositionServiceImpl implements PlanePositionService {
  private final PlanePositionProvider planePositionProvider;

  @Autowired
  public PlanePositionServiceImpl(PlanePositionProvider planePositionProvider) {
    this.planePositionProvider = planePositionProvider;
  }

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
