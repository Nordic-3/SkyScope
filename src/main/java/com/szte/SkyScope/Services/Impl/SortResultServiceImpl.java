package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.SortResultService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Service
public class SortResultServiceImpl implements SortResultService {

    @Override
    public List<FlightOffers> sortOffersByDeffault(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted(Comparator.comparingInt(
                                offer -> offer.getItineraries().getFirst().getSegments().size()))
                .toList();
    }

    @Override
    public List<FlightOffers> sortOffersByPriceASC(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted((o1, o2) -> {
                    if (!o1.getPrice().getTotal().isEmpty() && !o2.getPrice().getTotal().isEmpty()) {
                        if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                                < Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                            return -1;
                        }
                    }
                    return 1;})
                .toList();
    }

    @Override
    public List<FlightOffers> sortOffersByPriceDSC(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted((o1, o2) -> {
                    if (!o1.getPrice().getTotal().isEmpty() && !o2.getPrice().getTotal().isEmpty()) {
                        if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                                > Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                            return -1;
                        }
                    }
                    return 1;})
                .toList();
    }

    @Override
    public List<FlightOffers> sortOffersByFlyTimeAsc(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted((o1, o2) -> {
                    if (!o1.getItineraries().getFirst().getDuration().isEmpty() &&
                            !o2.getItineraries().getFirst().getDuration().isEmpty()) {
                        if (Duration.parse(
                                o1.getItineraries().getFirst().getDuration())
                                        .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration())) < 0 ) {
                            return -1;
                        }
                    }
                    return 1;
                })
                .toList();
    }

    @Override
    public List<FlightOffers> sortOffersByFlyTimeDsc(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted((o1, o2) -> {
                    if (!o1.getItineraries().getFirst().getDuration().isEmpty() &&
                            !o2.getItineraries().getFirst().getDuration().isEmpty()) {
                        if (Duration.parse(
                                        o1.getItineraries().getFirst().getDuration())
                                .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration())) > 0 ) {
                            return -1;
                        }
                    }
                    return 1;
                })
                .toList();
    }

    @Override
    public List<FlightOffers> sortOffersByTransferTimeDsc(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted(Comparator.comparingInt(
                        (FlightOffers offer) -> offer.getItineraries().getFirst().getSegments().size())
                        .reversed())
                .toList();
    }
}
