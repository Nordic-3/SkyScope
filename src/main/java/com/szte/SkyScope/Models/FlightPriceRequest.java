package com.szte.SkyScope.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.szte.SkyScope.DTOs.FlightOfferDTO;
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

    private List<FlightOfferDTO> flightOffers = new ArrayList<>();

    public Data() {}

    public Data(FlightOfferDTO flightOffer) {
      this.flightOffers.add(flightOffer);
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<FlightOfferDTO> getFlightOffers() {
      return flightOffers;
    }

    public void setFlightOffers(List<FlightOfferDTO> flightOffers) {
      this.flightOffers = flightOffers;
    }
  }
}
