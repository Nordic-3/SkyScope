package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.TravellerWrapper;
import java.util.List;

public interface CreateFlightOrderService {
  FlightOffers getFinalPrice(FlightOffers flightOffers, String token);

  FlightOffers getSelectedOffer(List<FlightOffers> flightOffers, String offerId);

  void setTravellers(TravellerWrapper travellers, String email, FlightOffers flightOffers);

  void setPassportValidations(TravellerWrapper travellers);

  void setContacts(TravellerWrapper travellers);
}
