package com.szte.skyScope.models;

import java.util.List;

public class CreateOrder {
  private Data data = new Data();

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public static class Data {
    private String type = "flight-order";
    private List<FinalPriceResponse.FlightOffer> flightOffers;
    private List<Traveller> travelers;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<FinalPriceResponse.FlightOffer> getFlightOffers() {
      return flightOffers;
    }

    public void setFlightOffers(List<FinalPriceResponse.FlightOffer> flightOffers) {
      this.flightOffers = flightOffers;
    }

    public List<Traveller> getTravelers() {
      return travelers;
    }

    public void setTravelers(List<Traveller> travelers) {
      this.travelers = travelers;
    }
  }
}
