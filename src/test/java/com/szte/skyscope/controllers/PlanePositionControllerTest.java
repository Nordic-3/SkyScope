package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.Plane;
import com.szte.skyscope.services.PlanePositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanePositionControllerTest {

  @Mock private PlanePositionService planePositionService;

  private PlanePositionController planePositionController;

  @BeforeEach
  void setUp() {
    planePositionController = new PlanePositionController(planePositionService);
  }

  @Test
  void planePosition_Success_ReturnsPlaneWhenCallsignIsNotEmpty() {
    String callsign = "RYR1234";
    Plane expectedPlane = mock(Plane.class);

    when(planePositionService.getPlanePosition(callsign)).thenReturn(expectedPlane);

    Plane result = planePositionController.planePosition(callsign);

    assertThat(result).isNotNull().isEqualTo(expectedPlane);

    verify(planePositionService).getPlanePosition(callsign);
  }

  @Test
  void planePosition_ReturnsNull_WhenCallsignIsEmpty() {
    String emptyCallsign = "";

    Plane result = planePositionController.planePosition(emptyCallsign);

    assertThat(result).isNull();

    verifyNoInteractions(planePositionService);
  }
}
