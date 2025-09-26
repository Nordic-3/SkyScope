package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Models.*;
import com.szte.SkyScope.Services.SearchStore;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SearchStoreImpl implements SearchStore {
  private final Map<String, SearchData> store = new HashMap<>();

  @Override
  public void saveSearchDatas(String id, SearchData searchData) {
    store.put(id, searchData);
  }

  @Override
  public SearchData getSearchDatas(String id) {
    return store.get(id);
  }
}
