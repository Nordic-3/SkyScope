package com.szte.skyScope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyScope.services.impl.JsonReaderServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class JsonReaderServiceTest {

  private final JsonReaderServiceImpl reader = new JsonReaderServiceImpl();

  @Test
  void readJsonFromResources_ShouldReturnContent_WhenFileExists() {
    String result = reader.readJsonFromResources("test.json");
    assertThat(result).containsIgnoringWhitespaces("\"name\":\"test\"");
  }

  @Test
  void readJsonFromResources_ShouldReturnEmptyString_WhenFileDoesNotExist() {
    String result = reader.readJsonFromResources("non-existent.json");
    assertThat(result).isEmpty();
  }

  @Test
  void readJsonFromResources_ShouldReturnEmptyString_WhenIOExceptionOccurs() throws IOException {
    JsonReaderServiceImpl spyReader = spy(new JsonReaderServiceImpl());
    InputStream mockStream = mock(InputStream.class);
    when(mockStream.readAllBytes()).thenThrow(new IOException("Simulated IO error"));

    doReturn(mockStream).when(spyReader).getResourceStream(anyString());
    String result = spyReader.readJsonFromResources("barmilyen_fajl.json");
    assertThat(result).isEmpty();
  }
}
