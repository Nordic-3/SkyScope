package com.szte.skyscope.services;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.CreateOrder;
import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.Traveller;
import com.szte.skyscope.models.TravellerWrapper;
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
