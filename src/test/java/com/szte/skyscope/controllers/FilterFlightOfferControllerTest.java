package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.ChosenFilters;
import com.szte.skyscope.models.SearchData;
import com.szte.skyscope.services.FilterService;
import com.szte.skyscope.services.SearchStore;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterFlightOfferControllerTest {

  @Mock private SearchStore searchStore;

  @Mock private FilterService filterService;

  @Mock private SearchData searchData;

  private FilterFlightOfferController controller;

  private static final String SEARCH_ID = "search123";
  private final List<FlightOfferDTO> dummyOriginalList = Collections.emptyList();
  private final List<FlightOfferDTO> dummyFilteredList = Collections.emptyList();

  @BeforeEach
  void setUp() {
    controller = new FilterFlightOfferController(searchStore, filterService);
  }

  @Test
  void filterOffers_Success() {
    ChosenFilters filters = new ChosenFilters();
    String byParam = "priceAsc";

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(dummyOriginalList);
    when(filterService.filterOffers(dummyOriginalList, filters)).thenReturn(dummyFilteredList);

    String viewName = controller.filterOffers(filters, SEARCH_ID, byParam);

    assertThat(viewName).isEqualTo("redirect:/resultsPage/" + SEARCH_ID + "?by=" + byParam);

    verify(searchData).setFilterAttribute(filters);
    verify(searchData).setSearchResult(dummyFilteredList);
  }

  @Test
  void resetFilters_Success() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getOriginalSearchResult()).thenReturn(dummyOriginalList);

    String viewName = controller.resetFilters(SEARCH_ID);

    assertThat(viewName).isEqualTo("redirect:/resultsPage/" + SEARCH_ID + "?by=transferTimeAsc");

    verify(searchData).setFilterAttribute(any(ChosenFilters.class));
    verify(searchData).setSearchResult(dummyOriginalList);
  }
}
