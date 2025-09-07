package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Models.FlightSearch;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.FlightService;
import com.szte.SkyScope.Services.JsonReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FlightServiceImpl implements FlightService {

    private final RestClient restClient = RestClient.create();
    private final ApplicationConfig applicationConfig;
    private final JsonReaderService jsonReaderService;

    @Autowired
    public FlightServiceImpl(ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
        this.applicationConfig = applicationConfig;
        this.jsonReaderService = jsonReaderService;
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
    public String getIataCodeFromApi(String city, String token) {
        if (applicationConfig.getAmadeusCitySearchApi().equals("noApi") || !applicationConfig.useApis()) {
            return getIataCodeFromJson(city);
        }
        try {
            return Parser.getIataFromJson(restClient.get()
                    .uri(applicationConfig.getAmadeusCitySearchApi(), city)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class), "data");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getIataCodeFromJson(String city) {
        return Parser.getIataFromJson(
                jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"),
                city);
    }

    @Override
    public void setIataCodes(FlightSearch flightSearch, String token) {
        flightSearch.setOriginCityIata(getIataCodeFromApi(flightSearch.getOriginCity(), token));
        flightSearch.setDestinationCityIata(getIataCodeFromApi(flightSearch.getDestinationCity(), token));
    }

    @CacheEvict(value = "amadeusApiToken", allEntries = true)
    @Scheduled(fixedRateString = "${amadeus_token_expiry}")
    public void emptyAmadeusApiToken() {}
}
