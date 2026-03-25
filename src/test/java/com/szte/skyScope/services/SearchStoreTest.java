package com.szte.skyScope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.szte.skyScope.models.SearchData;
import com.szte.skyScope.services.impl.SearchStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchStoreTest {
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

    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(data);
  }

  @Test
  void getSearchDatas_ShouldReturnNull_WhenIdNotFound() {
    SearchData result = searchStore.getSearchDatas("non-existent");
    assertThat(result).isNull();
  }
}
