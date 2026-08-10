package com.szte.skyscope.services;

import com.szte.skyscope.models.FlightSearch;
import java.util.Map;

public interface FlightServiceProvider {
  String getFlightOffers(FlightSearch flightSearch, String token);

  Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token);
}
