package com.szte.skyscope.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.models.ChosenFilters;
import com.szte.skyscope.models.FilterValue;
import com.szte.skyscope.models.FlightSearch;
import com.szte.skyscope.models.SearchData;
import com.szte.skyscope.services.*;
import com.szte.skyscope.utils.FlightOfferFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class FlightSearchControllerTest {

  @Mock private InputValidationService inputValidationService;
  @Mock private FlightService flightService;
  @Mock private SearchStore searchStore;
  @Mock private SortResultService sortResultService;
  @Mock private FilterService filterService;
  @Mock private CheapestFlightDateService cheapestFlightDateService;
  @Mock private PlanePositionService planePositionService;

  @Mock private Model model;
  @Mock private SearchData searchData;

  private FlightSearchController controller;

  private static final String DUMMY_TOKEN = "dummyToken";
  private static final String SEARCH_ID = "search-123";

  @BeforeEach
  void setUp() {
    controller =
        new FlightSearchController(
            inputValidationService,
            flightService,
            searchStore,
            sortResultService,
            filterService,
            cheapestFlightDateService,
            planePositionService);
  }

  @Test
  void flightSubmit_ValidationFails_ReturnsFlightSearchPage() {
    FlightSearch flightSearch = new FlightSearch();
    String errorMsg = "Input validation failed";
    when(inputValidationService.validateInputFields(flightSearch)).thenReturn(errorMsg);

    String viewName = controller.flightSubmit(flightSearch, model);

    assertThat(viewName).isEqualTo("flightSearchPage");
    verify(model).addAttribute("errorMessages", errorMsg);
    verifyNoInteractions(flightService);
  }

  @Test
  void flightSubmit_IataValidationFails_ReturnsFlightSearchPage() {
    FlightSearch flightSearch = new FlightSearch();
    when(inputValidationService.validateInputFields(flightSearch))
        .thenReturn(""); // Első validáció sikeres
    when(flightService.getToken()).thenReturn(DUMMY_TOKEN);

    String iataErrorMsg = "IATA code invalid";
    when(inputValidationService.validateIataCodes(flightSearch)).thenReturn(iataErrorMsg);

    String viewName = controller.flightSubmit(flightSearch, model);

    assertThat(viewName).isEqualTo("flightSearchPage");
    verify(flightService).setIataCodes(flightSearch, DUMMY_TOKEN);
    verify(model).addAttribute("errorMessages", iataErrorMsg);
  }

  @Test
  void flightSubmit_Success_TriggersAsyncTasks() {
    FlightSearch flightSearch = new FlightSearch();
    when(inputValidationService.validateInputFields(flightSearch)).thenReturn("");
    when(inputValidationService.validateIataCodes(flightSearch)).thenReturn("");
    when(flightService.getToken()).thenReturn(DUMMY_TOKEN);

    List<FlightOfferDTO> mockOffers = Collections.singletonList(new FlightOfferDTO());
    when(flightService.getFlightOffers(eq(flightSearch), eq(DUMMY_TOKEN), anyString()))
        .thenReturn(CompletableFuture.completedFuture(mockOffers));

    when(searchStore.getSearchDatas(anyString())).thenReturn(searchData);
    when(searchData.getCarrierDictionary()).thenReturn(new HashMap<>());
    when(flightService.getIcaoCodes(any(), eq(DUMMY_TOKEN))).thenReturn(new HashMap<>());

    when(planePositionService.getAllPlanePositions()).thenReturn(Collections.emptyMap());
    when(sortResultService.sortOffersByDeffault(mockOffers)).thenReturn(mockOffers);

    String viewName = controller.flightSubmit(flightSearch, model);

    assertThat(viewName).isEqualTo("loading");

    ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
    verify(model).addAttribute(eq("searchId"), idCaptor.capture());
    String generatedId = idCaptor.getValue();
    assertThat(generatedId).isNotBlank();

    verify(flightService).setCallsigns(eq(mockOffers), any());

    verify(flightService).setIsCurrentlyFlying(mockOffers, Collections.emptyMap());
    verify(flightService).setFlightOffersAttributes(mockOffers, generatedId, DUMMY_TOKEN);
    verify(searchData).setSearchResult(mockOffers);
    verify(searchData).setOriginalSearchResult(mockOffers);
    verify(searchData).setFlightSearch(flightSearch);
  }

  @Test
  void flightSubmit_AsyncFails_SetsEmptyList() {
    FlightSearch flightSearch = new FlightSearch();
    when(inputValidationService.validateInputFields(flightSearch)).thenReturn("");
    when(inputValidationService.validateIataCodes(flightSearch)).thenReturn("");
    when(flightService.getToken()).thenReturn(DUMMY_TOKEN);

    when(flightService.getFlightOffers(eq(flightSearch), eq(DUMMY_TOKEN), anyString()))
        .thenReturn(CompletableFuture.failedFuture(new RuntimeException("API error")));

    when(searchStore.getSearchDatas(anyString())).thenReturn(searchData);

    String viewName = controller.flightSubmit(flightSearch, model);

    assertThat(viewName).isEqualTo("loading");

    verify(searchData).setSearchResult(argThat(List::isEmpty));
  }

  @Test
  void checkIfOffersAvailable_ReturnsInProgress_WhenResultIsNull() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(null);

    ResponseEntity<String> response = controller.checkIfOffersAvaliable(SEARCH_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    assertThat(response.getBody()).isEqualTo("IN_PROGRESS");
  }

  @Test
  void checkIfOffersAvailable_ReturnsError_WhenResultIsEmpty() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(new ArrayList<>());

    ResponseEntity<String> response = controller.checkIfOffersAvaliable(SEARCH_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(response.getBody()).isEqualTo("ERROR");
  }

  @Test
  void checkIfOffersAvailable_ReturnsReady_WhenResultIsPopulated() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getSearchResult()).thenReturn(Collections.singletonList(new FlightOfferDTO()));

    ResponseEntity<String> response = controller.checkIfOffersAvaliable(SEARCH_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("READY");
  }

  @Test
  void resultsPage_NormalFlow_SetsAttributesAndChecksCheaper() {
    FlightSearch flightSearch = new FlightSearch();
    String by = "priceAsc";
    List<FlightOfferDTO> dummyResults = Collections.singletonList(new FlightOfferDTO());
    ChosenFilters dummyFilters = new ChosenFilters();

    FilterValue dummyFilterOptions = mock(FilterValue.class);

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getFlightSearch()).thenReturn(flightSearch);
    when(searchData.getSearchResult()).thenReturn(dummyResults);
    when(searchData.getFilterAttribute()).thenReturn(dummyFilters);
    when(filterService.getFilterOptions(dummyResults)).thenReturn(dummyFilterOptions);

    when(searchData.isCheaperOfferAvailable()).thenReturn(false);
    when(searchData.haveToCheckForCheaperOffer()).thenReturn(true);
    when(flightService.getToken()).thenReturn(DUMMY_TOKEN);
    when(sortResultService.sortOffersByPriceASC(dummyResults)).thenReturn(dummyResults);
    when(cheapestFlightDateService.checkForCheaperOfferAndGetIt(
            flightSearch, DUMMY_TOKEN, SEARCH_ID, dummyResults))
        .thenReturn(
            CompletableFuture.completedFuture(null)); // Nullt adunk vissza, tehát nincs olcsóbb

    try (MockedStatic<FlightOfferFormatter> mockedFormatter =
        mockStatic(FlightOfferFormatter.class)) {
      mockedFormatter
          .when(() -> FlightOfferFormatter.formatFlightOfferFields(dummyResults))
          .thenReturn(dummyResults);
      String viewName = controller.resultsPage(SEARCH_ID, model, flightSearch, by, null);
      assertThat(viewName).isEqualTo("flightOffers");
      verify(model).addAttribute("flightSearch", flightSearch);
      verify(model).addAttribute("results", dummyResults);
      verify(model).addAttribute("searchId", SEARCH_ID);
      verify(model).addAttribute("sortBy", by);
      verify(model).addAttribute("filterOffers", dummyFilters);
      verify(model).addAttribute("filterOptions", dummyFilterOptions);
      verify(searchData).setHaveToCheckForCheaperOffer(false);
    }
  }

  @Test
  void resultsPage_UserChoseCheaper_UpdatesSearchResults() {
    String by = "priceAsc";
    String cheaper = "true";
    List<FlightOfferDTO> cheaperResults = Collections.singletonList(new FlightOfferDTO());
    FlightSearch cheaperSearch = new FlightSearch();

    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.getCheaperSearchResult()).thenReturn(cheaperResults);
    when(flightService.getToken()).thenReturn(DUMMY_TOKEN);
    when(sortResultService.sortOffersByDeffault(cheaperResults)).thenReturn(cheaperResults);
    when(searchData.getCheaperSearch()).thenReturn(cheaperSearch);

    controller.resultsPage(SEARCH_ID, model, new FlightSearch(), by, cheaper);
    verify(flightService).setFlightOffersAttributes(cheaperResults, SEARCH_ID, DUMMY_TOKEN);
    verify(searchData).setSearchResult(cheaperResults);
    verify(searchData).setOriginalSearchResult(cheaperResults);
    verify(searchData).setFlightSearch(cheaperSearch);
  }

  @Test
  void checkIfCheaperOfferAvailable_ReturnsReady_WhenAvailable() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.isCheaperOfferAvailable()).thenReturn(true);

    ResponseEntity<String> response = controller.checkIfCheaperOfferAvaliable(SEARCH_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("READY");
  }

  @Test
  void checkIfCheaperOfferAvailable_ReturnsInProgress_WhenNotAvailable() {
    when(searchStore.getSearchDatas(SEARCH_ID)).thenReturn(searchData);
    when(searchData.isCheaperOfferAvailable()).thenReturn(false);

    ResponseEntity<String> response = controller.checkIfCheaperOfferAvaliable(SEARCH_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    assertThat(response.getBody()).isEqualTo("IN_PROGRESS");
  }
}
