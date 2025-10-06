package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.ChosenFilters;
import com.szte.SkyScope.Models.FilterValue;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.FilterService;
import com.szte.SkyScope.Services.SortResultService;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.time.Duration;
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
  public List<FlightOffers> filterOffers(List<FlightOffers> flightOffers, ChosenFilters filter) {
    return flightOffers.stream()
        .filter(
            offer ->
                filterPrice(offer, filter)
                    && filterAirline(offer, filter)
                    && filterTransferNumber(offer, filter)
                    && filterLayoverTime(offer, filter)
                    && filterAirplane(offer, filter))
        .toList();
  }

  @Override
  public FilterValue getFilterOptions(List<FlightOffers> flightOffers) {
    return new FilterValue(
        getAirlineNames(flightOffers),
        getTransferNumbers(flightOffers),
        getTransferDurations(flightOffers),
        getAirplanes(flightOffers),
        getMaxPrice(flightOffers));
  }

  private List<String> getAirlineNames(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getOperating().getCarrierName())
        .distinct()
        .filter(airlineName -> !airlineName.isEmpty())
        .toList();
  }

  private List<String> getTransferNumbers(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .map(FlightOffers.Itinerary::getTransferNumber)
        .distinct()
        .toList();
  }

  private List<Duration> getTransferDurations(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .map(
            offer ->
                offer.getItineraries().stream()
                    .flatMap(
                        itinerary ->
                            FlightOfferFormatter.calculateLayoverTime(itinerary.getSegments())
                                .stream())
                    .max(Duration::compareTo)
                    .orElse(Duration.ZERO))
        .distinct()
        .filter(duration -> !duration.isZero())
        .sorted(Duration::compareTo)
        .toList();
  }

  private List<String> getAirplanes(List<FlightOffers> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getAircraft().getName())
        .filter(aircraft -> !aircraft.isEmpty())
        .distinct()
        .toList();
  }

  private String getMaxPrice(List<FlightOffers> flightOffers) {
    List<FlightOffers> orderedOffers = sortResultService.sortOffersByPriceDSC(flightOffers);
    if (!orderedOffers.isEmpty()) {
      return FlightOfferFormatter.formatPrice(orderedOffers.getFirst().getPrice().getTotal())
          + "Ft";
    }
    return "";
  }

  private boolean filterPrice(FlightOffers offer, ChosenFilters filter) {
    return filter.getMaxPrice().isEmpty()
        || Integer.parseInt(offer.getPrice().getTotal().split("\\.")[0])
            <= Integer.parseInt(filter.getMaxPrice());
  }

  private boolean filterAirline(FlightOffers offer, ChosenFilters filter) {
    return filter.getAirlines().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(
                segment -> filter.getAirlines().contains(segment.getOperating().getCarrierName()));
  }

  private boolean filterTransferNumber(FlightOffers offer, ChosenFilters filter) {
    return filter.getTransferNumber().isEmpty()
        || offer.getItineraries().stream()
            .allMatch(
                itinerary ->
                    parseTransferNumber(itinerary.getTransferNumber())
                        <= parseTransferNumber(filter.getTransferNumber()));
  }

  private boolean filterLayoverTime(FlightOffers offer, ChosenFilters filter) {
    List<Duration> layoverTime = getLayoverTime(offer);
    return filter.getTransferDuration().isEmpty()
        || layoverTime.stream()
            .allMatch(
                duration -> duration.compareTo(Duration.parse(filter.getTransferDuration())) <= 0);
  }

  private boolean filterAirplane(FlightOffers offer, ChosenFilters filter) {
    return filter.getAirplanes().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(segment -> filter.getAirplanes().contains(segment.getAircraft().getName()));
  }

  private List<Duration> getLayoverTime(FlightOffers flightOffer) {
    return flightOffer.getItineraries().stream()
        .flatMap(
            itinerary ->
                FlightOfferFormatter.calculateLayoverTime(itinerary.getSegments()).stream())
        .toList();
  }

  private int parseTransferNumber(String transferNumber) {
    return Integer.parseInt(transferNumber.split(" ")[1]);
  }
}
