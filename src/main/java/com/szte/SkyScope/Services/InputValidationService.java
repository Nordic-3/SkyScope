package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.RegisterUser;

public interface InputValidationService {
  String validateInputFields(FlightSearch flightSearch);

  String validateIataCodes(FlightSearch flightSearch);

  String validatePasswordAndEmail(RegisterUser registerUser);
}
