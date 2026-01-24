package com.szte.skyScope.services;

import com.szte.skyScope.models.FinalPriceResponse;
import com.szte.skyScope.models.FlightPriceRequest;

public interface CreateFlightOrderProvider {
  FinalPriceResponse getFinalPrice(FlightPriceRequest request, String token);
}
