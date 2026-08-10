package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.FinalPriceResponse;
import com.szte.skyscope.models.FlightPriceRequest;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.JsonReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonCreateFlightOrderTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonCreateFlightOrder jsonCreateFlightOrder;

  @BeforeEach
  void setUp() {
    jsonCreateFlightOrder = new JsonCreateFlightOrder(jsonReaderService);
  }

  @Test
  void getFinalPrice_Success_ReturnsParsedResponse() {
    String token = "dummyToken";
    FlightPriceRequest mockRequest = mock(FlightPriceRequest.class);
    String dummyJson = "{\"price\":\"100\"}";
    String expectedFilePath = "exampleDatas/finalPrice.json";

    FinalPriceResponse expectedResponse = mock(FinalPriceResponse.class);

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser
          .when(() -> Parser.parseFlightPriceRequest(dummyJson))
          .thenReturn(expectedResponse);
      FinalPriceResponse result = jsonCreateFlightOrder.getFinalPrice(mockRequest, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedResponse);
      verify(jsonReaderService).readJsonFromResources(expectedFilePath);
      mockedParser.verify(() -> Parser.parseFlightPriceRequest(dummyJson));
    }
  }

  @Test
  void getFinalPrice_ReturnsNewObject_WhenParserReturnsNull() {
    String token = "dummyToken";
    FlightPriceRequest mockRequest = mock(FlightPriceRequest.class);
    String dummyJson = "invalid_json";
    String expectedFilePath = "exampleDatas/finalPrice.json";

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser.when(() -> Parser.parseFlightPriceRequest(dummyJson)).thenReturn(null);
      FinalPriceResponse result = jsonCreateFlightOrder.getFinalPrice(mockRequest, token);
      assertThat(result).isNotNull();
      assertThat(result).isExactlyInstanceOf(FinalPriceResponse.class);
      verify(jsonReaderService).readJsonFromResources(expectedFilePath);
      mockedParser.verify(() -> Parser.parseFlightPriceRequest(dummyJson));
    }
  }
}
