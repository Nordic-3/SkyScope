package com.szte.skyscope.services.providers;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.services.PlanePositionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
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
      log.error(
          "Error while calling OpenSky API for plane positions: {}",
          exception.getMessage(),
          exception);
      return "";
    }
  }
}
