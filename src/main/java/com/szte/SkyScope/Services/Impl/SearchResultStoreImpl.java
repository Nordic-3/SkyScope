package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.SearchResultStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchResultStoreImpl implements SearchResultStore {
    private final Map<String, List<FlightOffers>> store = new HashMap<>();

    @Override
    public void save(String id, List<FlightOffers> offers) {
        store.put(id, offers);
    }

    @Override
    public List<FlightOffers> get(String id) {
        return store.get(id);
    }
}
