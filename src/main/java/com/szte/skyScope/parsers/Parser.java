package com.szte.skyScope.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
  public static ObjectMapper objectMapper = new ObjectMapper();
  private static final Logger logger = Logger.getLogger(Parser.class.getName());

  public static City parseJsonToCity(String json, String rootElement) {
    JsonNode root;
    try {
      root = objectMapper.readTree(json);
      JsonNode cityDetails = root.get(rootElement).get(0);
      return objectMapper.treeToValue(cityDetails, City.class);
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
  }

  public static Map<String, Plane> parseJsonToMapOfPlanes(String json) {
    Map<String, Plane> planes = new HashMap<>();
    JsonNode root;
    try {
      root = objectMapper.readTree(json);
      root.get("states")
          .forEach(
              onePlaneDetails ->
                  planes.put(
                      onePlaneDetails.get(1).asText().strip(),
                      new Plane(
                          onePlaneDetails.get(1).asText().strip(),
                          onePlaneDetails.get(5).asText().strip(),
                          onePlaneDetails.get(6).asText().strip(),
                          onePlaneDetails.get(9).asDouble(),
                          onePlaneDetails.get(10).asDouble())));
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
    return planes;
  }

  public static String getIataFromJson(String json, String rootElement) {
    JsonNode root;
    String iata;
    try {
      root = objectMapper.readTree(json);
      iata = root.get(rootElement).get(0).get("iataCode").asText();
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
    return iata;
  }

  public static List<FlightOfferDTO> parseFlightOffersFromJson(String json) {
    JsonNode root;
    List<FlightOfferDTO> flightOffers = new ArrayList<>();
    try {
      root = objectMapper.readTree(json);
      JsonNode datas = root.get("data");
      datas.forEach(
          jsonNode -> {
            try {
              flightOffers.add(objectMapper.treeToValue(jsonNode, FlightOfferDTO.class));
            } catch (JsonProcessingException exception) {
              logger.log(Level.SEVERE, exception.getMessage(), exception);
            }
          });
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
    return flightOffers;
  }

  public static <T> Map<String, T> parseFlightDictionary(
      String json, String dictionary, TypeReference<Map<String, T>> typeRef) {
    Map<String, T> flightDictionary = null;
    try {
      JsonNode root = objectMapper.readTree(json);
      JsonNode dictNode = root.path("dictionaries").path(dictionary);
      flightDictionary = objectMapper.convertValue(dictNode, typeRef);
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return flightDictionary;
  }

  public static String getAirportNameFromJson(String json, String root) {
    try {
      return objectMapper.readTree(json).get(root).get(0).get("name").asText();

    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return "";
  }

  public static String getCityNameFromAirportAndCityApi(String json, String root) {
    try {
      return objectMapper.readTree(json).get(root).get(0).get("address").get("cityName").asText();
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return "";
  }

  public static List<CheapestDateOffer> parseCheapestFlightApi(String json) {
    List<CheapestDateOffer> cheapestDateOffers = new ArrayList<>();
    try {
      objectMapper
          .readTree(json)
          .get("data")
          .forEach(
              data ->
                  cheapestDateOffers.add(
                      new CheapestDateOffer(
                          data.get("origin").asText(),
                          data.get("destination").asText(),
                          data.get("departureDate").asText(),
                          data.get("returnDate").asText(),
                          data.get("price").get("total").asInt(),
                          data.get("links").get("flightOffers").asText())));
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return cheapestDateOffers;
  }

  public static FinalPriceResponse parseFlightPriceRequest(String json) {
    JsonNode root;
    try {
      root = objectMapper.readTree(json);
      return objectMapper.treeToValue(root, FinalPriceResponse.class);
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return null;
    }
  }

  public static Map<String, String> getIcaoCodesFromJson(String response) {
    Map<String, String> icaoCodes = new HashMap<>();
    try {
      JsonNode root = objectMapper.readTree(response);
      root.get("data")
          .forEach(
              jsonNode ->
                  icaoCodes.put(
                      jsonNode.get("iataCode").asText(), jsonNode.get("icaoCode").asText()));
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return icaoCodes;
  }
}
