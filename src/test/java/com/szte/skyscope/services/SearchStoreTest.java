package com.szte.skyscope.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.szte.skyscope.models.SearchData;
import com.szte.skyscope.services.impl.SearchStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchStoreTest {
  private SearchStoreImpl searchStore;

  @BeforeEach
  void setUp() {
    searchStore = new SearchStoreImpl();
  }

  @Test
  void saveAndGet_ShouldReturnCorrectData() {
    String id = "session-123";
    SearchData data = new SearchData();

    searchStore.saveSearchDatas(id, data);
    SearchData result = searchStore.getSearchDatas(id);

    assertThat(result).isNotNull().isEqualTo(data);
  }

  @Test
  void getSearchDatas_ShouldReturnNull_WhenIdNotFound() {
    SearchData result = searchStore.getSearchDatas("non-existent");
    assertThat(result).isNull();
  }
}
