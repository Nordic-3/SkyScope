package com.szte.skyscope.services;

import com.szte.skyscope.models.City;

public interface CityProvider {
  City getCity(String name);
}
