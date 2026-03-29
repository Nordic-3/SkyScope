package com.szte.skyScope.services.impl;

import com.szte.skyScope.models.City;
import com.szte.skyScope.services.CityProvider;
import com.szte.skyScope.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

  private final CityProvider cityProvider;

  @Override
  @Cacheable("city")
  public City getCity(String name) {
    return cityProvider.getCity(name);
  }
}
