package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.City;

public interface CityService {
    City getCityFromApi(String name);
    City getCityFromJson(String name);
}