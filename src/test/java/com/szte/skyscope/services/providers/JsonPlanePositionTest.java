package com.szte.skyscope.services.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.services.JsonReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonPlanePositionTest {

  @Mock private JsonReaderService jsonReaderService;

  private JsonPlanePosition jsonPlanePosition;

  @BeforeEach
  void setUp() {
    jsonPlanePosition = new JsonPlanePosition(jsonReaderService);
  }

  @Test
  void getAllPLanePositions_Success() {
    String expectedFilePath = "exampleDatas/planePositions.json";
    String dummyJson = "[{\"icao24\":\"a0b1c2\",\"callsign\":\"RYR123\"}]";

    when(jsonReaderService.readJsonFromResources(expectedFilePath)).thenReturn(dummyJson);

    String result = jsonPlanePosition.getAllPLanePositions();

    assertThat(result).isNotNull().isEqualTo(dummyJson);

    verify(jsonReaderService).readJsonFromResources(expectedFilePath);
  }
}
