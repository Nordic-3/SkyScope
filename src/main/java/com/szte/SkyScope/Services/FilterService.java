package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.ChosenFilters;
import com.szte.SkyScope.Models.FilterValue;
import com.szte.SkyScope.Models.FlightOffers;
import java.util.List;

public interface FilterService {

  List<FlightOffers> filterOffers(List<FlightOffers> flightOffers, ChosenFilters chosenFilters);

  FilterValue getFilterOptions(List<FlightOffers> flightOffers);
}
