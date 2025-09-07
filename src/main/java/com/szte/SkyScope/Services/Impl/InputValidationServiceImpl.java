package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.InputValidationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Service
public class InputValidationServiceImpl implements InputValidationService {
    private static final String EMPTY_INPUT_ERROR = "A mező kitöltése kötelező!";
    private static final String INVALID_DATE_FORMAT = "A dátum formátuma nem megfelelő!";
    private static final String DEPARTURE_BEFORE_TODAY = "Az indulás napja nem lehet korábbi a mai napnál!";
    private static final String RETURN_BEFORE_DEPARTURE = "A visszaút napja nem lehet korábbi az indulás napjánál!";
    private static final String INVALID_CITY_NAME = "Helyeten város név!";
    private static final String INVALID_NUMBER = "Csak pozitív egész számot használjon!";
    private static final String INVALID_TRAVEL_CLASS = "Kérjük a listából válasszon osztályt!";

    @Override
    public boolean isValidInputDatas(FlightSearch flightSearch, Model model) {
        return !isEmptyInputFields(flightSearch, model)
                && isValidDate(flightSearch, model)
                && isNaturalNumber(flightSearch.getNumberOfAdults(), model)
                && isValidTravelClass(flightSearch.getTravelClass(), model)
                && isValidOptionalInputFileds(flightSearch, model);
    }

    @Override
    public boolean isValidIataCodes(FlightSearch flightSearch, Model model) {
        boolean isValid = true;
        if (flightSearch.getOriginCityIata() == null) {
            model.addAttribute("originError", INVALID_CITY_NAME);
            isValid = false;
        }
        if (flightSearch.getDestinationCityIata() == null) {
            model.addAttribute("destinationError", INVALID_CITY_NAME);
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmptyInputFields(FlightSearch flightSearch, Model model) {
        boolean isEmpty = false;
        if (flightSearch.getOriginCity() == null || flightSearch.getOriginCity().isEmpty()) {
            model.addAttribute("originError", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (flightSearch.getDestinationCity() == null || flightSearch.getDestinationCity().isEmpty()) {
            model.addAttribute("destinationError", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (flightSearch.getDepartureDate() == null || flightSearch.getDepartureDate().isEmpty()) {
            model.addAttribute("errorOutDate", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (flightSearch.getNumberOfAdults() == null || flightSearch.getNumberOfAdults().isEmpty()) {
            model.addAttribute("errorInAdvancedSearch", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (!flightSearch.isOneWay()) {
            if (flightSearch.getReturnDate() == null || flightSearch.getReturnDate().isEmpty()) {
                model.addAttribute("errorReturnDate", EMPTY_INPUT_ERROR);
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    private boolean isValidDate(FlightSearch flightSearch, Model model) {
        LocalDate departureDate = null;
        LocalDate returnDate = null;
        try {
            departureDate = LocalDate.parse(flightSearch.getDepartureDate());
            if (!flightSearch.isOneWay()) {
                returnDate = LocalDate.parse(flightSearch.getReturnDate());
            }
        } catch (DateTimeParseException exception) {
            model.addAttribute("errorOutDate", INVALID_DATE_FORMAT);
            model.addAttribute("errorReturnDate", INVALID_DATE_FORMAT);
            return false;
        }
        if (departureDate.isBefore(LocalDate.now())) {
            model.addAttribute("errorOutDate", DEPARTURE_BEFORE_TODAY);
            return false;
        }
        if (!flightSearch.isOneWay()) {
            if (returnDate.isBefore(departureDate)) {
                model.addAttribute("errorOutDate", RETURN_BEFORE_DEPARTURE);
                return false;
            }
        }
        return true;
    }

    private boolean isValidOptionalInputFileds(FlightSearch flightSearch, Model model) {
        if (flightSearch.getNumberOfChildren() != null && !flightSearch.getNumberOfChildren().isEmpty()) {
            return isNaturalNumber(flightSearch.getNumberOfChildren(), model);
        }
        if (flightSearch.getNumberOfInfants() != null && !flightSearch.getNumberOfInfants().isEmpty()) {
            return isNaturalNumber(flightSearch.getNumberOfInfants(), model);
        }
        if (flightSearch.getMaxPrice() != null && !flightSearch.getMaxPrice().isEmpty()) {
            return isNaturalNumber(flightSearch.getMaxPrice(), model);
        }
        return true;
    }

    private boolean isNaturalNumber(String numberToCheck, Model model) {
        int number;
        try {
            number = Integer.parseInt(numberToCheck);
        } catch (NumberFormatException exception) {
            model.addAttribute("errorInAdvancedSearch", INVALID_NUMBER);
            return false;
        }
        if (number <= 0) {
            model.addAttribute("errorInAdvancedSearch", INVALID_NUMBER);
            return false;
        }
        return true;
    }

    private boolean isValidTravelClass(String travelClass, Model model) {
        if (Arrays.asList("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST", "ALL").contains(travelClass)) {
            return true;
        }
        model.addAttribute("errorInAdvancedSearch", INVALID_TRAVEL_CLASS);
        return false;
    }
}
