package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.AmadeusApiCred;
import com.szte.skyscope.parsers.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes"})
class ApiCachedCallsTest {

  @Mock private ApplicationConfig applicationConfig;

  @Mock private RestClient restClientMock;

  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;

  @Mock private RestClient.RequestBodySpec requestBodySpec;

  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiCachedCalls apiCachedCalls;

  @BeforeEach
  void setUp() {
    apiCachedCalls = new ApiCachedCalls(applicationConfig);
    ReflectionTestUtils.setField(apiCachedCalls, "restClient", restClientMock);
  }

  @Test
  void getIataCode_Success() {
    String city = " Budapest ";
    String token = "testToken";
    String url = "http://api.test/search";
    String dummyResponse = "{\"dummy\": \"response\"}";
    String expectedIata = "BUD";

    when(applicationConfig.getAmadeusCitySearchApi()).thenReturn(url);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(url, "Budapest", "CITY");
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.getIataFromJson(dummyResponse, "data"))
          .thenReturn(expectedIata);
      String result = apiCachedCalls.getIataCode(city, token);
      assertThat(result).isEqualTo(expectedIata);
      mockedParser.verify(() -> Parser.getIataFromJson(dummyResponse, "data"));
    }
  }

  @Test
  void getIataCode_ThrowsException_ReturnsNull() {
    String city = "Budapest";
    String token = "testToken";
    String url = "http://api.test/search";

    when(applicationConfig.getAmadeusCitySearchApi()).thenReturn(url);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(url, city, "CITY");
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    when(responseSpec.body(String.class)).thenThrow(new RuntimeException("API error"));

    String result = apiCachedCalls.getIataCode(city, token);

    assertThat(result).isNull();
  }

  @Test
  void getAirportName_Success() {
    String iata = "BUD";
    String token = "testToken";
    String url = "http://api.test/search";
    String dummyResponse = "{\"dummy\": \"response\"}";

    when(applicationConfig.getAmadeusCitySearchApi()).thenReturn(url);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(url, iata, "AIRPORT");
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.getCityNameFromAirportAndCityApi(dummyResponse, "data"))
          .thenReturn("Budapest");
      mockedParser
          .when(() -> Parser.getAirportNameFromJson(dummyResponse, "data"))
          .thenReturn("Ferenc Liszt");

      String result = apiCachedCalls.getAirportName(iata, token);

      assertThat(result).isEqualTo("Budapest, Ferenc Liszt");

      mockedParser.verify(() -> Parser.getCityNameFromAirportAndCityApi(dummyResponse, "data"));
      mockedParser.verify(() -> Parser.getAirportNameFromJson(dummyResponse, "data"));
    }
  }

  @Test
  void getApiCred_Success() {
    String clientId = "client123";
    String clientSecret = "secret456";
    String authUrl = "http://auth.test/token";
    String expectedBody =
        "grant_type=client_credentials&client_id=client123&client_secret=secret456";

    AmadeusApiCred expectedCred = new AmadeusApiCred();

    doReturn(requestBodyUriSpec).when(restClientMock).post();
    doReturn(requestBodySpec).when(requestBodyUriSpec).uri(authUrl);
    doReturn(requestBodySpec)
        .when(requestBodySpec)
        .header("Content-Type", "application/x-www-form-urlencoded");
    doReturn(requestBodySpec).when(requestBodySpec).body(expectedBody);
    doReturn(responseSpec).when(requestBodySpec).retrieve();
    doReturn(expectedCred).when(responseSpec).body(AmadeusApiCred.class);

    AmadeusApiCred result = apiCachedCalls.getApiCred(clientId, clientSecret, authUrl);

    assertThat(result).isEqualTo(expectedCred);
  }
}
