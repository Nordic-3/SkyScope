package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CheapestFlightDateService;
import com.szte.skyScope.services.FlightService;
import com.szte.skyScope.services.JsonReaderService;
import com.szte.skyScope.services.SearchStore;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CheapestFlightDateServiveImpl implements CheapestFlightDateService {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();
  private final FlightService flightService;
  private final JsonReaderService jsonReaderService;
  private final SearchStore searchStore;

  @Autowired
  public CheapestFlightDateServiveImpl(
      ApplicationConfig applicationConfig,
      FlightService flightService,
      JsonReaderService jsonReaderService,
      SearchStore searchStore) {
    this.applicationConfig = applicationConfig;
    this.flightService = flightService;
    this.jsonReaderService = jsonReaderService;
    this.searchStore = searchStore;
  }

  @Override
  public CompletableFuture<List<FlightOfferDTO>> checkForCheaperOfferAndGetIt(
      FlightSearch flightSearch, String token, String searchId, List<FlightOfferDTO> flightOffers) {
    List<CheapestDateOffer> cheapestDateOffers = getCheapestDateOffers(flightSearch, token);
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
                return cheapestDateOffer.getPrice() < (price * 380)
                    && cheapestDateOffer.getPrice() != price * 380;
              }
              return false;
            });
  }

  private CheapestDateOffer getCheapestOffer(List<CheapestDateOffer> cheapestDateOffers) {
    return cheapestDateOffers.stream()
        .min(Comparator.comparingInt(CheapestDateOffer::getPrice))
        .get();
  }

  private List<CheapestDateOffer> getCheapestDateOffers(FlightSearch flightSearch, String token) {
    if (!applicationConfig.useApis()
        || applicationConfig.getAmadeusCheapestDateSearch().equals("noApi")) {
      return Parser.parseCheapestFlightApi(
          jsonReaderService.readJsonFromResources("exampleDatas/cheaperOffer.json"));
    }
    UriComponentsBuilder uriBulder =
        UriComponentsBuilder.fromUriString(applicationConfig.getAmadeusCheapestDateSearch());
    uriBulder.queryParam("origin", flightSearch.getOriginCityIata());
    uriBulder.queryParam("destination", flightSearch.getDestinationCityIata());
    uriBulder.queryParam("departureDate", calculateFlightDateRange(flightSearch));
    if (flightSearch.isOneWay()) {
      uriBulder.queryParam("oneWay", true);
    }
    try {
      String response =
          restClient
              .get()
              .uri(uriBulder.build(true).toUri())
              .header("Authorization", "Bearer " + token)
              .header("Accept", "application/json")
              .retrieve()
              .body(String.class);
      return Parser.parseCheapestFlightApi(response);
    } catch (Exception exception) {
      Logger.getLogger(CheapestDateOffer.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
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

  private String calculateFlightDateRange(FlightSearch flightSearch) {
    LocalDate departure = LocalDate.parse(flightSearch.getDepartureDate());
    if (!flightSearch.isOneWay()) {
      return calculateRoundTripRange(flightSearch, departure);
    }
    if (LocalDate.now().plusDays(15).isBefore(departure)) {
      return departure.minusDays(15) + "," + departure.plusDays(15);
    }
    return LocalDate.now().plusDays(1) + "," + departure.plusDays(15);
  }

  private String calculateRoundTripRange(FlightSearch flightSearch, LocalDate departure) {
    LocalDate returnDate = LocalDate.parse(flightSearch.getReturnDate());
    if (LocalDate.now().plusDays(15).isBefore(departure)) {
      return departure.minusDays(15) + "," + returnDate.plusDays(15);
    }
    return LocalDate.now().plusDays(1) + "," + returnDate.plusDays(15);
  }
}
