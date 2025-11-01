package com.szte.skyScope.services;

import com.szte.skyScope.models.*;

public interface SearchStore {
  void saveSearchDatas(String id, SearchData searchData);

  SearchData getSearchDatas(String id);
}
