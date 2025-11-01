package com.szte.skyScope.services;

import com.szte.skyScope.models.City;

public interface CityService {
  City getCityFromApi(String name);

  City getCityFromJson(String name);
}
