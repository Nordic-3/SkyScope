package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.CheapestFlightDataProvider;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class ApiCheapestFlightData implements CheapestFlightDataProvider {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();

  public ApiCheapestFlightData(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Override
  public List<CheapestDateOffer> getCheapestDateOffers(FlightSearch flightSearch, String token) {
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
