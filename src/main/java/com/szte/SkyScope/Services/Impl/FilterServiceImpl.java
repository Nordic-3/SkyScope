package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FilterAttribute;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.FilterService;
import com.szte.SkyScope.Services.SortResultService;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService {

  private final SortResultService sortResultService;

  @Autowired
  public FilterServiceImpl(SortResultService sortResultService) {
    this.sortResultService = sortResultService;
  }

  @Override
  public List<String> getAirlineNames(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getOperating().getCarrierName())
        .distinct()
        .filter(airlineName -> !airlineName.isEmpty())
        .toList();
  }

  @Override
  public List<String> getTransferNumbers(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .map(FlightOffers.Itinerary::getTransferNumber)
        .distinct()
        .toList();
  }

  @Override
  public List<String> getTransferDurations(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(
            itinerary ->
                FlightOfferFormatter.calculateLayoverTime(itinerary.getSegments()).stream())
        .distinct()
        .toList();
  }

  @Override
  public List<String> getAirplanes(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getAircraft().getName())
        .filter(aircraft -> !aircraft.isEmpty())
        .distinct()
        .toList();
  }

  @Override
  public List<FlightOffers> filterOffers(List<FlightOffers> flightOffers, FilterAttribute filter) {
    return flightOffers.stream()
        .filter(
            offer ->
                filterPrice(offer, filter)
                    && filterAirline(offer, filter)
                    && filterTransferNumber(offer, filter)
                    && filterTransferTime(offer, filter)
                    && filterAirplane(offer, filter))
        .toList();
  }

  @Override
  public String getMaxPrice(List<FlightOffers> flightOffers) {
    List<FlightOffers> orderedOffers = sortResultService.sortOffersByPriceDSC(flightOffers);
    if (!orderedOffers.isEmpty()) {
      return FlightOfferFormatter.formatPrice(orderedOffers.getFirst().getPrice().getTotal())
          + "Ft";
    }
    return "";
  }

  private boolean filterPrice(FlightOffers offer, FilterAttribute filter) {
    return filter.getMaxPrice().isEmpty()
        || Integer.parseInt(offer.getPrice().getTotal().split("\\.")[0])
            <= Integer.parseInt(filter.getMaxPrice());
  }

  private boolean filterAirline(FlightOffers offer, FilterAttribute filter) {
    return filter.getAirlines().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(
                segment -> filter.getAirlines().contains(segment.getOperating().getCarrierName()));
  }

  private boolean filterTransferNumber(FlightOffers offer, FilterAttribute filter) {
    return filter.getTransferNumber().isEmpty()
        || offer.getItineraries().stream()
            .anyMatch(
                itinerary -> itinerary.getTransferNumber().equals(filter.getTransferNumber()));
  }

  private boolean filterTransferTime(FlightOffers offer, FilterAttribute filter) {
    List<String> layoverTime = getOutGoingFlightLayoverTime(offer);
    return filter.getTransferDuration().isEmpty()
        || layoverTime.contains(filter.getTransferDuration());
  }

  private boolean filterAirplane(FlightOffers offer, FilterAttribute filter) {
    return filter.getAirplanes().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(segment -> filter.getAirplanes().contains(segment.getAircraft().getName()));
  }

  private List<String> getOutGoingFlightLayoverTime(FlightOffers flightOffer) {
    return flightOffer.getItineraries().stream()
        .flatMap(
            itinerary ->
                FlightOfferFormatter.calculateLayoverTime(itinerary.getSegments()).stream())
        .toList();
  }
}
