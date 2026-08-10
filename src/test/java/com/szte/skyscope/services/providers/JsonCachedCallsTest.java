package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.AmadeusApiCred;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.JsonReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonCachedCallsTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonCachedCalls jsonCachedCalls;

  @BeforeEach
  void setUp() {
    jsonCachedCalls = new JsonCachedCalls(jsonReaderService);
  }

  @Test
  void getIataCode_Success() {
    String city = "Budapest";
    String token = "dummyToken";
    String dummyJson = "{\"data\":\"dummy iata data\"}";
    String expectedIata = "BUD";

    when(jsonReaderService.readJsonFromResources("exampleDatas/iataCodes.json"))
        .thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser.when(() -> Parser.getIataFromJson(dummyJson, city)).thenReturn(expectedIata);
      String result = jsonCachedCalls.getIataCode(city, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedIata);
      verify(jsonReaderService).readJsonFromResources("exampleDatas/iataCodes.json");
      mockedParser.verify(() -> Parser.getIataFromJson(dummyJson, city));
    }
  }

  @Test
  void getAirportName_Success() {
    String iata = "BUD";
    String token = "dummyToken";
    String dummyJson = "{\"data\":\"dummy airport data\"}";

    String expectedCityName = "Budapest";
    String expectedAirportName = "Liszt Ferenc International Airport";
    String expectedResult = expectedCityName + ", " + expectedAirportName;

    when(jsonReaderService.readJsonFromResources("exampleDatas/airportNames.json"))
        .thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.getCityNameFromAirportAndCityApi(dummyJson, iata))
          .thenReturn(expectedCityName);
      mockedParser
          .when(() -> Parser.getAirportNameFromJson(dummyJson, iata))
          .thenReturn(expectedAirportName);
      String result = jsonCachedCalls.getAirportName(iata, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedResult);

      verify(jsonReaderService).readJsonFromResources("exampleDatas/airportNames.json");
      mockedParser.verify(() -> Parser.getCityNameFromAirportAndCityApi(dummyJson, iata));
      mockedParser.verify(() -> Parser.getAirportNameFromJson(dummyJson, iata));
    }
  }

  @Test
  void getApiCred_ReturnsNewAmadeusApiCred() {
    String clientId = "testClientId";
    String clientSecret = "testClientSecret";
    String authUrl = "http://test.auth.url";
    AmadeusApiCred result = jsonCachedCalls.getApiCred(clientId, clientSecret, authUrl);
    assertThat(result).isNotNull();
    assertThat(result.getClass()).isEqualTo(AmadeusApiCred.class);
  }
}
