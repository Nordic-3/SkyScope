package com.szte.skyScope.controllers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.Plane;
import com.szte.skyScope.services.PlanePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanePositionController {
  private final PlanePositionService planePositionService;
  private final ApplicationConfig applicationConfig;

  @Autowired
  public PlanePositionController(
      PlanePositionService planePositionService, ApplicationConfig applicationConfig) {
    this.planePositionService = planePositionService;
    this.applicationConfig = applicationConfig;
  }

  @GetMapping(path = "/planePosition")
  public Plane planePosition(@RequestParam(name = "callsign") final String callsign) {
    if (callsign.isEmpty()) {
      return null;
    }
    if (applicationConfig.useApis()) {
      return planePositionService.getPlanePositionFromApi(callsign);
    }
    return planePositionService.getPlanePositionFromJson(callsign);
  }
}
