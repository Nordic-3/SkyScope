package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.JsonReaderService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonFlightServiceTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonFlightService jsonFlightService;

  @BeforeEach
  void setUp() {
    jsonFlightService = new JsonFlightService(jsonReaderService);
  }

  @Test
  void getFlightOffers_Success() {
    String token = "dummyToken";
    FlightSearch mockFlightSearch = mock(FlightSearch.class);
    String expectedFilePath = "exampleDatas/FlightOffers.json";
    String dummyJson = "{\"offers\":[]}";
    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);
    String result = jsonFlightService.getFlightOffers(mockFlightSearch, token);
    assertThat(result).isNotNull().isEqualTo(dummyJson);
    verify(jsonReaderService).readJsonFromResources(expectedFilePath);
  }

  @Test
  void getIcaoCodes_Success() {
    String token = "dummyToken";
    Map<String, String> dummyCarrierDictionary = new HashMap<>();
    dummyCarrierDictionary.put("FR", "Ryanair");

    String expectedFilePath = "exampleDatas/icaoCodes.json";
    String dummyJson = "{\"FR\":\"RYR\"}";

    Map<String, String> expectedIcaoMap = new HashMap<>();
    expectedIcaoMap.put("FR", "RYR");

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser.when(() -> Parser.getIcaoCodesFromJson(dummyJson)).thenReturn(expectedIcaoMap);
      Map<String, String> result = jsonFlightService.getIcaoCodes(dummyCarrierDictionary, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedIcaoMap);
      assertThat(result).hasSize(1);
      verify(jsonReaderService).readJsonFromResources(expectedFilePath);
      mockedParser.verify(() -> Parser.getIcaoCodesFromJson(dummyJson));
    }
  }
}
