package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.Plane;

public interface PlanePositionService {
  Plane getPlanePositionFromJson(String callsign);

  Plane getPlanePositionFromApi(String callsign);
}
