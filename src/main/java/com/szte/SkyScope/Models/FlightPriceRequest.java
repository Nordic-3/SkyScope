package com.szte.SkyScope.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightPriceRequest {
  private Data data;

  public FlightPriceRequest(Data data) {
    this.data = data;
  }

  public FlightPriceRequest() {}

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {
    private String type = "flight-offers-pricing";

    private List<FlightOffers> flightOffers = new ArrayList<>();

    public Data() {}

    public Data(FlightOffers flightOffer) {
      this.flightOffers.add(flightOffer);
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<FlightOffers> getFlightOffers() {
      return flightOffers;
    }

    public void setFlightOffers(List<FlightOffers> flightOffers) {
      this.flightOffers = flightOffers;
    }
  }
}
