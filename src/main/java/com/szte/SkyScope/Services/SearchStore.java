package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;

import java.util.List;

public interface SearchStore {
    void saveSearchResult(String id, List<FlightOffers> offers);
    List<FlightOffers> getSearchResult(String id);
    void saveSearchParameters(FlightSearch flightSearch);
    FlightSearch getSearchParameters();
}
