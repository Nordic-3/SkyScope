package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.RegisterUser;
import com.szte.SkyScope.Models.TravellerWrapper;

public interface InputValidationService {
  String validateInputFields(FlightSearch flightSearch);

  String validateIataCodes(FlightSearch flightSearch);

  String validatePasswordAndEmail(RegisterUser registerUser);

  String validateTravellers(TravellerWrapper travellers, FlightOffers flightOffers);
}
