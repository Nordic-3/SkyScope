package com.szte.skyScope.factories;

import com.szte.skyScope.dtos.FlightOfferDTO;
import java.util.List;

public class FlightOfferDTOFactory {

  public static FlightOfferDTO createFlightOfferWithBritishAirways() {
    FlightOfferDTO offer = new FlightOfferDTO();
    FlightOfferDTO.Itinerary itinerary = new FlightOfferDTO.Itinerary();
    FlightOfferDTO.Segment segment = new FlightOfferDTO.Segment();
    segment.getOperating().setCarrierName("British Airways");
    segment.getAircraft().setName("Boeing");
    segment.setCurrentlyFlying(true);
    itinerary.setSegments(List.of(segment));
    offer.setItineraries(List.of(itinerary));
    offer.getPrice().setTotal("150000.0");
    return offer;
  }

  public static FlightOfferDTO createFlightOfferWithLufthansa() {
    FlightOfferDTO offer = new FlightOfferDTO();
    FlightOfferDTO.Itinerary itinerary = new FlightOfferDTO.Itinerary();
    FlightOfferDTO.Segment segment = new FlightOfferDTO.Segment();
    segment.getOperating().setCarrierName("Lufthansa");
    segment.getAircraft().setName("Airbus");
    itinerary.setSegments(List.of(segment));
    offer.setItineraries(List.of(itinerary));
    offer.getPrice().setTotal("300000");
    return offer;
  }

  public static FlightOfferDTO createFlightOfferWithTravelerPricing(String totalPrice) {
    FlightOfferDTO offer = new FlightOfferDTO();
    FlightOfferDTO.TravelerPricing pricing = new FlightOfferDTO.TravelerPricing();
    pricing.setTravelerType("ADULT");
    FlightOfferDTO.Price price = new FlightOfferDTO.Price();
    price.setTotal(totalPrice);
    pricing.setPrice(price);
    offer.setTravelerPricings(List.of(pricing));
    return offer;
  }
}
