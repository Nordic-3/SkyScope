package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;

import java.util.List;

public interface OrderResultService {
    List<FlightOffers> orderOffersByDeffault(List<FlightOffers> flightOffers);
}
