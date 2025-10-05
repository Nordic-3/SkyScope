package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;
import java.util.List;

public interface CreateFlightOrderService {
  FlightOffers getFinalPrice(FlightOffers flightOffers, String token);

  FlightOffers getSelectedOffer(List<FlightOffers> flightOffers, String offerId);
}
