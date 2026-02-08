package com.szte.skyScope.factories;

import com.szte.skyScope.models.FlightSearch;
import java.time.LocalDate;

public class FlightSearchFactory {

  public static FlightSearch createValidFlightSearch() {
    FlightSearch flightSearch = new FlightSearch();
    flightSearch.setOriginCity("Budapest");
    flightSearch.setDestinationCity("London");
    flightSearch.setDepartureDate(LocalDate.now().plusDays(1).toString());
    flightSearch.setReturnDate(LocalDate.now().plusDays(10).toString());
    flightSearch.setOneWay(false);
    flightSearch.setNumberOfAdults("1");
    flightSearch.setTravelClass("ECONOMY");
    return flightSearch;
  }

  public static FlightSearch createFlighSearchInvalidOriginCty() {
    FlightSearch flightSearch = new FlightSearch();
    flightSearch.setOriginCityIata(null);
    flightSearch.setDestinationCityIata("BUD");
    return flightSearch;
  }

  public static FlightSearch createFlighSearchInvalidDestinationCity() {
    FlightSearch flightSearch = new FlightSearch();
    flightSearch.setOriginCityIata("BUD");
    flightSearch.setDestinationCityIata(null);
    return flightSearch;
  }
}
