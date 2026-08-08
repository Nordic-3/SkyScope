package com.szte.skyscope.services;

import com.szte.skyscope.models.AmadeusApiCred;

public interface CachedApiCallsProvider {
  String getIataCode(String city, String token);

  String getAirportName(String iata, String token);

  AmadeusApiCred getApiCred(String clientId, String clientSecret, String authUrl);
}
