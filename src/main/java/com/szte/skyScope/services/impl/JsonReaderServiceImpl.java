package com.szte.skyScope.services.impl;

import com.szte.skyScope.services.JsonReaderService;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonReaderServiceImpl implements JsonReaderService {

  @Override
  public String readJsonFromResources(String file) {
    String json = "";
    try (InputStream jsonFile = getResourceStream(file)) {
      if (jsonFile == null) {
        log.error("Could not find the file {} in resources", file);
        return "";
      }
      json = new String(jsonFile.readAllBytes());
    } catch (IOException exception) {
      log.error(
          "Error while reading the file {} from resources: {}",
          file,
          exception.getMessage(),
          exception);
    }
    return json;
  }

  public InputStream getResourceStream(String file) {
    return getClass().getClassLoader().getResourceAsStream(file);
  }
}
