package com.szte.skyScope.services;

import com.szte.skyScope.models.Plane;
import java.util.Map;

public interface PlanePositionService {
  Plane getPlanePosition(String callsign);

  Map<String, Plane> getAllPlanePositions();
}
