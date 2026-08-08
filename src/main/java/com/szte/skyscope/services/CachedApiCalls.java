package com.szte.skyscope.services;

import com.szte.skyscope.models.AmadeusApiCred;

public interface CachedApiCalls {
  AmadeusApiCred getAmadeusApiCred();

  String getIataCode(String city, String token);

  String getAirportName(String iata, String token);

  AmadeusApiCred getTestAmadeusApiCred();
}
