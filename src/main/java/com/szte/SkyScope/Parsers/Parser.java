package com.szte.SkyScope.Parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szte.SkyScope.Models.City;
import com.szte.SkyScope.Models.Plane;

import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static City parseJsonToCity(String json, String rootElement) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
            JsonNode cityDetails = root.get(rootElement).get(0);
            return objectMapper.treeToValue(cityDetails, City.class);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Map<String, Plane> parseJsonToMapOfPlanes(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Plane> planes = new HashMap<>();
        JsonNode root;
        try {
            root = objectMapper.readTree(json);

        } catch (Exception exception) {
            return null;
        }
        root.get("states").forEach(onePlaneDetails -> planes.put(
                onePlaneDetails.get(1).asText().strip(),
                new Plane(
                        onePlaneDetails.get(1).asText().strip(),
                        onePlaneDetails.get(5).asText().strip(),
                        onePlaneDetails.get(6).asText().strip(),
                        onePlaneDetails.get(9).asDouble(),
                        onePlaneDetails.get(10).asDouble())));
        return planes;
    }
}