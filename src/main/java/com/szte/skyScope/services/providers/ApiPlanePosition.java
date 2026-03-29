package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.services.PlanePositionProvider;
import com.szte.skyScope.services.impl.PlanePositionServiceImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class ApiPlanePosition implements PlanePositionProvider {
  private final ApplicationConfig applicationConfig;

  @Override
  public String getAllPLanePositions() {
    try {
      return RestClient.create()
          .get()
          .uri(applicationConfig.getOpenskyApiKey())
          .retrieve()
          .body(String.class);
    } catch (Exception exception) {
      Logger.getLogger(PlanePositionServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      return "";
    }
  }
}
