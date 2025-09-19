package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface FlightService {
    AmadeusApiCred getToken();
    String getIataCode(String city, String token);
    String getIataCodeFromLocalJson(String city);
    void setIataCodes(FlightSearch flightSearch, String token);
    CompletableFuture<List<FlightOffers>> getFlightOffers(FlightSearch flightSearch, String token);
    List<FlightOffers> getFlightOffersFromLocalJson(FlightSearch flightSearch);
    void setAircraftType(FlightOffers flightOffers, Map<String, String> aircrafts);
    void setCarrierNames(FlightOffers flightOffers, Map<String, String> carriers);
}
