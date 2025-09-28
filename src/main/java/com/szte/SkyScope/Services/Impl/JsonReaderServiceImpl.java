package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Services.JsonReaderService;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class JsonReaderServiceImpl implements JsonReaderService {

  @Override
  public String readJsonFromResources(String file) {
    String json = "";
    try (InputStream jsonFile = getClass().getClassLoader().getResourceAsStream(file)) {
      json = new String(jsonFile.readAllBytes());
    } catch (IOException exception) {
      Logger.getLogger(JsonReaderServiceImpl.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
    }
    return json;
  }
}
