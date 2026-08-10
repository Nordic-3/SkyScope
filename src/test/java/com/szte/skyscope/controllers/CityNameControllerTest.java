package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.models.City;
import com.szte.skyscope.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CityNameControllerTest {

  @Mock private CityService cityService;

  private CityNameController cityNameController;

  @BeforeEach
  void setUp() {
    cityNameController = new CityNameController(cityService);
  }

  @Test
  void getCityCoordinates_Success_ReturnsCityWhenNameIsNotEmpty() {
    String cityName = "Budapest";
    City expectedCity = mock(City.class);

    when(cityService.getCity(cityName)).thenReturn(expectedCity);

    City result = cityNameController.getCityCoordinates(cityName);

    assertThat(result).isNotNull().isEqualTo(expectedCity);
    verify(cityService).getCity(cityName);
  }

  @Test
  void getCityCoordinates_ReturnsNull_WhenCityNameIsEmpty() {
    String emptyCityName = "";

    City result = cityNameController.getCityCoordinates(emptyCityName);

    assertThat(result).isNull();

    verifyNoInteractions(cityService);
  }
}
