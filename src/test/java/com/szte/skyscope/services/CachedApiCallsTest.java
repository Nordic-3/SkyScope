package com.szte.skyscope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.models.AmadeusApiCred;
import com.szte.skyscope.services.impl.CachedApiCallsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CachedApiCallsTest {

  @Mock private CachedApiCallsProvider cachedApiCallsProvider;

  @Mock private ApplicationConfig applicationConfig;

  @InjectMocks private CachedApiCallsImpl cachedApiCalls;

  @Test
  void getAmadeusApiCred() {
    when(applicationConfig.getAmadeusClientId()).thenReturn("client-id");
    when(applicationConfig.getAmadeusClientSecret()).thenReturn("client-secret");
    when(applicationConfig.getAmadeusAuthUrl()).thenReturn("url");
    AmadeusApiCred cred = new AmadeusApiCred();
    cred.setAccessToken("fake-token");
    when(cachedApiCallsProvider.getApiCred(anyString(), anyString(), anyString())).thenReturn(cred);
    AmadeusApiCred result = cachedApiCalls.getAmadeusApiCred();
    assertThat(result.getAccessToken()).isEqualTo("fake-token");
    verify(cachedApiCallsProvider).getApiCred("client-id", "client-secret", "url");
  }

  @Test
  void getIataCode() {
    when(cachedApiCallsProvider.getIataCode("Budapest", "fake-token")).thenReturn("BUD");
    String iata = cachedApiCalls.getIataCode("Budapest", "fake-token");
    assertThat(iata).isEqualTo("BUD");
    verify(cachedApiCallsProvider).getIataCode("Budapest", "fake-token");
  }

  @Test
  void getAirportName() {
    when(cachedApiCallsProvider.getAirportName("BUD", "fake-token"))
        .thenReturn("Budapest Liszt Ferenc International Airport");
    String airportName = cachedApiCalls.getAirportName("BUD", "fake-token");
    assertThat(airportName).isEqualTo("Budapest Liszt Ferenc International Airport");
    verify(cachedApiCallsProvider).getAirportName("BUD", "fake-token");
  }

  @Test
  void getTestAmadeusApiCred() {
    when(applicationConfig.getAmadeusTestClientId()).thenReturn("client-id");
    when(applicationConfig.getAmadeusTestClientSecret()).thenReturn("client-secret");
    when(applicationConfig.getAmadeusTestAuthUrl()).thenReturn("url");
    AmadeusApiCred cred = new AmadeusApiCred();
    cred.setAccessToken("fake-token");
    when(cachedApiCallsProvider.getApiCred(anyString(), anyString(), anyString())).thenReturn(cred);
    AmadeusApiCred result = cachedApiCalls.getTestAmadeusApiCred();
    assertThat(result.getAccessToken()).isEqualTo("fake-token");
    verify(cachedApiCallsProvider).getApiCred("client-id", "client-secret", "url");
  }
}
