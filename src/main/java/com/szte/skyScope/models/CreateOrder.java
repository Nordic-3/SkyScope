package com.szte.skyScope.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateOrder {
  private Data data = new Data();

  @Setter
  @Getter
  public static class Data {
    private String type = "flight-order";
    private List<FinalPriceResponse.FlightOffer> flightOffers;
    private List<Traveller> travelers;
  }
}
