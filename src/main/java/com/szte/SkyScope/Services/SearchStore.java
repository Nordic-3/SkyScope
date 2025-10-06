package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.*;

public interface SearchStore {
  void saveSearchDatas(String id, SearchData searchData);

  SearchData getSearchDatas(String id);
}
