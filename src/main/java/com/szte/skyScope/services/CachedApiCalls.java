package com.szte.skyScope.services;

import com.szte.skyScope.models.AmadeusApiCred;

public interface CachedApiCalls {
  AmadeusApiCred getAmadeusApiCred();

  String getIataCode(String city, String token);

  String getAirportNameFromApi(String iata, String token);

  AmadeusApiCred getTestAmadeusApiCred();
}
