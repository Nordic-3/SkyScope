package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.szte.skyscope.models.FlightSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

  @Mock private Model model;

  private IndexController indexController;

  @BeforeEach
  void setUp() {
    indexController = new IndexController();
  }

  @Test
  void home_ReturnsIndexView() {
    String viewName = indexController.home();

    assertThat(viewName).isEqualTo("index");
  }

  @Test
  void flightSearch_ReturnsFlightSearchPage_AndAddsAttributeToModel() {
    String viewName = indexController.flightSearch(model);

    assertThat(viewName).isEqualTo("flightSearchPage");

    verify(model).addAttribute(eq("flightSearch"), any(FlightSearch.class));
  }

  @Test
  void flightTracker_ReturnsFlightTrackView() {
    String viewName = indexController.flightTracker();

    assertThat(viewName).isEqualTo("flightTrack");
  }
}
