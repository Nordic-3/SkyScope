package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CachedApiCallsProvider;
import com.szte.skyScope.services.impl.CachedApiCallsImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;

public class ApiCachedCalls implements CachedApiCallsProvider {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();

  public ApiCachedCalls(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Override
  public String getIataCode(String city, String token) {
    try {
      return Parser.getIataFromJson(getCityAirportSearchResponse(city, "CITY", token), "data");
    } catch (Exception exception) {
      Logger.getLogger(CachedApiCallsImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
  }

  @Override
  public String getAirportName(String iata, String token) {
    String response = getCityAirportSearchResponse(iata, "AIRPORT", token);
    return Parser.getCityNameFromAirportAndCityApi(response, "data")
        + ", "
        + Parser.getAirportNameFromJson(response, "data");
  }

  @Override
  public AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl) {
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

  @CacheEvict(value = "amadeusApiToken", allEntries = true)
  @Scheduled(fixedRateString = "${amadeus_token_expiry}")
  public void emptyAmadeusApiToken() {}

  @CacheEvict(value = "amadeusTestApiToken", allEntries = true)
  @Scheduled(fixedRateString = "${amadeus_token_expiry}")
  public void emptyTestToken() {}

  private String getCityAirportSearchResponse(String keyword, String subType, String token) {
    return restClient
        .get()
        .uri(applicationConfig.getAmadeusCitySearchApi(), keyword.strip(), subType)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .body(String.class);
  }
}
