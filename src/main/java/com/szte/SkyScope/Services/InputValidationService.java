package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FlightSearch;
import org.springframework.ui.Model;

public interface InputValidationService {
    boolean isValidInputDatas(FlightSearch flightSearch, Model model);
}
