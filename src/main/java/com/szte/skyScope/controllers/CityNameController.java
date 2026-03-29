package com.szte.skyScope.controllers;

import com.szte.skyScope.models.City;
import com.szte.skyScope.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CityNameController {
  private final CityService cityService;

  @GetMapping(path = "/city")
  public City getCityCoordinates(@RequestParam(name = "city") final String city) {
    if (city.isEmpty()) {
      return null;
    }
    return cityService.getCity(city);
  }
}
