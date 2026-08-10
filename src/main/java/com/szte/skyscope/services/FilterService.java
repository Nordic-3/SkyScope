package com.szte.skyscope.services;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.ChosenFilters;
import com.szte.skyscope.models.FilterValue;
import java.util.List;

public interface FilterService {

  List<FlightOfferDTO> filterOffers(List<FlightOfferDTO> flightOffers, ChosenFilters chosenFilters);

  FilterValue getFilterOptions(List<FlightOfferDTO> flightOffers);
}
