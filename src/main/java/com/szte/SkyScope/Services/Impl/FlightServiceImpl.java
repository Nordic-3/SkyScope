package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Services.FlightService;
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

    @Autowired
    public FlightServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
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

    @CacheEvict(value = "amadeusApiToken", allEntries = true)
    @Scheduled(fixedRateString = "${amadeus_token_expiry}")
    public void emptyAmadeusApiToken() {}
}
