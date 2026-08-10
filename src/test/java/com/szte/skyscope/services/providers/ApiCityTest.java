package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.City;
import com.szte.skyscope.parsers.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes"})
class ApiCityTest {

  @Mock private ApplicationConfig applicationConfig;

  @Mock private RestClient restClientMock;
  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiCity apiCity;

  @BeforeEach
  void setUp() {
    apiCity = new ApiCity(applicationConfig);
  }

  @Test
  void getCity_Success() {
    String cityName = "Budapest";
    String baseUrl = "http://api.geonames.test/search";
    String dummyResponse = "{\"geonames\":[{\"name\":\"Budapest\"}]}";

    when(applicationConfig.getGeoNamesApiKey()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(baseUrl, cityName);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    City expectedCity = new City();
    expectedCity.setName(cityName);

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class);
        MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {

      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      mockedParser
          .when(() -> Parser.parseJsonToCity(dummyResponse, "geonames"))
          .thenReturn(expectedCity);

      City result = apiCity.getCity(cityName);

      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedCity);

      mockedParser.verify(() -> Parser.parseJsonToCity(dummyResponse, "geonames"));
    }
  }

  @Test
  void getCity_ApiThrowsException_ThrowsException() {
    String cityName = "InvalidCity";
    String baseUrl = "http://api.geonames.test/search";

    when(applicationConfig.getGeoNamesApiKey()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(baseUrl, cityName);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();

    when(responseSpec.body(String.class)).thenThrow(new RuntimeException("API Connection Failed"));

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      RuntimeException exception =
          assertThrows(RuntimeException.class, () -> apiCity.getCity(cityName));
      assertThat(exception.getMessage()).isEqualTo("API Connection Failed");
    }
  }
}
