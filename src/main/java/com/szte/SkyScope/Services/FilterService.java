package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.FilterAttribute;
import com.szte.SkyScope.Models.FlightOffers;
import java.util.List;

public interface FilterService {
  List<String> getAirlineNames(List<FlightOffers> flightOffers);

  List<String> getTransferNumbers(List<FlightOffers> flightOffers);

  List<String> getTransferDurations(List<FlightOffers> flightOffers);

  List<String> getAirplanes(List<FlightOffers> flightOffers);

  List<FlightOffers> filterOffers(List<FlightOffers> flightOffers, FilterAttribute filterAttribute);

  String getMaxPrice(List<FlightOffers> flightOffers);
}
