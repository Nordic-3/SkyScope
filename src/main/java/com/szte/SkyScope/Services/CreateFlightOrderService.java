package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.FlightOfferDTO;
import com.szte.SkyScope.Models.CreateOrder;
import com.szte.SkyScope.Models.FinalPriceResponse;
import com.szte.SkyScope.Models.Traveller;
import com.szte.SkyScope.Models.TravellerWrapper;
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
