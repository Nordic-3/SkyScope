package com.szte.skyScope.services.providers;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.services.PlanePositionProvider;
import com.szte.skyScope.services.impl.PlanePositionServiceImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestClient;

public class ApiPlanePosition implements PlanePositionProvider {
  private final ApplicationConfig applicationConfig;

  public ApiPlanePosition(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

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
