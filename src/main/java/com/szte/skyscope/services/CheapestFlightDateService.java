package com.szte.skyscope.services;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.FlightSearch;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CheapestFlightDateService {
  CompletableFuture<List<FlightOfferDTO>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOfferDTO> flightOffers);
}
