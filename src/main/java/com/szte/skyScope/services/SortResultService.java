package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import java.util.List;

public interface SortResultService {
  List<FlightOfferDTO> sortOffersByDeffault(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByPriceASC(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByPriceDSC(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByFlyTimeAsc(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByFlyTimeDsc(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByTransferTimeDsc(List<FlightOfferDTO> flightOffers);
}
