package com.szte.skyScope.models;

import com.szte.skyScope.dtos.FlightOfferDTO;
import java.util.List;
import java.util.Map;

public class SearchData {
  private List<FlightOfferDTO> searchResult;
  private FlightSearch flightSearch;
  private Map<String, String> aircraftDictionary;
  private Map<String, Location> locationDictionary;
  private Map<String, String> carrierDictionary;
  private ChosenFilters chosenFilters = new ChosenFilters();
  private List<FlightOfferDTO> originalSearchResult;
  private boolean cheaperOfferAvailable = false;
  private boolean haveToCheckForCheaperOffer = true;
  private FlightSearch cheaperSearch;
  private List<FlightOfferDTO> cheaperSearchResult;
  private String offerId;
  private List<Traveller> travelers;
  private List<FinalPriceResponse.FlightOffer> validatedOffers;

  public List<FinalPriceResponse.FlightOffer> getValidatedOffers() {
    return validatedOffers;
  }

  public void setValidatedOffers(List<FinalPriceResponse.FlightOffer> validatedOffers) {
    this.validatedOffers = validatedOffers;
  }

  public List<Traveller> getTravelers() {
    return travelers;
  }

  public void setTravelers(List<Traveller> travelers) {
    this.travelers = travelers;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public boolean haveToCheckForCheaperOffer() {
    return haveToCheckForCheaperOffer;
  }

  public void setHaveToCheckForCheaperOffer(boolean haveToCheckForCheaperOffer) {
    this.haveToCheckForCheaperOffer = haveToCheckForCheaperOffer;
  }

  public List<FlightOfferDTO> getCheaperSearchResult() {
    return cheaperSearchResult;
  }

  public void setCheaperSearchResult(List<FlightOfferDTO> cheaperSearchResult) {
    this.cheaperSearchResult = cheaperSearchResult;
  }

  public FlightSearch getCheaperSearch() {
    return cheaperSearch;
  }

  public void setCheaperSearch(FlightSearch cheaperSearch) {
    this.cheaperSearch = cheaperSearch;
  }

  public boolean isCheaperOfferAvailable() {
    return cheaperOfferAvailable;
  }

  public void setCheaperOfferAvailable(boolean cheaperOfferAvailable) {
    this.cheaperOfferAvailable = cheaperOfferAvailable;
  }

  public List<FlightOfferDTO> getSearchResult() {
    return searchResult;
  }

  public void setSearchResult(List<FlightOfferDTO> searchResult) {
    this.searchResult = searchResult;
  }

  public FlightSearch getFlightSearch() {
    return flightSearch;
  }

  public void setFlightSearch(FlightSearch flightSearch) {
    this.flightSearch = flightSearch;
  }

  public Map<String, String> getAircraftDictionary() {
    return aircraftDictionary;
  }

  public void setAircraftDictionary(Map<String, String> aircraftDictionary) {
    this.aircraftDictionary = aircraftDictionary;
  }

  public Map<String, Location> getLocationDictionary() {
    return locationDictionary;
  }

  public void setLocationDictionary(Map<String, Location> locationDictionary) {
    this.locationDictionary = locationDictionary;
  }

  public Map<String, String> getCarrierDictionary() {
    return carrierDictionary;
  }

  public void setCarrierDictionary(Map<String, String> carrierDictionary) {
    this.carrierDictionary = carrierDictionary;
  }

  public ChosenFilters getFilterAttribute() {
    return chosenFilters;
  }

  public void setFilterAttribute(ChosenFilters chosenFilters) {
    this.chosenFilters = chosenFilters;
  }

  public List<FlightOfferDTO> getOriginalSearchResult() {
    return originalSearchResult;
  }

  public void setOriginalSearchResult(List<FlightOfferDTO> originalSearchResult) {
    this.originalSearchResult = originalSearchResult;
  }
}
