package com.szte.skyscope.services;

import com.szte.skyscope.models.*;

public interface SearchStore {
  void saveSearchDatas(String id, SearchData searchData);

  SearchData getSearchDatas(String id);
}
