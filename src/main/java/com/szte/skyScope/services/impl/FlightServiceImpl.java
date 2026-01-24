package com.szte.skyScope.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.enums.TravellerTypes;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.models.Location;
import com.szte.skyScope.models.Plane;
import com.szte.skyScope.parsers.Parser;
import com.szte.skyScope.services.*;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImpl implements FlightService {

  private final SearchStore searchStore;
  private final CityService cityService;
  private final CachedApiCalls cachedApiCalls;
  private final FlightServiceProvider flightServiceProvider;

  @Autowired
  public FlightServiceImpl(
      SearchStore searchStore,
      CityService cityService,
      CachedApiCalls cachedApiCalls,
      FlightServiceProvider flightServiceProvider) {
    this.searchStore = searchStore;
    this.cityService = cityService;
    this.cachedApiCalls = cachedApiCalls;
    this.flightServiceProvider = flightServiceProvider;
  }

  @Override
  public String getToken() {
    return cachedApiCalls.getAmadeusApiCred().getAccess_token();
  }

  @Override
  public void setIataCodes(FlightSearch flightSearch, String token) {
    setEnglishNameOfTheCities(flightSearch);
    removeAccents(flightSearch);
    flightSearch.setOriginCityIata(cachedApiCalls.getIataCode(flightSearch.getOriginCity(), token));
    flightSearch.setDestinationCityIata(
        cachedApiCalls.getIataCode(flightSearch.getDestinationCity(), token));
  }

  @Async
  @Override
  public CompletableFuture<List<FlightOfferDTO>> getFlightOffers(
      FlightSearch flightSearch, String token, String searchId) {
    String response = flightServiceProvider.getFlightOffers(flightSearch, token);
    saveDictionaries(response, searchId);
    return CompletableFuture.completedFuture(Parser.parseFlightOffersFromJson(response));
  }

  @Override
  public void setAircraftType(List<FlightOfferDTO> flightOffers, Map<String, String> aircrafts) {
    getSegmentStream(flightOffers)
        .forEach(
            segment ->
                segment
                    .getAircraft()
                    .setName(
                        WordUtils.capitalizeFully(
                            aircrafts.getOrDefault(
                                segment.getAircraft().getCode(),
                                segment.getAircraft().getCode()))));
  }

  @Override
  public void setCarrierNames(List<FlightOfferDTO> flightOffers, Map<String, String> carriers) {
    getSegmentStream(flightOffers)
        .forEach(
            segment -> {
              segment.setCarrierName(
                  WordUtils.capitalizeFully(
                      carriers.getOrDefault(segment.getCarrierCode(), segment.getCarrierCode())));
              segment
                  .getOperating()
                  .setCarrierName(
                      WordUtils.capitalizeFully(
                          carriers.getOrDefault(
                              segment.getOperating().getCarrierCode(),
                              segment.getOperating().getCarrierCode())));
            });
  }

  @Override
  public Map<String, String> getAirportNamesByItsIata(
      Map<String, Location> locations, String token) {
    Map<String, String> airportNamesByIataCode = new HashMap<>();
    locations.forEach(
        (key, value) -> airportNamesByIataCode.put(key, cachedApiCalls.getAirportName(key, token)));
    return airportNamesByIataCode;
  }

  @Override
  public void setAirportNames(List<FlightOfferDTO> flightOffers, Map<String, String> airprots) {
    getSegmentStream(flightOffers)
        .forEach(
            segment -> {
              segment
                  .getDeparture()
                  .setAirportName(
                      WordUtils.capitalizeFully(
                          airprots.getOrDefault(
                              segment.getDeparture().getIataCode(),
                              segment.getDeparture().getIataCode())));
              segment
                  .getArrival()
                  .setAirportName(
                      WordUtils.capitalizeFully(
                          airprots.getOrDefault(
                              segment.getArrival().getIataCode(),
                              segment.getArrival().getIataCode())));
            });
  }

  @Override
  public void setFlightOffersAttributes(
      List<FlightOfferDTO> result, String searchId, String token) {
    setAircraftType(result, searchStore.getSearchDatas(searchId).getAircraftDictionary());
    setCarrierNames(result, searchStore.getSearchDatas(searchId).getCarrierDictionary());
    setAirportNames(
        result,
        getAirportNamesByItsIata(
            searchStore.getSearchDatas(searchId).getLocationDictionary(), token));
    setHungarianNameOfTravellerTypes(result);
  }

  @Override
  public Map<String, String> getIcaoCodes(Map<String, String> carrierDictionary, String token) {
    return flightServiceProvider.getIcaoCodes(carrierDictionary, token);
  }

  @Override
  public void setCallsigns(List<FlightOfferDTO> result, Map<String, String> icaoCodes) {
    getSegmentStream(result)
        .forEach(
            segment ->
                segment.setCallSign(
                    icaoCodes.get(segment.getOperating().getCarrierCode()) + segment.getNumber()));
  }

  @Override
  public void setIsCurrentlyFlying(List<FlightOfferDTO> result, Map<String, Plane> planePositions) {
    if (planePositions == null) {
      return;
    }
    getSegmentStream(result)
        .forEach(
            segment ->
                segment.setCurrentlyFlying(planePositions.get(segment.getCallSign()) != null));
  }

  private void setEnglishNameOfTheCities(FlightSearch flightSearch) {
    flightSearch.setOriginCity(cityService.getCity(flightSearch.getOriginCity()).getName());
    flightSearch.setDestinationCity(
        cityService.getCity(flightSearch.getDestinationCity()).getName());
  }

  private String normalizeCityNames(String name) {
    return Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
  }

  private void removeAccents(FlightSearch flightSearch) {
    flightSearch.setOriginCity(normalizeCityNames(flightSearch.getOriginCity()));
    flightSearch.setDestinationCity(normalizeCityNames(flightSearch.getDestinationCity()));
  }

  private Stream<FlightOfferDTO.Segment> getSegmentStream(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .flatMap(offer -> offer.getItineraries().stream())
        .flatMap(itinerary -> itinerary.getSegments().stream());
  }

  private void saveDictionaries(String json, String searchId) {
    searchStore
        .getSearchDatas(searchId)
        .setAircraftDictionary(
            Parser.parseFlightDictionary(json, "aircraft", new TypeReference<>() {}));
    searchStore
        .getSearchDatas(searchId)
        .setLocationDictionary(
            Parser.parseFlightDictionary(json, "locations", new TypeReference<>() {}));
    searchStore
        .getSearchDatas(searchId)
        .setCarrierDictionary(
            Parser.parseFlightDictionary(json, "carriers", new TypeReference<>() {}));
  }

  private void setHungarianNameOfTravellerTypes(List<FlightOfferDTO> flightOffers) {
    flightOffers.stream()
        .flatMap(flightOffer -> flightOffer.getTravelerPricings().stream())
        .forEach(
            travelerPricing ->
                travelerPricing.setTraveller(
                    TravellerTypes.getValueFromType(travelerPricing.getTravelerType())));
  }
}
