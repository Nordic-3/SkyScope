package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.FlightPriceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class ApiCreateFlightOrderTest {

  @Mock private ApplicationConfig applicationConfig;

  @Mock private RestClient restClientMock;
  @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;
  @Mock private RestClient.RequestBodySpec requestBodySpec;
  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiCreateFlightOrder apiCreateFlightOrder;

  @BeforeEach
  void setUp() {
    apiCreateFlightOrder = new ApiCreateFlightOrder(applicationConfig);
  }

  @Test
  void getFinalPrice_Success() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/finalprice";
    FlightPriceRequest request = new FlightPriceRequest();

    FinalPriceResponse expectedResponse = mock(FinalPriceResponse.class);

    when(applicationConfig.getAmadeusFinalPrice()).thenReturn(baseUrl);

    doReturn(requestBodyUriSpec).when(restClientMock).post();
    doReturn(requestBodySpec).when(requestBodyUriSpec).uri(baseUrl);
    doReturn(requestBodySpec).when(requestBodySpec).header("Authorization", "Bearer " + token);
    doReturn(requestBodySpec).when(requestBodySpec).contentType(MediaType.APPLICATION_JSON);
    doReturn(requestBodySpec).when(requestBodySpec).body(request);
    doReturn(responseSpec).when(requestBodySpec).retrieve();
    doReturn(expectedResponse).when(responseSpec).body(FinalPriceResponse.class);

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      FinalPriceResponse result = apiCreateFlightOrder.getFinalPrice(request, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedResponse);
    }
  }

  @Test
  void getFinalPrice_ApiThrowsException_ReturnsEmptyResponse() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/finalprice";
    FlightPriceRequest request = new FlightPriceRequest();

    when(applicationConfig.getAmadeusFinalPrice()).thenReturn(baseUrl);

    doReturn(requestBodyUriSpec).when(restClientMock).post();
    doReturn(requestBodySpec).when(requestBodyUriSpec).uri(baseUrl);
    doReturn(requestBodySpec).when(requestBodySpec).header("Authorization", "Bearer " + token);
    doReturn(requestBodySpec).when(requestBodySpec).contentType(MediaType.APPLICATION_JSON);
    doReturn(requestBodySpec).when(requestBodySpec).body(request);
    doReturn(responseSpec).when(requestBodySpec).retrieve();

    when(responseSpec.body(FinalPriceResponse.class))
        .thenThrow(new RuntimeException("Amadeus API Timeout"));

    try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
      mockedRestClient.when(RestClient::create).thenReturn(restClientMock);
      FinalPriceResponse result = apiCreateFlightOrder.getFinalPrice(request, token);
      assertThat(result).isNotNull();
      assertThat(result.getClass()).isEqualTo(FinalPriceResponse.class);
    }
  }
}
