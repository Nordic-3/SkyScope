package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Services.OrderResultService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderResultServiceImpl implements OrderResultService {

    @Override
    public List<FlightOffers> orderOffersByDeffault(List<FlightOffers> flightOffers) {
        return flightOffers
                .stream()
                .sorted(Comparator.comparingInt(
                                offer -> offer.getItineraries().getFirst().getSegments().size()))
                .toList();
    }
}
