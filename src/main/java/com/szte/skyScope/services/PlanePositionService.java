package com.szte.skyScope.services;

import com.szte.skyScope.models.Plane;
import java.util.Map;

public interface PlanePositionService {
  Plane getPlanePositionFromJson(String callsign);

  Plane getPlanePositionFromApi(String callsign);

  Map<String, Plane> getAllPlanePositions();
}
