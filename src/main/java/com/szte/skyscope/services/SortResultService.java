package com.szte.skyscope.services;

import com.szte.skyscope.dtos.FlightOfferDTO;
import java.util.List;

public interface SortResultService {
  List<FlightOfferDTO> sortOffersByDeffault(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByPriceASC(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByPriceDSC(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByFlyTimeAsc(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByFlyTimeDsc(List<FlightOfferDTO> flightOffers);

  List<FlightOfferDTO> sortOffersByTransferTimeDsc(List<FlightOfferDTO> flightOffers);
}
