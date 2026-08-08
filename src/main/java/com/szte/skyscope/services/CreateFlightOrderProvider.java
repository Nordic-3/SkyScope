package com.szte.skyscope.services;

import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.FlightPriceRequest;

public interface CreateFlightOrderProvider {
  FinalPriceResponse getFinalPrice(FlightPriceRequest request, String token);
}
