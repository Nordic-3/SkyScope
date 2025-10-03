package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;

public interface CreateFlightOrderService {
  FlightOffers getFinalPrice(FlightOffers flightOffers, String token);
}
