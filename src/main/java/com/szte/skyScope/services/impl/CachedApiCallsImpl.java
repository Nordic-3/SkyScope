package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CachedApiCalls;
import com.szte.skyScope.services.JsonReaderService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CachedApiCallsImpl implements CachedApiCalls {

  private final ApplicationConfig applicationConfig;
  private final JsonReaderService jsonReaderService;
  private final RestClient restClient = RestClient.create();

  @Autowired
  public CachedApiCallsImpl(
      ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
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
    if (applicationConfig.getAmadeusCitySearchApi().equals("noApi")
        || !applicationConfig.useApis()) {
      return getIataCodeFromLocalJson(city);
    }
    try {
      return Parser.getIataFromJson(getCityAirportSearchApiResponse(city, "CITY", token), "data");
    } catch (Exception exception) {
      Logger.getLogger(CachedApiCallsImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
  }

  @Override
  @Cacheable("airportName")
  public String getAirportNameFromApi(String iata, String token) {
    if (applicationConfig.getAmadeusCitySearchApi().equals("noApi")
        || !applicationConfig.useApis()) {
      return getAirportNameFromLocalJson(iata);
    }
    String response = getCityAirportSearchApiResponse(iata, "AIRPORT", token);
    return Parser.getCityNameFromAirportAndCityApi(response, "data")
        + ", "
        + Parser.getAirportNameFromJson(response, "data");
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

  private String getAirportNameFromLocalJson(String iata) {
    String json = jsonReaderService.readJsonFromResources("exampleDatas/airportNames.json");
    return Parser.getCityNameFromAirportAndCityApi(json, iata)
        + ", "
        + Parser.getAirportNameFromJson(json, iata);
  }

  private String getCityAirportSearchApiResponse(String keyword, String subType, String token) {
    return restClient
        .get()
        .uri(applicationConfig.getAmadeusCitySearchApi(), keyword.strip(), subType)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .body(String.class);
  }

  private String getIataCodeFromLocalJson(String city) {
    return Parser.getIataFromJson(
        jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"), city);
  }

  private AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl) {
    if (!applicationConfig.useApis() || applicationConfig.getAmadeusClientId().equals("noApi")) {
      return new AmadeusApiCred();
    }
    String body =
        "grant_type=client_credentials"
            + "&client_id="
            + clientId
            + "&client_secret="
            + clientSecret;
    return restClient
        .post()
        .uri(authUrl)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body(body)
        .retrieve()
        .body(AmadeusApiCred.class);
  }
}
