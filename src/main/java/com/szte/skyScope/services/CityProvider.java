package com.szte.skyScope.services;

import com.szte.skyScope.models.City;

public interface CityProvider {
  City getCity(String name);
}
