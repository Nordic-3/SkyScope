package com.szte.skyScope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szte.skyScope.models.City;
import com.szte.skyScope.services.impl.CityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

  @Mock CityProvider cityProvider;

  @InjectMocks private CityServiceImpl cityService;

  @Test
  void getCity() {
    City city = new City();
    city.setName("Budapest");
    city.setLat("47.4979");
    city.setLng("19.0402");
    when(cityProvider.getCity("Budapest")).thenReturn(city);
    City result = cityService.getCity("Budapest");
    assertThat(result.getName()).isEqualTo("Budapest");
    assertThat(result.getLat()).isEqualTo("47.4979");
    assertThat(result.getLng()).isEqualTo("19.0402");
    verify(cityProvider).getCity("Budapest");
  }
}
