package com.szte.skyscope.services.impl;

import com.szte.skyscope.models.City;
import com.szte.skyscope.services.CityProvider;
import com.szte.skyscope.services.CityService;
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
