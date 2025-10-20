package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.Plane;
import java.util.Map;

public interface PlanePositionService {
  Plane getPlanePositionFromJson(String callsign);

  Plane getPlanePositionFromApi(String callsign);

  Map<String, Plane> getAllPlanePositions();
}
