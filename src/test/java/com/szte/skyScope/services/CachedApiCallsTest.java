package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import com.szte.skyScope.config.ApplicationConfig;
import com.szte.skyScope.models.AmadeusApiCred;
import com.szte.skyScope.services.impl.CachedApiCallsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CachedApiCallsTest {

  @Mock private CachedApiCallsProvider cachedApiCallsProvider;

  @Mock private ApplicationConfig applicationConfig;

  @InjectMocks private CachedApiCallsImpl cachedApiCalls;

  @Test
  void getAmadeusApiCred() {
    Mockito.when(applicationConfig.getAmadeusClientId()).thenReturn("client-id");
    Mockito.when(applicationConfig.getAmadeusClientSecret()).thenReturn("client-secret");
    Mockito.when(applicationConfig.getAmadeusAuthUrl()).thenReturn("url");
    AmadeusApiCred cred = new AmadeusApiCred();
    cred.setAccess_token("fake-token");
    Mockito.when(cachedApiCallsProvider.getApiCred(anyString(), anyString(), anyString()))
        .thenReturn(cred);
    AmadeusApiCred result = cachedApiCalls.getAmadeusApiCred();
    assertThat(result.getAccess_token()).isEqualTo("fake-token");
    verify(cachedApiCallsProvider).getApiCred("client-id", "client-secret", "url");
  }

  @Test
  void getIataCode() {
    Mockito.when(cachedApiCallsProvider.getIataCode("Budapest", "fake-token")).thenReturn("BUD");
    String iata = cachedApiCalls.getIataCode("Budapest", "fake-token");
    assertThat(iata).isEqualTo("BUD");
    verify(cachedApiCallsProvider).getIataCode("Budapest", "fake-token");
  }

  @Test
  void getAirportName() {
    Mockito.when(cachedApiCallsProvider.getAirportName("BUD", "fake-token"))
        .thenReturn("Budapest Liszt Ferenc International Airport");
    String airportName = cachedApiCalls.getAirportName("BUD", "fake-token");
    assertThat(airportName).isEqualTo("Budapest Liszt Ferenc International Airport");
    verify(cachedApiCallsProvider).getAirportName("BUD", "fake-token");
  }

  @Test
  void getTestAmadeusApiCred() {
    Mockito.when(applicationConfig.getAmadeusTestClientId()).thenReturn("client-id");
    Mockito.when(applicationConfig.getAmadeusTestClientSecret()).thenReturn("client-secret");
    Mockito.when(applicationConfig.getAmadeusTestAuthUrl()).thenReturn("url");
    AmadeusApiCred cred = new AmadeusApiCred();
    cred.setAccess_token("fake-token");
    Mockito.when(cachedApiCallsProvider.getApiCred(anyString(), anyString(), anyString()))
        .thenReturn(cred);
    AmadeusApiCred result = cachedApiCalls.getTestAmadeusApiCred();
    assertThat(result.getAccess_token()).isEqualTo("fake-token");
    verify(cachedApiCallsProvider).getApiCred("client-id", "client-secret", "url");
  }
}
