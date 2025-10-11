package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.RegisterUser;
import com.szte.SkyScope.Services.InputValidationService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class InputValidationServiceImpl implements InputValidationService {
  private static final String EMPTY_INPUT_ERROR = " mező kitöltése kötelező! ";
  private static final String INVALID_DATE_FORMAT = "A dátum formátuma nem megfelelő!";
  private static final String DEPARTURE_BEFORE_TODAY =
      "Az indulás napja nem lehet korábbi a mai napnál!";
  private static final String RETURN_BEFORE_DEPARTURE =
      "A visszaút napja nem lehet korábbi az indulás napjánál!";
  private static final String INVALID_CITY_NAME = "Helyeten város név!";
  private static final String INVALID_NUMBER = "Csak pozitív egész számot használjon!";
  private static final String INVALID_TRAVEL_CLASS = "Kérjük a listából válasszon osztályt!";
  private static final String NOT_MATCHING_PASSWORD = "A jelszavak nem egyeznek meg!";
  private static final String TOO_SHORT_PASSWORD =
      "A jelszónak legalább 8 karakter hosszúnak kell lennie!";

  @Override
  public String validateInputFields(FlightSearch flightSearch) {
    return checkEmptySearchFields(flightSearch)
        + checkValidDates(flightSearch)
        + checkNaturalNumber(flightSearch.getNumberOfAdults())
        + checkValidTravelClass(flightSearch.getTravelClass())
        + checkOptionalInputFileds(flightSearch);
  }

  @Override
  public String validateIataCodes(FlightSearch flightSearch) {
    if (flightSearch.getOriginCityIata() == null) {
      return INVALID_CITY_NAME;
    }
    if (flightSearch.getDestinationCityIata() == null) {
      return INVALID_CITY_NAME;
    }
    return "";
  }

  @Override
  public String validatePassword(RegisterUser registerUser) {
    if (!registerUser.getPassword().equals(registerUser.getRePassword())) {
      return NOT_MATCHING_PASSWORD;
    }
    if (registerUser.getPassword().length() < 8) {
      return TOO_SHORT_PASSWORD;
    }
    return "";
  }

  private String checkEmptySearchFields(FlightSearch flightSearch) {
    StringBuilder errorMessage = new StringBuilder();
    if (flightSearch.getOriginCity() == null || flightSearch.getOriginCity().isEmpty()) {
      errorMessage.append("indulási város");
    }
    if (flightSearch.getDestinationCity() == null || flightSearch.getDestinationCity().isEmpty()) {
      errorMessage.append(" célváros");
    }
    if (flightSearch.getDepartureDate() == null || flightSearch.getDepartureDate().isEmpty()) {
      errorMessage.append(" indulás dátuma");
    }
    if (flightSearch.getNumberOfAdults() == null || flightSearch.getNumberOfAdults().isEmpty()) {
      errorMessage.append(" felnőtt utasok száma");
    }
    if (!flightSearch.isOneWay()) {
      if (flightSearch.getReturnDate() == null || flightSearch.getReturnDate().isEmpty()) {
        errorMessage.append(" visszaút dátuma");
      }
    }
    if (!errorMessage.isEmpty()) {
      errorMessage.append(EMPTY_INPUT_ERROR);
    }
    return errorMessage.toString();
  }

  private String checkValidDates(FlightSearch flightSearch) {
    LocalDate departureDate = null;
    LocalDate returnDate = null;
    try {
      departureDate = LocalDate.parse(flightSearch.getDepartureDate());
      if (!flightSearch.isOneWay()) {
        returnDate = LocalDate.parse(flightSearch.getReturnDate());
      }
    } catch (DateTimeParseException exception) {
      return INVALID_DATE_FORMAT;
    }
    if (departureDate.isBefore(LocalDate.now())) {
      return DEPARTURE_BEFORE_TODAY;
    }
    if (!flightSearch.isOneWay()) {
      if (returnDate.isBefore(departureDate)) {
        return RETURN_BEFORE_DEPARTURE;
      }
    }
    return "";
  }

  private String checkOptionalInputFileds(FlightSearch flightSearch) {
    if (flightSearch.getNumberOfChildren() != null
        && !flightSearch.getNumberOfChildren().isEmpty()) {
      return checkNaturalNumber(flightSearch.getNumberOfChildren());
    }
    if (flightSearch.getNumberOfInfants() != null && !flightSearch.getNumberOfInfants().isEmpty()) {
      return checkNaturalNumber(flightSearch.getNumberOfInfants());
    }
    if (flightSearch.getMaxPrice() != null && !flightSearch.getMaxPrice().isEmpty()) {
      return checkNaturalNumber(flightSearch.getMaxPrice());
    }
    return "";
  }

  private String checkNaturalNumber(String numberToCheck) {
    int number;
    try {
      number = Integer.parseInt(numberToCheck);
    } catch (NumberFormatException exception) {
      return INVALID_NUMBER;
    }
    if (number <= 0) {
      return INVALID_NUMBER;
    }
    return "";
  }

  private String checkValidTravelClass(String travelClass) {
    if (Arrays.asList("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST", "ALL")
        .contains(travelClass)) {
      return "";
    }
    return INVALID_TRAVEL_CLASS;
  }
}
