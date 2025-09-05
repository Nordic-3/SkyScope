package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Services.InputValidationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class InputValidationServiceImpl implements InputValidationService {
    private static final String EMPTY_INPUT_ERROR = "A mező kitöltése kötelező!";
    private static final String INVALID_DATE_FORMAT = "A dátum formátuma nem megfelelő!";
    private static final String DEPARTURE_BEFORE_TODAY = "Az indulás napja nem lehet korábbi a mai napnál!";
    private static final String RETURN_BEFORE_DEPARTURE = "A visszaút napja nem lehet korábbi az indulás napjánál!";

    @Override
    public boolean isValidInputDatas(FlightSearch flightSearch, Model model) {
        return !isEmptyInputFields(flightSearch, model) && isValidDate(flightSearch, model);
    }

    private boolean isEmptyInputFields(FlightSearch flightSearch, Model model) {
        boolean isEmpty = false;
        if (flightSearch.getOriginCity() == null || flightSearch.getOriginCity().isEmpty()) {
            model.addAttribute("emptyOrigin", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (flightSearch.getDestinationCity() == null || flightSearch.getDestinationCity().isEmpty()) {
            model.addAttribute("emptyDestination", EMPTY_INPUT_ERROR);
            isEmpty = true;
        }
        if (flightSearch.getDepartureDate() == null || flightSearch.getDepartureDate().isEmpty()) {
            model.addAttribute("errorOutDate", EMPTY_INPUT_ERROR);
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
}
