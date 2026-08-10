package com.szte.skyscope.services;

import com.szte.skyscope.models.CheapestDateOffer;
import com.szte.skyscope.models.FlightSearch;
import java.util.List;

public interface CheapestFlightDataProvider {
  List<CheapestDateOffer> getCheapestDateOffers(FlightSearch flightSearch, String token);
}
