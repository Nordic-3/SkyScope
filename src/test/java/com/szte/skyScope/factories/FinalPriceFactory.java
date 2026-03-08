package com.szte.skyScope.factories;

import com.szte.skyScope.models.FinalPriceResponse;

public class FinalPriceFactory {

  public static FinalPriceResponse.FlightOffer createFlightOffer(String totalAmount) {
    FinalPriceResponse.FlightOffer offer = new FinalPriceResponse.FlightOffer();
    offer.setId("test-offer-id");
    offer.setPrice(createPrice(totalAmount));
    return offer;
  }

  public static FinalPriceResponse.FlightOffer.Price createPrice(String total) {
    FinalPriceResponse.FlightOffer.Price price = new FinalPriceResponse.FlightOffer.Price();
    price.setTotal(total);
    price.setCurrency("HUF");
    return price;
  }
}
