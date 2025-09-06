package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.City;
import com.szte.SkyScope.Services.CityService;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.JsonReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CityServiceImpl implements CityService {

    private final RestClient restClient = RestClient.create();
    private final ApplicationConfig applicationConfig;
    private final JsonReaderService jsonReaderService;

    @Autowired
    public CityServiceImpl(ApplicationConfig applicationConfig, JsonReaderService jsonReaderService) {
        this.applicationConfig = applicationConfig;
        this.jsonReaderService = jsonReaderService;
    }

    @Override
    public City getCityFromApi(String name) {
        if (applicationConfig.getGeoNamesApiKey().equals("noApi")) {
            return getCityFromJson(name);
        }
        String response = restClient
                .get()
                .uri(applicationConfig.getGeoNamesApiKey(), name)
                .retrieve()
                .body(String.class);
        return Parser.parseJsonToCity(response, "geonames");
    }

    @Override
    public City getCityFromJson(String name) {
        return Parser.parseJsonToCity(jsonReaderService.readJsonFromResources("exampleDatas/cities.json"), name);
    }
}