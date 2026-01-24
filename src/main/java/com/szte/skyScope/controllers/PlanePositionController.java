package com.szte.skyScope.controllers;

import com.szte.skyScope.models.Plane;
import com.szte.skyScope.services.PlanePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanePositionController {
  private final PlanePositionService planePositionService;

  @Autowired
  public PlanePositionController(PlanePositionService planePositionService) {
    this.planePositionService = planePositionService;
  }

  @GetMapping(path = "/planePosition")
  public Plane planePosition(@RequestParam(name = "callsign") final String callsign) {
    if (callsign.isEmpty()) {
      return null;
    }
    return planePositionService.getPlanePosition(callsign);
  }
}
