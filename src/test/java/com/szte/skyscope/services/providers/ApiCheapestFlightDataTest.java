package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.CheapestDateOffer;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.utils.Constants;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes"})
class ApiCheapestFlightDataTest {

  @Mock private ApplicationConfig applicationConfig;

  @Mock private RestClient restClientMock;
  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiCheapestFlightData apiCheapestFlightData;

  private LocalDate today;

  @BeforeEach
  void setUp() {
    apiCheapestFlightData = new ApiCheapestFlightData(applicationConfig);
    ReflectionTestUtils.setField(apiCheapestFlightData, "restClient", restClientMock);
    today = LocalDate.now(ZoneId.of(Constants.ZONE_ID));
  }

  @Test
  void getCheapestDateOffers_OneWay_FutureGreaterThan15Days_Success() {
    String token = "testToken";
    String baseUrl = "http://api.test/cheapest";
    String dummyResponse = "[{\"dummy\":\"data\"}]";

    LocalDate departure = today.plusDays(20);
    String expectedDateRange = departure.minusDays(15) + "," + departure.plusDays(15);

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn(departure.toString());
    when(flightSearch.isOneWay()).thenReturn(true);

    when(applicationConfig.getAmadeusCheapestDateSearch()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    List<CheapestDateOffer> expectedOffers = List.of(new CheapestDateOffer());

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.parseCheapestFlightApi(dummyResponse))
          .thenReturn(expectedOffers);

      List<CheapestDateOffer> result =
          apiCheapestFlightData.getCheapestDateOffers(flightSearch, token);

      assertThat(result).isEqualTo(expectedOffers);

      String calledUri = uriCaptor.getValue().toString();
      assertThat(calledUri)
          .contains("origin=BUD")
          .contains("destination=PAR")
          .contains("departureDate=" + expectedDateRange)
          .contains("oneWay=true");

      mockedParser.verify(() -> Parser.parseCheapestFlightApi(dummyResponse));
    }
  }

  @Test
  void getCheapestDateOffers_OneWay_FutureLessThan15Days_Success() {
    String token = "testToken";
    String baseUrl = "http://api.test/cheapest";
    String dummyResponse = "[{\"dummy\":\"data\"}]";

    LocalDate departure = today.plusDays(10);
    String expectedDateRange = today.plusDays(1) + "," + departure.plusDays(15);

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn(departure.toString());
    when(flightSearch.isOneWay()).thenReturn(true);

    when(applicationConfig.getAmadeusCheapestDateSearch()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.parseCheapestFlightApi(dummyResponse))
          .thenReturn(new ArrayList<>());

      apiCheapestFlightData.getCheapestDateOffers(flightSearch, token);

      String calledUri = uriCaptor.getValue().toString();
      assertThat(calledUri).contains("departureDate=" + expectedDateRange);
    }
  }

  @Test
  void getCheapestDateOffers_RoundTrip_FutureGreaterThan15Days_Success() {
    String token = "testToken";
    String baseUrl = "http://api.test/cheapest";
    String dummyResponse = "[{\"dummy\":\"data\"}]";

    LocalDate departure = today.plusDays(20);
    LocalDate returnDate = departure.plusDays(5);
    String expectedDateRange = departure.minusDays(15) + "," + returnDate.plusDays(15);

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn(departure.toString());
    when(flightSearch.getReturnDate()).thenReturn(returnDate.toString());
    when(flightSearch.isOneWay()).thenReturn(false);

    when(applicationConfig.getAmadeusCheapestDateSearch()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.parseCheapestFlightApi(dummyResponse))
          .thenReturn(new ArrayList<>());

      apiCheapestFlightData.getCheapestDateOffers(flightSearch, token);

      String calledUri = uriCaptor.getValue().toString();
      assertThat(calledUri).contains("departureDate=" + expectedDateRange);
      assertThat(calledUri).doesNotContain("oneWay=true"); // Mivel RoundTrip
    }
  }

  @Test
  void getCheapestDateOffers_RoundTrip_FutureLessThan15Days_Success() {
    String token = "testToken";
    String baseUrl = "http://api.test/cheapest";
    String dummyResponse = "[{\"dummy\":\"data\"}]";

    LocalDate departure = today.plusDays(10);
    LocalDate returnDate = departure.plusDays(5);
    String expectedDateRange = today.plusDays(1) + "," + returnDate.plusDays(15);

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn(departure.toString());
    when(flightSearch.getReturnDate()).thenReturn(returnDate.toString());
    when(flightSearch.isOneWay()).thenReturn(false);

    when(applicationConfig.getAmadeusCheapestDateSearch()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.parseCheapestFlightApi(dummyResponse))
          .thenReturn(new ArrayList<>());

      apiCheapestFlightData.getCheapestDateOffers(flightSearch, token);

      String calledUri = uriCaptor.getValue().toString();
      assertThat(calledUri).contains("departureDate=" + expectedDateRange);
    }
  }

  @Test
  void getCheapestDateOffers_ThrowsException_ReturnsEmptyList() {
    String token = "testToken";
    String baseUrl = "http://api.test/cheapest";

    LocalDate departure = today.plusDays(20);

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn(departure.toString());
    when(flightSearch.isOneWay()).thenReturn(true);

    when(applicationConfig.getAmadeusCheapestDateSearch()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(URI.class));
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();

    when(responseSpec.body(String.class)).thenThrow(new RuntimeException("API Connection Failed"));

    List<CheapestDateOffer> result =
        apiCheapestFlightData.getCheapestDateOffers(flightSearch, token);

    assertThat(result).isNotNull().isEmpty();
  }
}
