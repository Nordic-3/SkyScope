package com.szte.skyScope.models;

import com.szte.skyScope.dtos.FlightOfferDTO;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class SearchData {
  @Setter @Getter private List<FlightOfferDTO> searchResult;
  @Setter @Getter private FlightSearch flightSearch;
  @Setter @Getter private Map<String, String> aircraftDictionary;
  @Setter @Getter private Map<String, Location> locationDictionary;
  @Setter @Getter private Map<String, String> carrierDictionary;
  private ChosenFilters chosenFilters = new ChosenFilters();
  @Setter @Getter private List<FlightOfferDTO> originalSearchResult;
  @Setter @Getter private boolean cheaperOfferAvailable = false;
  @Setter private boolean haveToCheckForCheaperOffer = true;
  @Setter @Getter private FlightSearch cheaperSearch;
  @Setter @Getter private List<FlightOfferDTO> cheaperSearchResult;
  @Setter @Getter private String offerId;
  @Setter @Getter private List<Traveller> travelers;
  @Setter @Getter private List<FinalPriceResponse.FlightOffer> validatedOffers;

  public boolean haveToCheckForCheaperOffer() {
    return haveToCheckForCheaperOffer;
  }

  public ChosenFilters getFilterAttribute() {
    return chosenFilters;
  }

  public void setFilterAttribute(ChosenFilters chosenFilters) {
    this.chosenFilters = chosenFilters;
  }
}
