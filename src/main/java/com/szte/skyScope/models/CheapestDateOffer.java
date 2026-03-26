package com.szte.skyScope.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheapestDateOffer {
  private String origin;
  private String destination;
  private String departureDate;
  private String returnDate;
  private int price;
  private String flightOfferApiLink;
}
