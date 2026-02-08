package com.szte.skyScope.factories;

import com.szte.skyScope.dtos.FlightOfferDTO;
import java.time.LocalDate;
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

  public static FlightOfferDTO createFlightOfferWithDurations() {
    FlightOfferDTO flightOffer = new FlightOfferDTO();
    FlightOfferDTO.Itinerary itinerary = new FlightOfferDTO.Itinerary();
    FlightOfferDTO.Segment segment = new FlightOfferDTO.Segment();
    segment.getOperating().setCarrierName("United Airlines");
    segment.getAircraft().setName("Boeing");
    segment.setCurrentlyFlying(true);
    itinerary.setSegments(List.of(segment));
    flightOffer.setItineraries(List.of(itinerary));
    flightOffer.getPrice().setTotal("150000.0");
    FlightOfferDTO.FlightSchedule departure = new FlightOfferDTO.FlightSchedule();
    departure.setAt(LocalDate.now().plusDays(10) + "T10:00:00");
    segment.setDeparture(departure);
    FlightOfferDTO.FlightSchedule arrival = new FlightOfferDTO.FlightSchedule();
    arrival.setAt(LocalDate.now().plusDays(10) + "T15:00:00");
    segment.setArrival(arrival);
    itinerary.setSegments(java.util.List.of(segment));
    flightOffer.setItineraries(java.util.List.of(itinerary));
    return flightOffer;
  }

  public static FlightOfferDTO createFlightOfferWithTransferNumber() {
    FlightOfferDTO flightOffer = new FlightOfferDTO();
    FlightOfferDTO.Itinerary itinerary = new FlightOfferDTO.Itinerary();
    FlightOfferDTO.Segment seg1 = new FlightOfferDTO.Segment();
    FlightOfferDTO.Segment seg2 = new FlightOfferDTO.Segment();

    FlightOfferDTO.FlightSchedule departure = new FlightOfferDTO.FlightSchedule();
    FlightOfferDTO.FlightSchedule arrival = new FlightOfferDTO.FlightSchedule();
    departure.setAt(LocalDate.now().plusDays(1) + "T09:00:00");
    arrival.setAt(LocalDate.now().plusDays(1) + "T11:00:00");
    seg1.setDeparture(departure);
    seg1.setArrival(arrival);

    FlightOfferDTO.FlightSchedule departure1 = new FlightOfferDTO.FlightSchedule();
    FlightOfferDTO.FlightSchedule arrival1 = new FlightOfferDTO.FlightSchedule();
    departure1.setAt(LocalDate.now().plusDays(1) + "T14:00:00");
    arrival1.setAt(LocalDate.now().plusDays(1) + "T15:00:00");
    seg2.setDeparture(departure1);
    seg2.setArrival(arrival1);

    itinerary.setSegments(List.of(seg1, seg2));
    flightOffer.setItineraries(List.of(itinerary));
    flightOffer.getPrice().setTotal("100000");
    return flightOffer;
  }
}
