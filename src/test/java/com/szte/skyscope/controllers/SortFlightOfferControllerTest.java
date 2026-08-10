package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.enums.FlightOffersSortOptions;
import com.szte.skyscope.models.SearchData;
import com.szte.skyscope.services.SearchStore;
import com.szte.skyscope.services.SortResultService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class SortFlightOfferControllerTest {

  @Mock private SortResultService sortResultService;

  @Mock private SearchStore searchStore;

  @Mock private Model model;

  @Mock private SearchData searchData;

  private SortFlightOfferController controller;

  private static final String SEARCH_ID = "search-123";
  private final List<FlightOfferDTO> dummyOriginalList = Collections.emptyList();
  private final List<FlightOfferDTO> dummySortedList = Collections.emptyList();

  @BeforeEach
  void setUp() {
    controller = new SortFlightOfferController(sortResultService, searchStore);
  }

  @Test
  void sort_ByPriceAsc() {
    executeAndVerifySort(
        FlightOffersSortOptions.PRICE_ASC,
        "priceAsc",
        () ->
            when(sortResultService.sortOffersByPriceASC(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByPriceASC(dummyOriginalList));
  }

  @Test
  void sort_ByPriceDsc() {
    executeAndVerifySort(
        FlightOffersSortOptions.PRICE_DSC,
        "priceDsc",
        () ->
            when(sortResultService.sortOffersByPriceDSC(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByPriceDSC(dummyOriginalList));
  }

  @Test
  void sort_ByFlyTimeAsc() {
    executeAndVerifySort(
        FlightOffersSortOptions.FLYTIME_ASC,
        "flyTimeAsc",
        () ->
            when(sortResultService.sortOffersByFlyTimeAsc(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByFlyTimeAsc(dummyOriginalList));
  }

  @Test
  void sort_ByFlyTimeDsc() {
    executeAndVerifySort(
        FlightOffersSortOptions.FLYTIME_DSC,
        "flyTimeDsc",
        () ->
            when(sortResultService.sortOffersByFlyTimeDsc(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByFlyTimeDsc(dummyOriginalList));
  }

  @Test
  void sort_ByTransferTimeAsc_UsesDefaultSort() {
    executeAndVerifySort(
        FlightOffersSortOptions.TRANSFERTIME_ASC,
        "transferTimeAsc",
        () ->
            when(sortResultService.sortOffersByDeffault(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByDeffault(dummyOriginalList));
  }

  @Test
  void sort_ByTransferTimeDsc() {
    executeAndVerifySort(
        FlightOffersSortOptions.TRANSFERTIME_DSC,
        "transferTimeDsc",
        () ->
            when(sortResultService.sortOffersByTransferTimeDsc(dummyOriginalList))
                .thenReturn(dummySortedList),
        () -> verify(sortResultService).sortOffersByTransferTimeDsc(dummyOriginalList));
  }

  private void executeAndVerifySort(
      FlightOffersSortOptions option,
      String byParam,
      Runnable mockSetupAction,
      Runnable mockVerifyAction) {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(dummyOriginalList);

    mockSetupAction.run();

    try (MockedStatic<FlightOffersSortOptions> mockedEnum =
        mockStatic(FlightOffersSortOptions.class, CALLS_REAL_METHODS)) {
      mockedEnum.when(() -> FlightOffersSortOptions.getOptionFromValue(byParam)).thenReturn(option);
      String viewName = controller.sort(SEARCH_ID, byParam, model);
      assertThat(viewName).isEqualTo("redirect:/resultsPage/" + SEARCH_ID + "?by=" + byParam);
      verify(model).addAttribute("searchId", SEARCH_ID);
      mockVerifyAction.run();
      verify(searchData).setSearchResult(dummySortedList);
    }
  }
}
