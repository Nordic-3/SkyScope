package com.szte.SkyScope.Controllers;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.City;
import com.szte.SkyScope.Services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityNameController {

    private final CityService cityService;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public CityNameController(CityService cityService, ApplicationConfig applicationConfig) {
        this.cityService = cityService;
        this.applicationConfig = applicationConfig;
    }

    @GetMapping(path="/city")
    public City getCityCoordinates(@RequestParam(name = "city") final String city) {
        if (city.isEmpty()) {
            return null;
        }
        if (applicationConfig.useApis()) {
            return cityService.getCityFromApi(city);
        }
        else {
            return cityService.getCityFromJson(city);
        }
    }
}