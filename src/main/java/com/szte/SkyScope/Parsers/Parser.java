package com.szte.SkyScope.Parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szte.SkyScope.Models.City;
import com.szte.SkyScope.Models.FlightOffers;
import com.szte.SkyScope.Models.Plane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static City parseJsonToCity(String json, String rootElement) {
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

    public static String getIataFromJson(String json, String rootElement) {
        JsonNode root;
        String iata;
        try {
            root = objectMapper.readTree(json);
            iata = root.get(rootElement).get(0).get("iataCode").asText();
        } catch (Exception exception) {
            return null;
        }
        return iata;
    }

    public static List<FlightOffers> parseFlightOffersFromJson(String json) {
        JsonNode root;
        List<FlightOffers> flightOffers = new ArrayList<>();
        try {
            root = objectMapper.readTree(json);
            JsonNode datas = root.get("data");
            datas.forEach(jsonNode -> {
                try {
                    flightOffers.add(objectMapper.treeToValue(jsonNode, FlightOffers.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return flightOffers;
    }

    public static <T> Map<String, T> parseFlightDictionary(String json, String dictionary, TypeReference<Map<String, T>> typeRef) {
        Map<String, T> flightDictionary = null;
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode dictNode = root.path("dictionaries").path(dictionary);
            flightDictionary = objectMapper.convertValue(dictNode, typeRef);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return flightDictionary;
    }
}
