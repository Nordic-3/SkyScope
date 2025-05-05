package com.szte.SkyScope.Parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szte.SkyScope.Models.City;

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
}