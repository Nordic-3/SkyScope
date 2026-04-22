package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.models.TravellerWrapper;

public interface InputValidationService {
  String validateInputFields(FlightSearch flightSearch);

  String validateIataCodes(FlightSearch flightSearch);

  String validateTravellers(TravellerWrapper travellers, FlightOfferDTO flightOffer);
}
