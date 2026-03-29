package com.szte.skyScope.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.szte.skyScope.dtos.FlightOfferDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightPriceRequest {
  private Data data;

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {
    private String type = "flight-offers-pricing";

    private List<FlightOfferDTO> flightOffers = new ArrayList<>();

    public Data(FlightOfferDTO flightOffer) {
      this.flightOffers.add(flightOffer);
    }
  }
}
