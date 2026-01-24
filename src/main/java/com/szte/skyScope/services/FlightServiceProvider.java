package com.szte.skyScope.services;

import com.szte.skyScope.models.FlightSearch;
import java.util.Map;

public interface FlightServiceProvider {
  String getFlightOffers(FlightSearch flightSearch, String token);

  Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token);
}
