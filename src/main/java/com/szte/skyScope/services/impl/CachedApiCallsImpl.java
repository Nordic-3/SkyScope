package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.services.CachedApiCalls;
import com.szte.skyScope.services.CachedApiCallsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CachedApiCallsImpl implements CachedApiCalls {

  private final ApplicationConfig applicationConfig;
  private final CachedApiCallsProvider cachedApiCallsProvider;

  @Autowired
  public CachedApiCallsImpl(
      ApplicationConfig applicationConfig, CachedApiCallsProvider cachedApiCallsProvider) {
    this.applicationConfig = applicationConfig;
    this.cachedApiCallsProvider = cachedApiCallsProvider;
  }

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

  @CacheEvict(value = "amadeusApiToken", allEntries = true)
  @Scheduled(fixedRateString = "${amadeus_token_expiry}")
  public void emptyAmadeusApiToken() {}

  @CacheEvict(value = "amadeusTestApiToken", allEntries = true)
  @Scheduled(fixedRateString = "${amadeus_token_expiry}")
  public void emptyTestToken() {}

  private AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl) {
    return cachedApiCallsProvider.getApiCred(clientId, clientSecret, authUrl);
  }
}
