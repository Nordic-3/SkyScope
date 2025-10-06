package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.AmadeusApiCred;

public interface CachedApiCalls {
  AmadeusApiCred getAmadeusApiCred();

  String getIataCode(String city, String token);

  String getAirportNameFromApi(String iata, String token);
}
