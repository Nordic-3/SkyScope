package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.models.Location;
import com.szte.skyScope.models.Plane;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface FlightService {
  String getToken();

  void setIataCodes(FlightSearch flightSearch, String token);

  CompletableFuture<List<FlightOfferDTO>> getFlightOffers(
      FlightSearch flightSearch, String token, String searchId);

  List<FlightOfferDTO> getFlightOffersFromLocalJson(FlightSearch flightSearch, String searchId);

  void setAircraftType(List<FlightOfferDTO> flightOffers, Map<String, String> aircrafts);

  void setCarrierNames(List<FlightOfferDTO> flightOffers, Map<String, String> carriers);

  Map<String, String> getAirportNamesByItsIata(Map<String, Location> locations, String token);

  void setAirportNames(List<FlightOfferDTO> flightOffers, Map<String, String> airprots);

  void setFlightOffersAttributes(List<FlightOfferDTO> result, String searchId, String token);

  Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token);

  void setCallsigns(List<FlightOfferDTO> result, Map<String, String> icaoCodes);

  void setIsCurrentlyFlying(List<FlightOfferDTO> result, Map<String, Plane> planePositions);
}
