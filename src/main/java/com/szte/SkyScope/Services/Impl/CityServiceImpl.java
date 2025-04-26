package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.City;
import com.szte.SkyScope.Services.CityService;
import com.szte.SkyScope.parser.CityParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CityServiceImpl implements CityService {

    private final RestClient restClient = RestClient.create();
    private final ApplicationConfig applicationConfig;

    @Autowired
    public CityServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
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
        return CityParser.parseJsonToCity(response, "geonames");
    }

    @Override
    public City getCityFromJson(String name) {
        String json = "";
        try (InputStream jsonFile = getClass()
                .getClassLoader()
                .getResourceAsStream("exampleDatas/cities.json")) {
            json = new String(jsonFile.readAllBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return CityParser.parseJsonToCity(json, name);
    }
}