package com.szte.skyScope.services.impl;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.services.*;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheapestFlightDateServiveImpl implements CheapestFlightDateService {
  private final FlightService flightService;
  private final SearchStore searchStore;
  private final CheapestFlightDataProvider cheapestFlightDataProvider;

  @Override
  public CompletableFuture<List<FlightOfferDTO>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOfferDTO> flightOffers) {
    List<CheapestDateOffer> cheapestDateOffers =
        cheapestFlightDataProvider.getCheapestDateOffers(flightSearch, token);
    if (cheapestDateOffers == null
        || cheapestDateOffers.isEmpty()
        || !isCheaper(flightOffers, getCheapestOffer(cheapestDateOffers))) {
      return CompletableFuture.completedFuture(null);
    }
    FlightSearch cheaperFlight =
        getcheaperFlightOfferSearchParameters(flightSearch, cheapestDateOffers);
    searchStore.getSearchDatas(searchId).setCheaperSearch(cheaperFlight);
    return flightService.getFlightOffers(cheaperFlight, token, searchId);
  }

  private boolean isCheaper(
      List<FlightOfferDTO> flightOffers, CheapestDateOffer cheapestDateOffer) {
    FlightOfferDTO flightOffer = flightOffers.getFirst();
    return flightOffer.getTravelerPricings().stream()
        .filter(travelerPricing -> travelerPricing.getTravelerType().equals("ADULT"))
        .anyMatch(
            travelerPricing -> {
              String total = travelerPricing.getPrice().getTotal();
              if (!total.isEmpty()) {
                int price = Integer.parseInt(total.split("\\.")[0]);
                return cheapestDateOffer.getPrice() < (price / 380)
                    && cheapestDateOffer.getPrice() != price / 380;
              }
              return false;
            });
  }

  private CheapestDateOffer getCheapestOffer(List<CheapestDateOffer> cheapestDateOffers) {
    return cheapestDateOffers.stream()
        .min(Comparator.comparingInt(CheapestDateOffer::getPrice))
        .get();
  }

  private FlightSearch getcheaperFlightOfferSearchParameters(
      FlightSearch flightSearch, List<CheapestDateOffer> cheapestDateOffers) {
    FlightSearch cheaperSearch = new FlightSearch(flightSearch);
    CheapestDateOffer cheapestDateOffer = getCheapestOffer(cheapestDateOffers);
    cheaperSearch.setDepartureDate(cheapestDateOffer.getDepartureDate());
    if (!flightSearch.isOneWay()) {
      cheaperSearch.setReturnDate(cheapestDateOffer.getReturnDate());
    }
    return cheaperSearch;
  }
}
