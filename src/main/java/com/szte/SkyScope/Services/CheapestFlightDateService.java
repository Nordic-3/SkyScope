package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.FlightOfferDTO;
import com.szte.SkyScope.Models.FlightSearch;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CheapestFlightDateService {
  CompletableFuture<List<FlightOfferDTO>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOfferDTO> flightOffers);
}
