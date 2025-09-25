package com.szte.SkyScope.Models;

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

  public FlightSearch() {}

  public String getNumberOfChildren() {
    return numberOfChildren;
  }

  public void setNumberOfChildren(String numberOfChildren) {
    this.numberOfChildren = numberOfChildren;
  }

  public String getNumberOfInfants() {
    return numberOfInfants;
  }

  public void setNumberOfInfants(String numberOfInfants) {
    this.numberOfInfants = numberOfInfants;
  }

  public String getTravelClass() {
    return travelClass;
  }

  public void setTravelClass(String travelClass) {
    this.travelClass = travelClass;
  }

  public String getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(String maxPrice) {
    this.maxPrice = maxPrice;
  }

  public String getNumberOfAdults() {
    return numberOfAdults;
  }

  public void setNumberOfAdults(String numberOfAdults) {
    this.numberOfAdults = numberOfAdults;
  }

  public String getOriginCityIata() {
    return originCityIata;
  }

  public void setOriginCityIata(String originCityIata) {
    this.originCityIata = originCityIata;
  }

  public String getDestinationCityIata() {
    return destinationCityIata;
  }

  public void setDestinationCityIata(String destinationCityIata) {
    this.destinationCityIata = destinationCityIata;
  }

  public String getOriginCity() {
    return originCity;
  }

  public boolean isOneWay() {
    return oneWay;
  }

  public void setOneWay(boolean oneWay) {
    this.oneWay = oneWay;
  }

  public void setOriginCity(String originCity) {
    this.originCity = originCity;
  }

  public String getDestinationCity() {
    return destinationCity;
  }

  public void setDestinationCity(String destinationCity) {
    this.destinationCity = destinationCity;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public String getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(String returnDate) {
    this.returnDate = returnDate;
  }
}
