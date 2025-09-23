package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;

import java.util.List;

public interface SortResultService {
    List<FlightOffers> sortOffersByDeffault(List<FlightOffers> flightOffers);
    List<FlightOffers> sortOffersByPriceASC(List<FlightOffers> flightOffers);
    List<FlightOffers> sortOffersByPriceDSC(List<FlightOffers> flightOffers);
    List<FlightOffers> sortOffersByFlyTimeAsc(List<FlightOffers> flightOffers);
    List<FlightOffers> sortOffersByFlyTimeDsc(List<FlightOffers> flightOffers);
    List<FlightOffers> sortOffersByTransferTimeDsc(List<FlightOffers> flightOffers);
}
