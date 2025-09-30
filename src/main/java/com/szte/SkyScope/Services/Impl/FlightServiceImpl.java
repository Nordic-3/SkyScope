package com.szte.SkyScope.Services.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Models.Location;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.*;
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
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FlightServiceImpl implements FlightService {

  private final RestClient restClient = RestClient.create();
  private final ApplicationConfig applicationConfig;
  private final JsonReaderService jsonReaderService;
  private final SearchStore searchStore;
  private final CityService cityService;
  private final CachedApiCalls cachedApiCalls;

  @Autowired
  public FlightServiceImpl(
      ApplicationConfig applicationConfig,
      JsonReaderService jsonReaderService,
      SearchStore searchStore,
      CityService cityService,
      CachedApiCalls cachedApiCalls) {
    this.applicationConfig = applicationConfig;
    this.jsonReaderService = jsonReaderService;
    this.searchStore = searchStore;
    this.cityService = cityService;
    this.cachedApiCalls = cachedApiCalls;
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
  public CompletableFuture<List<FlightOffers>> getFlightOffers(
      FlightSearch flightSearch, String token, String searchId) {
    if (applicationConfig.getAmadeusCitySearchApi().equals("noApi")
        || !applicationConfig.useApis()) {
      return CompletableFuture.completedFuture(
          getFlightOffersFromLocalJson(flightSearch, searchId));
    }
    UriComponentsBuilder uriBulder =
        UriComponentsBuilder.fromUriString(applicationConfig.getAmadeusFlightOfferSearchApi());
    uriBulder.queryParam("originLocationCode", flightSearch.getOriginCityIata());
    uriBulder.queryParam("destinationLocationCode", flightSearch.getDestinationCityIata());
    uriBulder.queryParam("departureDate", flightSearch.getDepartureDate());
    uriBulder.queryParam("adults", flightSearch.getNumberOfAdults());
    uriBulder.queryParam("currencyCode", "HUF");
    uriBulder.queryParam("max", 100);
    bindOptionalParameters(uriBulder, flightSearch);
    String response =
        restClient
            .get()
            .uri(uriBulder.build(true).toUri())
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/json")
            .retrieve()
            .body(String.class);
    saveDictionaries(response, searchId);
    return CompletableFuture.completedFuture(Parser.parseFlightOffersFromJson(response));
  }

  @Override
  public List<FlightOffers> getFlightOffersFromLocalJson(
      FlightSearch flightSearch, String searchId) {
    String response = jsonReaderService.readJsonFromResources("exampleDatas/FlightOffers.json");
    saveDictionaries(response, searchId);
    return Parser.parseFlightOffersFromJson(response);
  }

  @Override
  public void setAircraftType(List<FlightOffers> flightOffers, Map<String, String> aircrafts) {
    getSegmentStream(flightOffers)
        .forEach(
            segment ->
                segment
                    .getAircraft()
                    .setName(
                        aircrafts
                            .getOrDefault(
                                segment.getAircraft().getCode(), segment.getAircraft().getCode())
                            .toLowerCase()));
  }

  @Override
  public void setCarrierNames(List<FlightOffers> flightOffers, Map<String, String> carriers) {
    getSegmentStream(flightOffers)
        .forEach(
            segment -> {
              segment.setCarrierName(
                  carriers
                      .getOrDefault(segment.getCarrierCode(), segment.getCarrierCode())
                      .toLowerCase());
              segment
                  .getOperating()
                  .setCarrierName(
                      carriers
                          .getOrDefault(
                              segment.getOperating().getCarrierCode(),
                              segment.getOperating().getCarrierCode())
                          .toLowerCase());
            });
  }

  @Override
  public Map<String, String> getAirportNamesByItsIata(
      Map<String, Location> locations, String token) {
    Map<String, String> airportNamesByIataCode = new HashMap<>();
    locations.forEach(
        (key, value) ->
            airportNamesByIataCode.put(key, cachedApiCalls.getAirportNameFromApi(key, token)));
    return airportNamesByIataCode;
  }

  @Override
  public void setAirportNames(List<FlightOffers> flightOffers, Map<String, String> airprots) {
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

  private void setEnglishNameOfTheCities(FlightSearch flightSearch) {
    flightSearch.setOriginCity(cityService.getCityFromApi(flightSearch.getOriginCity()).getName());
    flightSearch.setDestinationCity(
        cityService.getCityFromApi(flightSearch.getDestinationCity()).getName());
  }

  private String normalizeCityNames(String name) {
    return Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
  }

  private void removeAccents(FlightSearch flightSearch) {
    flightSearch.setOriginCity(normalizeCityNames(flightSearch.getOriginCity()));
    flightSearch.setDestinationCity(normalizeCityNames(flightSearch.getDestinationCity()));
  }

  private Stream<FlightOffers.Segment> getSegmentStream(List<FlightOffers> flightOffers) {
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

  private void bindOptionalParameters(UriComponentsBuilder uriBulder, FlightSearch flightSearch) {
    if (isNotNullAndNotEmpty(flightSearch.getReturnDate())) {
      uriBulder.queryParam("returnDate", flightSearch.getReturnDate());
    }
    if (isNotNullAndNotEmpty(flightSearch.getNumberOfChildren())) {
      uriBulder.queryParam("children", flightSearch.getNumberOfChildren());
    }
    if (isNotNullAndNotEmpty(flightSearch.getNumberOfInfants())) {
      uriBulder.queryParam("infants", flightSearch.getNumberOfInfants());
    }
    if (flightSearch.getTravelClass() != null && !flightSearch.getTravelClass().equals("ALL")) {
      uriBulder.queryParam("travelClass", flightSearch.getTravelClass());
    }
  }

  private boolean isNotNullAndNotEmpty(String dataToCheck) {
    return dataToCheck != null && !dataToCheck.isEmpty();
  }
}
