package com.szte.skyScope.services;

import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import java.util.List;

public interface CheapestFlightDataProvider {
  List<CheapestDateOffer> getCheapestDateOffers(FlightSearch flightSearch, String token);
}
