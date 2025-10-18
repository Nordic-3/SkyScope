package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.DTOs.UserCreationDTO;
import com.szte.SkyScope.Models.*;
import com.szte.SkyScope.Services.InputValidationService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
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
  private static final String ALL_FIELD_IS_COMPULSORY = "Minden mező kitöltése kötelező!";

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
  public String validatePasswordAndEmail(UserCreationDTO userCreationDTO) {
    if (isNullOrEmpty(userCreationDTO.email())) {
      return "Email cím" + EMPTY_INPUT_ERROR;
    }
    if (isNullOrEmpty(userCreationDTO.password()) || isNullOrEmpty(userCreationDTO.rePassword())) {
      return "Jelszó" + EMPTY_INPUT_ERROR;
    }
    if (!userCreationDTO.password().equals(userCreationDTO.rePassword())) {
      return NOT_MATCHING_PASSWORD;
    }
    if (userCreationDTO.rePassword().length() < 8) {
      return TOO_SHORT_PASSWORD;
    }
    return "";
  }

  @Override
  public String validateTravellers(TravellerWrapper travellers, FlightOffers flightOffers) {
    return allFieldIsCompulsory(travellers)
        + validEmailAddress(travellers)
        + validateCountryCodes(travellers)
        + validPassportAtTravelDate(travellers, flightOffers)
        + passportExpireBeforeIssueDate(travellers)
        + validDates(travellers);
  }

  @Override
  public String validateCardDetails(BankCard card) {
    if (isNullOrEmpty(card.getCardNumber())
        || isNullOrEmpty(card.getCvv())
        || isNullOrEmpty(card.getExpiration())
        || isNullOrEmpty(card.getName())) {
      return ALL_FIELD_IS_COMPULSORY;
    }
    return "";
  }

  private String checkEmptySearchFields(FlightSearch flightSearch) {
    StringBuilder errorMessage = new StringBuilder();
    if (isNullOrEmpty(flightSearch.getOriginCity())) {
      errorMessage.append("indulási város");
    }
    if (isNullOrEmpty(flightSearch.getDestinationCity())) {
      errorMessage.append(" célváros");
    }
    if (isNullOrEmpty(flightSearch.getDepartureDate())) {
      errorMessage.append(" indulás dátuma");
    }
    if (isNullOrEmpty(flightSearch.getNumberOfAdults())) {
      errorMessage.append(" felnőtt utasok száma");
    }
    if (!flightSearch.isOneWay()) {
      if (isNullOrEmpty(flightSearch.getReturnDate())) {
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

  private boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }

  private String allFieldIsCompulsory(TravellerWrapper travellers) {
    if (travellers.getTravellers().stream()
        .anyMatch(
            traveller ->
                isNullOrEmpty(traveller.getGender())
                    || isNullOrEmpty(traveller.getDateOfBirth())
                    || isNullOrEmpty(traveller.getName().getFirstName())
                    || isNullOrEmpty(traveller.getName().getLastName())
                    || isNullOrEmptyContacts(traveller.getContact())
                    || isNullOrEmptyDocuments(traveller.getDocuments()))) {
      return ALL_FIELD_IS_COMPULSORY;
    }
    return "";
  }

  private boolean isNullOrEmptyContacts(Traveller.Contact contact) {
    return isNullOrEmpty(contact.getEmailAddress())
        || contact.getPhones().stream()
            .anyMatch(
                phone ->
                    isNullOrEmpty(phone.getCountryCallingCode())
                        || isNullOrEmpty(phone.getNumber()));
  }

  private boolean isNullOrEmptyDocuments(List<Passport> passports) {
    return passports.stream()
        .anyMatch(
            passport ->
                isNullOrEmpty(passport.getIssuanceCountry())
                    || isNullOrEmpty(passport.getNumber())
                    || isNullOrEmpty(passport.getDocumentType())
                    || isNullOrEmpty(passport.getBirthPlace())
                    || isNullOrEmpty(passport.getIssuanceDate())
                    || isNullOrEmpty(passport.getNationality())
                    || isNullOrEmpty(passport.getExpiryDate())
                    || isNullOrEmpty(passport.getValidityCountry()));
  }

  private String validEmailAddress(TravellerWrapper travellers) {
    if (travellers.getTravellers().stream()
        .noneMatch(
            traveller ->
                traveller
                    .getContact()
                    .getEmailAddress()
                    .matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,}$"))) {
      return " Az email formátuma nem megfelelő!";
    }
    return "";
  }

  private String validateCountryCodes(TravellerWrapper travellers) {
    if (travellers.getTravellers().stream()
        .flatMap(traveller -> traveller.getDocuments().stream())
        .anyMatch(
            passport ->
                passport.getIssuanceCountry().length() != 2
                    || passport.getNationality().length() != 2)) {
      return " Az állampolgárság és kiállító ország mezőbe két betűs rövidítést használjon!";
    }
    return "";
  }

  private String validPassportAtTravelDate(TravellerWrapper travellers, FlightOffers flightOffers) {
    try {
      LocalDate lastFlight =
          LocalDate.parse(
              flightOffers
                  .getItineraries()
                  .getLast()
                  .getSegments()
                  .getLast()
                  .getArrival()
                  .getAt()
                  .split("T")[0]);
      if (travellers.getTravellers().stream()
          .flatMap(traveller -> traveller.getDocuments().stream())
          .anyMatch(passport -> LocalDate.parse(passport.getExpiryDate()).isBefore(lastFlight))) {
        return " Az úti okmány nem érvényes az utazás idején!";
      }
    } catch (DateTimeParseException exception) {
      return " Nem érvényes dátum formátum!";
    }
    return "";
  }

  private String validDates(TravellerWrapper travellers) {
    if (travellers.getTravellers().stream()
        .anyMatch(
            traveller -> {
              try {
                LocalDate.parse(traveller.getDateOfBirth());
                LocalDate.parse(traveller.getDocuments().getFirst().getIssuanceDate());
                LocalDate.parse(traveller.getDocuments().getFirst().getExpiryDate());
                return false;
              } catch (DateTimeParseException exception) {
                return true;
              }
            })) {
      return " Nem érvényes dátum formátum!";
    }
    return "";
  }

  private String passportExpireBeforeIssueDate(TravellerWrapper travellers) {
    if (travellers.getTravellers().stream()
        .flatMap(t -> t.getDocuments().stream())
        .anyMatch(
            passport -> {
              try {
                return LocalDate.parse(passport.getExpiryDate())
                    .isBefore(LocalDate.parse(passport.getIssuanceDate()));
              } catch (DateTimeParseException exception) {
                return true;
              }
            })) {
      return " Az úti okmány lejárati dátuma hamarabb van, mint a kiállítása!";
    }
    return "";
  }
}
