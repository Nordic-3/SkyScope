package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FilterAttribute;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.Location;
import java.util.List;
import java.util.Map;

public interface SearchStore {
  void saveSearchResult(String id, List<FlightOffers> offers);

  List<FlightOffers> getSearchResult(String id);

  void saveSearchParameters(FlightSearch flightSearch);

  FlightSearch getSearchParameters();

  void saveAircraftDictionary(Map<String, String> aircraftDictionary);

  Map<String, String> getAircraftDictionary();

  void saveLocationDictionary(Map<String, Location> locationDictionary);

  Map<String, Location> getLocationDictionary();

  void saveCarrierDictionary(Map<String, String> carrierDictionary);

  Map<String, String> getCarrierDictionary();

  void saveFilters(FilterAttribute filterAttribute);

  FilterAttribute getFilterAttribute();

  void saveOriginalSearchResult(String id, List<FlightOffers> offers);

  List<FlightOffers> getOriginalSearchResult(String id);
}
