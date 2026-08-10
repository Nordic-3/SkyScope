package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
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
class ApiFlightServiceTest {

  @Mock private ApplicationConfig applicationConfig;

  @Mock private RestClient restClientMock;
  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private RestClient.ResponseSpec responseSpec;

  private ApiFlightService apiFlightService;

  @BeforeEach
  void setUp() {
    apiFlightService = new ApiFlightService(applicationConfig);
    ReflectionTestUtils.setField(apiFlightService, "restClient", restClientMock);
  }

  @Test
  void getFlightOffers_WithMandatoryParameters_Success() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/flight-offers";
    String dummyResponse = "{\"data\":[]}";

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn("2026-10-15");
    when(flightSearch.getNumberOfAdults()).thenReturn("2");
    when(flightSearch.getReturnDate()).thenReturn(null);
    when(flightSearch.getNumberOfChildren()).thenReturn(null);
    when(flightSearch.getNumberOfInfants()).thenReturn("");
    when(flightSearch.getTravelClass()).thenReturn("ALL");

    when(applicationConfig.getAmadeusFlightOfferSearchApi()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header("Accept", "application/json");
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    String result = apiFlightService.getFlightOffers(flightSearch, token);

    assertThat(result).isEqualTo(dummyResponse);

    String calledUri = uriCaptor.getValue().toString();
    assertThat(calledUri)
        .contains("originLocationCode=BUD")
        .contains("destinationLocationCode=PAR")
        .contains("departureDate=2026-10-15")
        .contains("adults=2")
        .contains("currencyCode=HUF")
        .contains("max=100")
        .doesNotContain("returnDate=")
        .doesNotContain("children=")
        .doesNotContain("infants=")
        .doesNotContain("travelClass=");
  }

  @Test
  void getFlightOffers_WithAllOptionalParameters_Success() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/flight-offers";
    String dummyResponse = "{\"data\":[]}";

    FlightSearch flightSearch = mock(FlightSearch.class);
    when(flightSearch.getOriginCityIata()).thenReturn("BUD");
    when(flightSearch.getDestinationCityIata()).thenReturn("PAR");
    when(flightSearch.getDepartureDate()).thenReturn("2026-10-15");
    when(flightSearch.getNumberOfAdults()).thenReturn("2");

    when(flightSearch.getReturnDate()).thenReturn("2026-10-25");
    when(flightSearch.getNumberOfChildren()).thenReturn("1");
    when(flightSearch.getNumberOfInfants()).thenReturn("1");
    when(flightSearch.getTravelClass()).thenReturn("ECONOMY");

    when(applicationConfig.getAmadeusFlightOfferSearchApi()).thenReturn(baseUrl);

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(uriCaptor.capture());
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    apiFlightService.getFlightOffers(flightSearch, token);

    String calledUri = uriCaptor.getValue().toString();
    assertThat(calledUri)
        .contains("returnDate=2026-10-25")
        .contains("children=1")
        .contains("infants=1")
        .contains("travelClass=ECONOMY");
  }

  @Test
  void getIcaoCodes_Success() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/airline-code";
    String dummyResponse = "{\"data\":[]}";

    Map<String, String> carrierDictionary = new LinkedHashMap<>();
    carrierDictionary.put("LH", "Lufthansa");
    carrierDictionary.put("BA", "British Airways");

    Map<String, String> expectedIcaoCodes = Map.of("LH", "DLH", "BA", "BAW");

    when(applicationConfig.getAmadeusAirlineCode()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(baseUrl, "LH,BA");
    doReturn(requestHeadersSpec)
        .when(requestHeadersSpec)
        .header("Authorization", "Bearer " + token);
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(dummyResponse).when(responseSpec).body(String.class);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.getIcaoCodesFromJson(dummyResponse))
          .thenReturn(expectedIcaoCodes);
      Map<String, String> result = apiFlightService.getIcaoCodes(carrierDictionary, token);
      assertThat(result).isNotNull().isEqualTo(expectedIcaoCodes);
      mockedParser.verify(() -> Parser.getIcaoCodesFromJson(dummyResponse));
    }
  }

  @Test
  void getIcaoCodes_ApiThrowsException_ReturnsEmptyMap() {
    String token = "testToken";
    String baseUrl = "http://api.amadeus.test/airline-code";
    Map<String, String> carrierDictionary = Map.of("LH", "Lufthansa");

    when(applicationConfig.getAmadeusAirlineCode()).thenReturn(baseUrl);

    doReturn(requestHeadersUriSpec).when(restClientMock).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(eq(baseUrl), anyString());
    doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();

    when(responseSpec.body(String.class)).thenThrow(new RuntimeException("API Connection Failed"));

    Map<String, String> result = apiFlightService.getIcaoCodes(carrierDictionary, token);

    assertThat(result).isNotNull().isEmpty();
  }
}
