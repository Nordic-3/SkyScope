package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.FlightOfferDTO;
import com.szte.SkyScope.Models.ChosenFilters;
import com.szte.SkyScope.Models.FilterValue;
import java.util.List;

public interface FilterService {

  List<FlightOfferDTO> filterOffers(List<FlightOfferDTO> flightOffers, ChosenFilters chosenFilters);

  FilterValue getFilterOptions(List<FlightOfferDTO> flightOffers);
}
