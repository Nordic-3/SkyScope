package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FilterAttribute;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.FilterService;
import com.szte.SkyScope.Services.SortResultService;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FilterServiceImpl implements FilterService {

    private final SortResultService sortResultService;

    @Autowired
    public FilterServiceImpl(SortResultService sortResultService) {
        this.sortResultService = sortResultService;
    }

    @Override
    public List<String> getAirlineNames(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .flatMap(offer -> offer.getItineraries().stream())
                .flatMap(itinerary -> itinerary.getSegments().stream())
                .map(segment -> segment.getOperating().getCarrierName())
                .distinct()
                .filter(airlineName -> !airlineName.isEmpty())
                .toList();
    }

    @Override
    public List<String> getTransferNumbers(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .map(offer -> offer.getItineraries().getFirst())
                .map(FlightOffers.Itinerary::getTransferNumber)
                .distinct()
                .toList();

    }

    @Override
    public List<String> getTransferDurations(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .map(offer -> offer.getItineraries().getFirst())
                .map(itinerary -> {
                    List<String> layovers = FlightOfferFormatter.calculateLayoverTime(itinerary.getSegments());
                    return layovers.isEmpty() ? null : layovers.getFirst();
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public List<String> getAirplanes(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .flatMap(offer -> offer.getItineraries().stream())
                .flatMap(itinerary -> itinerary.getSegments().stream())
                .map(segment -> segment.getAircraft().getName())
                .filter(aircraft -> !aircraft.isEmpty())
                .distinct()
                .toList();
    }

    @Override
    public List<FlightOffers> filterOffers(List<FlightOffers> flightOffers, FilterAttribute filter) {
        return flightOffers
                .stream()
                .filter(offer -> filterPrice(offer, filter)
                        && filterAirline(offer, filter)
                        && filterTransferNumber(offer, filter)
                        && filterTransferTime(offer, filter)
                        && filterAirplane(offer, filter))
                .toList();
    }

    @Override
    public String getMaxPrice(List<FlightOffers> flightOffers) {
        return FlightOfferFormatter.formatPrice(
                sortResultService.sortOffersByPriceDSC(flightOffers).getFirst().getPrice().getTotal()) + "Ft";
    }

    private boolean filterPrice(FlightOffers offer, FilterAttribute filter) {
        return filter.getMaxPrice().isEmpty()
                || Integer.parseInt(offer.getPrice().getTotal().split("\\.")[0]) <= Integer.parseInt(filter.getMaxPrice());
    }

    private boolean filterAirline(FlightOffers offer, FilterAttribute filter) {
        return filter.getAirlines().isEmpty()
                || offer.getItineraries().stream()
                .flatMap(itinerary -> itinerary.getSegments().stream())
                .anyMatch(segment -> filter.getAirlines().contains(segment.getOperating().getCarrierName()));
    }

    private boolean filterTransferNumber(FlightOffers offer, FilterAttribute filter) {
        return filter.getTransferNumber().isEmpty()
                || offer.getItineraries().getFirst().getTransferNumber().equals(filter.getTransferNumber());
    }

    private boolean filterTransferTime(FlightOffers offer, FilterAttribute filter) {
        String layoverTime = getOutGoingFlightLayoverTime(offer);
        return filter.getTransferDuration().isEmpty() || layoverTime.isEmpty()
                || layoverTime.equals(filter.getTransferDuration());
    }

    private boolean filterAirplane(FlightOffers offer, FilterAttribute filter) {
        return filter.getAirplanes().isEmpty()
                || offer.getItineraries().stream()
                .flatMap(itinerary -> itinerary.getSegments().stream())
                .anyMatch(segment -> filter.getAirplanes().contains(segment.getAircraft().getName()));
    }

    private String getOutGoingFlightLayoverTime(FlightOffers flightOffer) {
        if (flightOffer.getItineraries().getFirst().getSegments().size() > 1) {
            return FlightOfferFormatter.calculateLayoverTime(flightOffer.getItineraries().getFirst().getSegments()).getFirst();
        }
        return "";
    }
}
