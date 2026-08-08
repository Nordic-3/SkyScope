package com.szte.skyscope.factories;

import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.utils.Constants;
import java.time.LocalDate;
import java.time.ZoneId;

public class FlightSearchFactory {

  public static FlightSearch createValidFlightSearch() {
    FlightSearch flightSearch = new FlightSearch();
    flightSearch.setOriginCity("Budapest");
    flightSearch.setDestinationCity("London");
    flightSearch.setDepartureDate(
        LocalDate.now(ZoneId.of(Constants.ZONE_ID)).plusDays(1).toString());
    flightSearch.setReturnDate(LocalDate.now(ZoneId.of(Constants.ZONE_ID)).plusDays(10).toString());
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
