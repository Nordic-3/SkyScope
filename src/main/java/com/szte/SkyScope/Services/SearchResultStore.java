package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;

import java.util.List;

public interface SearchResultStore {
    void save(String id, List<FlightOffers> offers);
    List<FlightOffers> get(String id);
}
