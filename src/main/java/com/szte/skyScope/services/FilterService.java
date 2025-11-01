package com.szte.skyScope.services;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.ChosenFilters;
import com.szte.skyScope.models.FilterValue;
import java.util.List;

public interface FilterService {

  List<FlightOfferDTO> filterOffers(List<FlightOfferDTO> flightOffers, ChosenFilters chosenFilters);

  FilterValue getFilterOptions(List<FlightOfferDTO> flightOffers);
}
