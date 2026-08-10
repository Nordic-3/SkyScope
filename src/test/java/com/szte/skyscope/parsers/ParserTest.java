package com.szte.skyscope.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.CheapestDateOffer;
import com.szte.skyscope.models.City;
import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.Plane;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ParserTest {

  private static final String INVALID_JSON = "{ invalid json : ";

  @Test
  void parseJsonToCity_Success() {
    String json = "{\"geonames\": [{\"name\": \"Budapest\", \"countryCode\": \"HU\"}]}";
    String rootElement = "geonames";

    City result = Parser.parseJsonToCity(json, rootElement);

    assertThat(result).isNotNull();
  }

  @Test
  void parseJsonToCity_Exception() {
    City result = Parser.parseJsonToCity(INVALID_JSON, "geonames");

    assertThat(result).isNull();
  }

  @Test
  void parseJsonToMapOfPlanes_Success() {
    String json =
        "{\"states\": [["
            + "\"icao24\", \" W6222 \", \"HU\", 123456, 123456, "
            + "\"Hungary\", \"1621234567\", 12.3, false, 19.04, 47.49"
            + "]]}";

    Map<String, Plane> result = Parser.parseJsonToMapOfPlanes(json);

    assertThat(result).isNotNull().hasSize(1).containsKey("W6222");

    Plane plane = result.get("W6222");
    assertThat(plane).isNotNull();
  }

  @Test
  void parseJsonToMapOfPlanes_Exception() {
    Map<String, Plane> result = Parser.parseJsonToMapOfPlanes(INVALID_JSON);

    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void getIataFromJson_Success() {
    String json = "{\"locations\": [{\"iataCode\": \"BUD\"}]}";

    String result = Parser.getIataFromJson(json, "locations");

    assertThat(result).isEqualTo("BUD");
  }

  @Test
  void getIataFromJson_Exception() {
    String result = Parser.getIataFromJson(INVALID_JSON, "locations");

    assertThat(result).isNull();
  }

  @Test
  void parseFlightOffersFromJson_Success() throws Exception {
    String json = "{\"data\": [{}]}";

    ObjectMapper originalMapper = Parser.objectMapper;

    try {
      ObjectMapper mockMapper = mock(ObjectMapper.class);

      Parser.objectMapper = mockMapper;

      JsonNode rootNode = originalMapper.readTree(json);
      when(mockMapper.readTree(json)).thenReturn(rootNode);

      FlightOfferDTO mockDto = mock(FlightOfferDTO.class);
      when(mockMapper.treeToValue(any(JsonNode.class), eq(FlightOfferDTO.class)))
          .thenReturn(mockDto);

      List<FlightOfferDTO> result = Parser.parseFlightOffersFromJson(json);

      assertThat(result).isNotNull().hasSize(1);
      assertThat(result.getFirst()).isEqualTo(mockDto);

    } finally {
      Parser.objectMapper = originalMapper;
    }
  }

  @Test
  void parseFlightOffersFromJson_Exception() {
    List<FlightOfferDTO> result = Parser.parseFlightOffersFromJson(INVALID_JSON);

    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void parseFlightDictionary_Success() {
    String json =
        "{\"dictionaries\": {\"locations\": {\"BUD\": \"Budapest\", \"PAR\": \"Paris\"}}}";
    TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};

    Map<String, String> result = Parser.parseFlightDictionary(json, "locations", typeRef);

    assertThat(result).isNotNull().hasSize(2).containsEntry("BUD", "Budapest");
  }

  @Test
  void parseFlightDictionary_Exception() {
    TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};

    Map<String, String> result = Parser.parseFlightDictionary(INVALID_JSON, "locations", typeRef);

    assertThat(result).isNull();
  }

  @Test
  void getAirportNameFromJson_Success() {
    String json = "{\"data\": [{\"name\": \"Liszt Ferenc\"}]}";

    String result = Parser.getAirportNameFromJson(json, "data");

    assertThat(result).isEqualTo("Liszt Ferenc");
  }

  @Test
  void getAirportNameFromJson_Exception() {
    String result = Parser.getAirportNameFromJson(INVALID_JSON, "data");

    assertThat(result).isEmpty();
  }

  @Test
  void getCityNameFromAirportAndCityApi_Success() {
    String json = "{\"data\": [{\"address\": {\"cityName\": \"Budapest\"}}]}";

    String result = Parser.getCityNameFromAirportAndCityApi(json, "data");

    assertThat(result).isEqualTo("Budapest");
  }

  @Test
  void getCityNameFromAirportAndCityApi_Exception() {
    String result = Parser.getCityNameFromAirportAndCityApi(INVALID_JSON, "data");

    assertThat(result).isEmpty();
  }

  @Test
  void parseCheapestFlightApi_Success() {
    String json =
        "{\"data\": [{"
            + "\"origin\": \"BUD\", "
            + "\"destination\": \"PAR\", "
            + "\"departureDate\": \"2026-08-10\", "
            + "\"returnDate\": \"2026-08-15\", "
            + "\"price\": {\"total\": 15000}, "
            + "\"links\": {\"flightOffers\": \"https://api.test/offers\"}"
            + "}]}";

    List<CheapestDateOffer> result = Parser.parseCheapestFlightApi(json);

    assertThat(result).isNotNull().hasSize(1);
  }

  @Test
  void parseCheapestFlightApi_Exception() {
    List<CheapestDateOffer> result = Parser.parseCheapestFlightApi(INVALID_JSON);

    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void parseFlightPriceRequest_Success() {
    String json = "{\"data\": {\"type\": \"flight-offers-pricing\"}}";

    FinalPriceResponse result = Parser.parseFlightPriceRequest(json);

    assertThat(result).isNotNull();
  }

  @Test
  void parseFlightPriceRequest_Exception() {
    FinalPriceResponse result = Parser.parseFlightPriceRequest(INVALID_JSON);

    assertThat(result).isNull();
  }

  @Test
  void getIcaoCodesFromJson_Success() {
    String json =
        "{\"data\": ["
            + "{\"iataCode\": \"BUD\", \"icaoCode\": \"LHBP\"}, "
            + "{\"iataCode\": \"VIE\", \"icaoCode\": \"LOWW\"}"
            + "]}";

    Map<String, String> result = Parser.getIcaoCodesFromJson(json);

    assertThat(result)
        .isNotNull()
        .hasSize(2)
        .containsEntry("BUD", "LHBP")
        .containsEntry("VIE", "LOWW");
  }

  @Test
  void getIcaoCodesFromJson_Exception() {
    Map<String, String> result = Parser.getIcaoCodesFromJson(INVALID_JSON);

    assertThat(result).isNotNull().isEmpty();
  }
}
