package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.UserCreationDTO;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.TravellerWrapper;

public interface InputValidationService {
  String validateInputFields(FlightSearch flightSearch);

  String validateIataCodes(FlightSearch flightSearch);

  String validatePasswordAndEmail(UserCreationDTO userCreationDTO);

  String validateTravellers(TravellerWrapper travellers, FlightOffers flightOffers);
}
