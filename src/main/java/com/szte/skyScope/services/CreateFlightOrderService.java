package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.CreateOrder;
import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.Traveller;
import com.szte.skyScope.models.TravellerWrapper;
import java.util.List;

public interface CreateFlightOrderService {
  FinalPriceResponse getFinalPrice(FlightOfferDTO flightOffer, String token);

  FlightOfferDTO getSelectedOffer(List<FlightOfferDTO> flightOffers, String offerId);

  void setTravellers(TravellerWrapper travellers, String email, FlightOfferDTO flightOffer);

  void setPassportValidations(TravellerWrapper travellers);

  void setContacts(TravellerWrapper travellers);

  void createOrder(CreateOrder createOrderBody, String token);

  String getTestApiToken();

  void setTravellersName(FlightOfferDTO selectedOffer, List<Traveller> travelers);
}
