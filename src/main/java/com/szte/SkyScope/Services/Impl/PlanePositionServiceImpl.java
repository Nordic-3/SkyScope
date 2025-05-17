package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Models.Plane;
import com.szte.SkyScope.Parsers.Parser;
import com.szte.SkyScope.Services.PlanePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class PlanePositionServiceImpl implements PlanePositionService {
    private final ApplicationConfig applicationConfig;
    private final RestClient restClient = RestClient.create();


    @Autowired
    public PlanePositionServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public Plane getPlanePositionFromJson(String callsign) {
        String json = "";
        try (InputStream jsonFile = getClass()
                .getClassLoader()
                .getResourceAsStream("exampleDatas/planePositions.json")) {
            json = new String(jsonFile.readAllBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return getPlaneFromDataSource(callsign, json);
    }

    @Override
    public Plane getPlanePositionFromApi(String callsign) {
        String response = restClient
                .get()
                .uri(applicationConfig.getOpenskyApiKey())
                .retrieve()
                .body(String.class);
        return getPlaneFromDataSource(callsign, response);
    }

    private Plane getPlaneFromDataSource(String callsign, String dataSource) {
        Map<String, Plane> planes = Parser.parseJsonToMapOfPlanes(dataSource);
        return planes != null ? planes.get(callsign.strip().toUpperCase()) : new Plane();
    }
}
