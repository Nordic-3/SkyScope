package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.SearchStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchResultStoreImpl implements SearchStore {
    private final Map<String, List<FlightOffers>> store = new HashMap<>();
    private FlightSearch flightSearch;

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
}
