package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.AmadeusApiCred;
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
        System.out.println(token);
        return Parser.getIataFromJson(restClient.get()
                .uri(applicationConfig.getAmadeusCitySearchApi(), city)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class), "data");
    }

    @Override
    public String getIataCodeFromJson(String city) {
        return Parser.getIataFromJson(
                jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"),
                city);
    }

    @CacheEvict(value = "amadeusApiToken", allEntries = true)
    @Scheduled(fixedRateString = "${amadeus_token_expiry}")
    public void emptyAmadeusApiToken() {}
}
