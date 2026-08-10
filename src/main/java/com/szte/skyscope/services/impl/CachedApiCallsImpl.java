package com.szte.skyscope.services.impl;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.AmadeusApiCred;
import com.szte.skyscope.services.CachedApiCalls;
import com.szte.skyscope.services.CachedApiCallsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CachedApiCallsImpl implements CachedApiCalls {

  private final ApplicationConfig applicationConfig;
  private final CachedApiCallsProvider cachedApiCallsProvider;

  @Override
  @Cacheable("amadeusApiToken")
  public AmadeusApiCred getAmadeusApiCred() {
    return getApiCred(
        applicationConfig.getAmadeusClientId(),
        applicationConfig.getAmadeusClientSecret(),
        applicationConfig.getAmadeusAuthUrl());
  }

  @Override
  @Cacheable("iataOfCity")
  public String getIataCode(String city, String token) {
    return cachedApiCallsProvider.getIataCode(city, token);
  }

  @Override
  @Cacheable("airportName")
  public String getAirportName(String iata, String token) {
    return cachedApiCallsProvider.getAirportName(iata, token);
  }

  @Override
  @Cacheable("amadeusTestApiToken")
  public AmadeusApiCred getTestAmadeusApiCred() {
    return getApiCred(
        applicationConfig.getAmadeusTestClientId(),
        applicationConfig.getAmadeusTestClientSecret(),
        applicationConfig.getAmadeusTestAuthUrl());
  }

  private AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl) {
    return cachedApiCallsProvider.getApiCred(clientId, clientSecret, authUrl);
  }
}
