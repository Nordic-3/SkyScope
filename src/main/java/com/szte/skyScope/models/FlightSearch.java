package com.szte.skyScope.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FlightSearch {
  private String originCity;
  private String destinationCity;
  private boolean oneWay;
  private String departureDate;
  private String returnDate;
  private String originCityIata;
  private String destinationCityIata;
  private String numberOfAdults = "1";
  private String numberOfChildren;
  private String numberOfInfants;
  private String travelClass;
  private String maxPrice;

  public FlightSearch(FlightSearch flightSearch) {
    this.originCity = flightSearch.getOriginCity();
    this.destinationCity = flightSearch.getDestinationCity();
    this.oneWay = flightSearch.isOneWay();
    this.departureDate = flightSearch.getDepartureDate();
    this.returnDate = flightSearch.getReturnDate();
    this.originCityIata = flightSearch.getOriginCityIata();
    this.destinationCityIata = flightSearch.getDestinationCityIata();
    this.numberOfAdults = flightSearch.getNumberOfAdults();
    this.numberOfChildren = flightSearch.getNumberOfChildren();
    this.numberOfInfants = flightSearch.getNumberOfInfants();
    this.travelClass = flightSearch.getTravelClass();
    this.maxPrice = flightSearch.getMaxPrice();
  }
}
