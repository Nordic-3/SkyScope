package com.szte.skyScope.services.impl;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.ChosenFilters;
import com.szte.skyScope.models.FilterValue;
import com.szte.skyScope.services.FilterService;
import com.szte.skyScope.services.SortResultService;
import com.szte.skyScope.utils.FlightOfferFormatter;
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
  public List<FlightOfferDTO> filterOffers(
      List<FlightOfferDTO> flightOffers, ChosenFilters filter) {
    return flightOffers.stream()
        .filter(
            offer ->
                filterPrice(offer, filter)
                    && filterAirline(offer, filter)
                    && filterTransferNumber(offer, filter)
                    && filterLayoverTime(offer, filter)
                    && filterAirplane(offer, filter)
                    && filterCurrentlyFlyng(offer, filter))
        .toList();
  }

  @Override
  public FilterValue getFilterOptions(List<FlightOfferDTO> flightOffers) {
    return new FilterValue(
        getAirlineNames(flightOffers),
        getTransferNumbers(flightOffers),
        getTransferDurations(flightOffers),
        getAirplanes(flightOffers),
        getMaxPrice(flightOffers),
        isCurrentlyFlying(flightOffers));
  }

  private boolean isCurrentlyFlying(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .anyMatch(FlightOfferDTO.Segment::isCurrentlyFlying);
  }

  private List<String> getAirlineNames(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getOperating().getCarrierName())
        .distinct()
        .filter(airlineName -> !airlineName.isEmpty())
        .toList();
  }

  private List<String> getTransferNumbers(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .map(
            offer ->
                offer.getItineraries().stream()
                    .mapToInt(itinerary -> itinerary.getSegments().size() - 1)
                    .max()
                    .orElse(0))
        .distinct()
        .sorted()
        .map(number -> " " + number + " átszállás")
        .toList();
  }

  private List<Duration> getTransferDurations(List<FlightOfferDTO> flightOffers) {
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

  private List<String> getAirplanes(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream())
        .map(segment -> segment.getAircraft().getName())
        .filter(aircraft -> !aircraft.isEmpty())
        .distinct()
        .toList();
  }

  private String getMaxPrice(List<FlightOfferDTO> flightOffers) {
    List<FlightOfferDTO> orderedOffers = sortResultService.sortOffersByPriceDSC(flightOffers);
    if (!orderedOffers.isEmpty()) {
      return FlightOfferFormatter.formatPrice(orderedOffers.getFirst().getPrice().getTotal())
          + "Ft";
    }
    return "";
  }

  private boolean filterCurrentlyFlyng(FlightOfferDTO offer, ChosenFilters filter) {
    return !filter.isCurrentlyFlying()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(FlightOfferDTO.Segment::isCurrentlyFlying);
  }

  private boolean filterPrice(FlightOfferDTO offer, ChosenFilters filter) {
    return filter.getMaxPrice().isEmpty()
        || Integer.parseInt(offer.getPrice().getTotal().split("\\.")[0])
            <= Integer.parseInt(filter.getMaxPrice().split("\\.")[0]);
  }

  private boolean filterAirline(FlightOfferDTO offer, ChosenFilters filter) {
    return filter.getAirlines().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(
                segment -> filter.getAirlines().contains(segment.getOperating().getCarrierName()));
  }

  private boolean filterTransferNumber(FlightOfferDTO offer, ChosenFilters filter) {
    return filter.getTransferNumber().isEmpty()
        || offer.getItineraries().stream()
            .allMatch(
                itinerary ->
                    parseTransferNumber(itinerary.getTransferNumber())
                        <= parseTransferNumber(filter.getTransferNumber()));
  }

  private boolean filterLayoverTime(FlightOfferDTO offer, ChosenFilters filter) {
    List<Duration> layoverTime = getLayoverTime(offer);
    return filter.getTransferDuration().isEmpty()
        || layoverTime.stream()
            .allMatch(
                duration -> duration.compareTo(Duration.parse(filter.getTransferDuration())) <= 0);
  }

  private boolean filterAirplane(FlightOfferDTO offer, ChosenFilters filter) {
    return filter.getAirplanes().isEmpty()
        || offer.getItineraries().stream()
            .flatMap(itinerary -> itinerary.getSegments().stream())
            .anyMatch(segment -> filter.getAirplanes().contains(segment.getAircraft().getName()));
  }

  private List<Duration> getLayoverTime(FlightOfferDTO flightOffer) {
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
