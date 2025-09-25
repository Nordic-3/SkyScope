package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FilterAttribute;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.Location;
import com.szte.SkyScope.Services.SearchStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SearchStoreImpl implements SearchStore {
  private final Map<String, List<FlightOffers>> store = new HashMap<>();
  private FlightSearch flightSearch;
  private Map<String, String> aircraftDictionary;
  private Map<String, Location> locationDictionary;
  private Map<String, String> carrierDictionary;
  private FilterAttribute filterAttribute = new FilterAttribute();
  private final Map<String, List<FlightOffers>> originalSearchResult = new HashMap<>();

  @Override
  public void saveSearchResult(String id, List<FlightOffers> offers) {
    store.put(id, offers);
  }

  @Override
  public List<FlightOffers> getSearchResult(String id) {
    return store.get(id);
  }

  @Override
  public void saveSearchParameters(FlightSearch flightSearch) {
    this.flightSearch = flightSearch;
  }

  @Override
  public FlightSearch getSearchParameters() {
    return this.flightSearch;
  }

  @Override
  public void saveAircraftDictionary(Map<String, String> aircraftDictionary) {
    this.aircraftDictionary = aircraftDictionary;
  }

  @Override
  public Map<String, String> getAircraftDictionary() {
    return this.aircraftDictionary;
  }

  @Override
  public void saveLocationDictionary(Map<String, Location> locationDictionary) {
    this.locationDictionary = locationDictionary;
  }

  @Override
  public Map<String, Location> getLocationDictionary() {
    return this.locationDictionary;
  }

  @Override
  public void saveCarrierDictionary(Map<String, String> carrierDictionary) {
    this.carrierDictionary = carrierDictionary;
  }

  @Override
  public Map<String, String> getCarrierDictionary() {
    return this.carrierDictionary;
  }

  @Override
  public void saveFilters(FilterAttribute filterAttribute) {
    this.filterAttribute = filterAttribute;
  }

  @Override
  public FilterAttribute getFilterAttribute() {
    return this.filterAttribute;
  }

  @Override
  public void saveOriginalSearchResult(String id, List<FlightOffers> offers) {
    originalSearchResult.put(id, offers);
  }

  @Override
  public List<FlightOffers> getOriginalSearchResult(String id) {
    return originalSearchResult.get(id);
  }
}
