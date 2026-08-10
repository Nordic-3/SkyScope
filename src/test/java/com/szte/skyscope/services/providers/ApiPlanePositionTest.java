package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes"})
class ApiPlanePositionTest {

  @Mock private ApplicationConfig applicationConfig;
  @Mock private RestClient restClientMock;
  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiPlanePosition apiPlanePosition;

  @BeforeEach
  void setUp() {
    apiPlanePosition = new ApiPlanePosition(applicationConfig);
  }

  @Test
  void getAllPLanePositions_Success() {
    String baseUrl = "http://api.opensky.test/states/all";
    String dummyResponse = "{\"time\":1690000000,\"states\":[[\"icao24\",\"callsign\",...]]}";

    when(applicationConfig.getOpenskyApiKey()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(baseUrl);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      String result = apiPlanePosition.getAllPLanePositions();
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(dummyResponse);
    }
  }

  @Test
  void getAllPLanePositions_ApiThrowsException_ReturnsEmptyString() {
    String baseUrl = "http://api.opensky.test/states/all";

    when(applicationConfig.getOpenskyApiKey()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(baseUrl);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();

    when(responseSpec.body(String.class)).thenThrow(new RuntimeException("OpenSky API Timeout"));

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      String result = apiPlanePosition.getAllPLanePositions();
      assertThat(result).isNotNull();
      assertThat(result).isEmpty();
    }
  }
}
