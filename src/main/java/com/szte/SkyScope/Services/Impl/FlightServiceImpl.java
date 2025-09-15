package com.szte.SkyScope.Services.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.JsonReaderService;
import com.szte.SkyScope.Services.SearchStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FlightServiceImpl implements FlightService {

    private final RestClient restClient = RestClient.create();
    private final ApplicationConfig applicationConfig;
    private final JsonReaderService jsonReaderService;
    private final SearchStore searchStore;

    @Autowired
    public FlightServiceImpl(ApplicationConfig applicationConfig, JsonReaderService jsonReaderService, SearchStore searchStore) {
        this.applicationConfig = applicationConfig;
        this.jsonReaderService = jsonReaderService;
        this.searchStore = searchStore;
    }

    @Override
    @Cacheable("amadeusApiToken")
    public AmadeusApiCred getToken() {
        if (!applicationConfig.useApis() || applicationConfig.getAmadeusClientId().equals("noApi")) {
            return new AmadeusApiCred();
        }
        String body = "grant_type=client_credentials" +
                "&client_id=" + applicationConfig.getAmadeusClientId() +
                "&client_secret=" + applicationConfig.getAmadeusClientSecret();
        return restClient.post()
                .uri(applicationConfig.getAmadeusAuthUrl())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(AmadeusApiCred.class);
    }

    @Override
    public String getIataCode(String city, String token) {
        if (applicationConfig.getAmadeusCitySearchApi().equals("noApi") || !applicationConfig.useApis()) {
            return getIataCodeFromLocalJson(city);
        }
        try {
            return Parser.getIataFromJson(restClient.get()
                    .uri(applicationConfig.getAmadeusCitySearchApi(), city.strip())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class), "data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getIataCodeFromLocalJson(String city) {
        return Parser.getIataFromJson(
                jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"),
                city);
    }

    @Override
    public void setIataCodes(FlightSearch flightSearch, String token) {
        flightSearch.setOriginCityIata(getIataCode(flightSearch.getOriginCity(), token));
        flightSearch.setDestinationCityIata(getIataCode(flightSearch.getDestinationCity(), token));
    }

    @Async
    @Override
    public CompletableFuture<List<FlightOffers>> getFlightOffers(FlightSearch flightSearch, String token) {
        if (applicationConfig.getAmadeusCitySearchApi().equals("noApi") || !applicationConfig.useApis()) {
            return CompletableFuture.completedFuture(getFlightOffersFromLocalJson(flightSearch));
        }
       UriComponentsBuilder uriBulder = UriComponentsBuilder.fromUriString(applicationConfig.getAmadeusFlightOfferSearchApi());
        uriBulder.queryParam("originLocationCode", flightSearch.getOriginCityIata());
        uriBulder.queryParam("destinationLocationCode", flightSearch.getDestinationCityIata());
        uriBulder.queryParam("departureDate", flightSearch.getDepartureDate());
        uriBulder.queryParam("adults", flightSearch.getNumberOfAdults());
        uriBulder.queryParam("currencyCode", "HUF");
        uriBulder.queryParam("max", 250);
        bindOptionalParameters(uriBulder, flightSearch);
        String response = restClient.get()
                .uri(uriBulder.build(true).toUri())
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);
        saveDictionaries(response);
        return CompletableFuture.completedFuture(Parser.parseFlightOffersFromJson(response));
    }

    @Override
    public List<FlightOffers> getFlightOffersFromLocalJson(FlightSearch flightSearch) {
        String response = jsonReaderService.readJsonFromResources("exampleDatas/FlightOffers.json");
        saveDictionaries(response);
        return Parser.parseFlightOffersFromJson(response);
    }

    @Override
    public void setAircraftType(FlightOffers flightOffers) {
        String aircraftCode = flightOffers.getItineraries().getFirst().getSegments().getFirst().getAircraft().getCode();
        flightOffers
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getAircraft()
                .setName(searchStore.getAircraftDictionary().get(aircraftCode).toLowerCase());

    }

    private void saveDictionaries(String json) {
        searchStore.saveAircraftDictionary(Parser.parseFlightDictionary(json, "aircraft", new TypeReference<>() {}));
        searchStore.saveLocationDictionary(Parser.parseFlightDictionary(json, "locations", new TypeReference<>() {}));
        searchStore.saveCarrierDictionary(Parser.parseFlightDictionary(json, "carriers", new TypeReference<>() {}));
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

    @CacheEvict(value = "amadeusApiToken", allEntries = true)
    @Scheduled(fixedRateString = "${amadeus_token_expiry}")
    public void emptyAmadeusApiToken() {}
}
