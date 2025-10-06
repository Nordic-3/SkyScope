package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.Plane;
import com.szte.SkyScope.Services.PlanePositionService;
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
