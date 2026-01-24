package com.szte.skyScope.services.impl;

import com.szte.skyScope.models.City;
import com.szte.skyScope.services.CityProvider;
import com.szte.skyScope.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

  private final CityProvider cityProvider;

  @Autowired
  public CityServiceImpl(CityProvider cityProvider) {
    this.cityProvider = cityProvider;
  }

  @Override
  @Cacheable("city")
  public City getCity(String name) {
    return cityProvider.getCity(name);
  }
}
