package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.FlightSearch;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CheapestFlightDateService {
  CompletableFuture<List<FlightOfferDTO>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOfferDTO> flightOffers);
}
