package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.szte.skyScope.models.Plane;
import com.szte.skyScope.services.impl.PlanePositionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanePositionServiceTest {

  @Mock PlanePositionProvider planePositionProvider;

  @InjectMocks private PlanePositionServiceImpl planePositionService;

  @Test
  void getPlanePosition() {
    when(planePositionProvider.getAllPLanePositions())
        .thenReturn(
            "{\"states\": [[\"7105b0\",\"TEST\",\"British Airways\",1760879418,1760879418,3.7066,51.2173,10965.18,false,240.77,301.28,0,null,11178.54,\"3572\",false,\"n\"],[\"7105b0\",\"Test1  \",\"United Airlines\",1760879418,1760879418,3.7066,51.2173,10965.18,false,240.77,301.28,0,null,11178.54,\"3572\",false,\"n\"]]}");
    Plane plane = planePositionService.getPlanePosition("Test");
    assertThat(plane.getCallsign()).isEqualTo("TEST");
    assertThat(plane.getLongitude()).isEqualTo("3.7066");
    assertThat(plane.getLatitude()).isEqualTo("51.2173");
    assertThat(plane.getGroundSpeedInMS()).isEqualTo(240.77);
    assertThat(plane.getHeading()).isEqualTo(301.28);
  }

  @Test
  void getPlanePosition_noDataRecieved() {
    when(planePositionProvider.getAllPLanePositions()).thenReturn(null);
    assertThat(planePositionService.getPlanePosition("Test").getCallsign()).isNull();
  }

  @Test
  void getAllPlanePositions() {
    when(planePositionProvider.getAllPLanePositions())
        .thenReturn(
            "{\"states\": [[\"7105b0\",\"TEST\",\"British Airways\",1760879418,1760879418,3.7066,51.2173,10965.18,false,240.77,301.28,0,null,11178.54,\"3572\",false,\"n\"],[\"7105b0\",\"Test1  \",\"United Airlines\",1760879418,1760879418,3.7066,51.2173,10965.18,false,240.77,301.28,0,null,11178.54,\"3572\",false,\"n\"]]}");
    assertThat(planePositionService.getAllPlanePositions().size()).isEqualTo(2);
  }
}
