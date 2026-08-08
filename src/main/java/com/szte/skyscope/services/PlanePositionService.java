package com.szte.skyscope.services;

import com.szte.skyscope.models.Plane;
import java.util.Map;

public interface PlanePositionService {
  Plane getPlanePosition(String callsign);

  Map<String, Plane> getAllPlanePositions();
}
