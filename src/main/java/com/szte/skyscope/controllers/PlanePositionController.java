package com.szte.skyscope.controllers;

import com.szte.skyscope.models.Plane;
import com.szte.skyscope.services.PlanePositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlanePositionController {
  private final PlanePositionService planePositionService;

  @GetMapping(path = "/planePosition")
  public Plane planePosition(@RequestParam(name = "callsign") final String callsign) {
    if (callsign.isEmpty()) {
      return null;
    }
    return planePositionService.getPlanePosition(callsign);
  }
}
