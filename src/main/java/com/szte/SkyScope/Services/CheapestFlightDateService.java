package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CheapestFlightDateService {
  CompletableFuture<List<FlightOffers>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOffers> flightOffers);
}
