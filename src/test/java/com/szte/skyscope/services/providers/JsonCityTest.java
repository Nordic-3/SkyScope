package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.City;
import com.szte.skyscope.parsers.Parser;
import com.szte.skyscope.services.JsonReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonCityTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonCity jsonCity;

  @BeforeEach
  void setUp() {
    jsonCity = new JsonCity(jsonReaderService);
  }

  @Test
  void getCity_Success() {
    String cityName = "Budapest";
    String dummyJson = "[{\"name\":\"Budapest\"}]";
    String expectedFilePath = "exampleDatas/cities.json";

    City expectedCity = mock(City.class);

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    try (MockedStatic<Parser> mockedParser = mockStatic(Parser.class)) {
      mockedParser.when(() -> Parser.parseJsonToCity(dummyJson, cityName)).thenReturn(expectedCity);
      City result = jsonCity.getCity(cityName);
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedCity);
      verify(jsonReaderService).readJsonFromResources(expectedFilePath);
      mockedParser.verify(() -> Parser.parseJsonToCity(dummyJson, cityName));
    }
  }
}
