package com.szte.skyScope.dataStore;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
  private final Map<String, Object> dataStore = new HashMap<>();

  public void putData(String key, Object value) {
    dataStore.put(key, value);
  }

  public <T> T getValue(String key, Class<T> type) {
    return type.cast(dataStore.get(key));
  }
}
