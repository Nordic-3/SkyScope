package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.Location;

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
    void setAircraftType(List<FlightOffers> flightOffers, Map<String, String> aircrafts);
    void setCarrierNames(List<FlightOffers> flightOffers, Map<String, String> carriers);
    Map<String, String> getAirportNamesByItsIata(Map<String, Location> locations, String token);
    void setAirportNames(List<FlightOffers> flightOffers, Map<String, String> airprots);
}
