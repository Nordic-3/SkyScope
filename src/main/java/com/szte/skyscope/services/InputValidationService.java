package com.szte.skyscope.services;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.models.TravellerWrapper;

public interface InputValidationService {
  String validateInputFields(FlightSearch flightSearch);

  String validateIataCodes(FlightSearch flightSearch);

  String validateTravellers(TravellerWrapper travellers, FlightOfferDTO flightOffer);
}
