package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.FlightOfferDTO;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.Location;
import com.szte.SkyScope.Models.Plane;
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
