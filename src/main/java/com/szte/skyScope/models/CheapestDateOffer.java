package com.szte.skyScope.models;

public class CheapestDateOffer {
  private String origin;
  private String destination;
  private String departureDate;
  private String returnDate;
  private int price;
  private String flightOfferApiLink;

  public CheapestDateOffer() {}

  public CheapestDateOffer(
      String origin,
      String destination,
      String departureDate,
      String returnDate,
      int price,
      String flightOfferApiLink) {
    this.origin = origin;
    this.destination = destination;
    this.departureDate = departureDate;
    this.returnDate = returnDate;
    this.price = price;
    this.flightOfferApiLink = flightOfferApiLink;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
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

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getFlightOfferApiLink() {
    return flightOfferApiLink;
  }

  public void setFlightOfferApiLink(String flightOfferApiLink) {
    this.flightOfferApiLink = flightOfferApiLink;
  }
}
