package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.CheapestDateOffer;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.JsonReaderService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonCheapestFlightDataTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonCheapestFlightData jsonCheapestFlightData;

  @BeforeEach
  void setUp() {
    jsonCheapestFlightData = new JsonCheapestFlightData(jsonReaderService);
  }

  @Test
  void getCheapestDateOffers_Success() {
    String token = "dummyToken";
    FlightSearch flightSearch = mock(FlightSearch.class);
    String dummyJson = "[{\"type\":\"flight-offer\"}]";
    String expectedFilePath = "exampleDatas/cheaperOffer.json";

    CheapestDateOffer dummyOffer = new CheapestDateOffer();
    List<CheapestDateOffer> expectedOffers = Collections.singletonList(dummyOffer);

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser.when(() -> Parser.parseCheapestFlightApi(dummyJson)).thenReturn(expectedOffers);
      List<CheapestDateOffer> result =
          jsonCheapestFlightData.getCheapestDateOffers(flightSearch, token);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedOffers);
      assertThat(result).hasSize(1);
      verify(jsonReaderService).readJsonFromResources(expectedFilePath);
      mockedParser.verify(() -> Parser.parseCheapestFlightApi(dummyJson));
    }
  }
}
