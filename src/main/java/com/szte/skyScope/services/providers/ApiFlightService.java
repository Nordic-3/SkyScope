package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.FlightServiceProvider;
import com.szte.skyScope.services.impl.FlightServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class ApiFlightService implements FlightServiceProvider {
  private final ApplicationConfig applicationConfig;
  private final RestClient restClient = RestClient.create();

  public ApiFlightService(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Override
  public String getFlightOffers(FlightSearch flightSearch, String token) {
    UriComponentsBuilder uriBulder =
        UriComponentsBuilder.fromUriString(applicationConfig.getAmadeusFlightOfferSearchApi());
    uriBulder.queryParam("originLocationCode", flightSearch.getOriginCityIata());
    uriBulder.queryParam("destinationLocationCode", flightSearch.getDestinationCityIata());
    uriBulder.queryParam("departureDate", flightSearch.getDepartureDate());
    uriBulder.queryParam("adults", flightSearch.getNumberOfAdults());
    uriBulder.queryParam("currencyCode", "HUF");
    uriBulder.queryParam("max", 100);
    bindOptionalParameters(uriBulder, flightSearch);
    return restClient
        .get()
        .uri(uriBulder.build(true).toUri())
        .header("Authorization", "Bearer " + token)
        .header("Accept", "application/json")
        .retrieve()
        .body(String.class);
  }

  @Override
  public Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token) {
    String response;
    try {
      response =
          restClient
              .get()
              .uri(
                  applicationConfig.getAmadeusAirlineCode(),
                  String.join(",", carrierDictionary.keySet()))
              .header("Authorization", "Bearer " + token)
              .retrieve()
              .body(String.class);
      return Parser.getIcaoCodesFromJson(response);
    } catch (Exception exception) {
      Logger.getLogger(FlightServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return new HashMap<>();
    }
  }

  private void bindOptionalParameters(UriComponentsBuilder uriBulder, FlightSearch flightSearch) {
    if (isNotNullAndNotEmpty(flightSearch.getReturnDate())) {
      uriBulder.queryParam("returnDate", flightSearch.getReturnDate());
    }
    if (isNotNullAndNotEmpty(flightSearch.getNumberOfChildren())) {
      uriBulder.queryParam("children", flightSearch.getNumberOfChildren());
    }
    if (isNotNullAndNotEmpty(flightSearch.getNumberOfInfants())) {
      uriBulder.queryParam("infants", flightSearch.getNumberOfInfants());
    }
    if (flightSearch.getTravelClass() != null && !flightSearch.getTravelClass().equals("ALL")) {
      uriBulder.queryParam("travelClass", flightSearch.getTravelClass());
    }
  }

  private boolean isNotNullAndNotEmpty(String dataToCheck) {
    return dataToCheck != null && !dataToCheck.isEmpty();
  }
}
