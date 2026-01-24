package com.szte.skyScope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.models.CheapestDateOffer;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.models.SearchData;
import com.szte.skyScope.services.impl.CheapestFlightDateServiveImpl;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheapestFlightDateServiveTest {

  @Mock private FlightService flightService;
  @Mock private SearchStore searchStore;
  @Mock private CheapestFlightDataProvider cheapestFlightDataProvider;

  @InjectMocks private CheapestFlightDateServiveImpl cheapestFlightDateService;

  private FlightSearch originalSearch;
  private final String token = "test-token";
  private final String searchId = "search-123";

  @BeforeEach
  void setUp() {
    originalSearch = new FlightSearch();
    originalSearch.setOneWay(true);
  }

  @Test
  void shouldReturnNullWhenNoCheaperOffersFound() {
    List<FlightOfferDTO> currentOffers = List.of(createMockOffer("100.00"));
    CheapestDateOffer expensiveOffer = new CheapestDateOffer();
    expensiveOffer.setPrice(40000);

    when(cheapestFlightDataProvider.getCheapestDateOffers(any(), any()))
        .thenReturn(List.of(expensiveOffer));

    CompletableFuture<List<FlightOfferDTO>> result =
        cheapestFlightDateService.checkForCheaperOfferAndGetIt(
            originalSearch, token, searchId, currentOffers);

    assertThat(result.join()).isNull();
    verifyNoInteractions(flightService);
  }

  @Test
  void shouldReturnNewOffersWhenCheaperOfferExists() {
    List<FlightOfferDTO> currentOffers = List.of(createMockOffer("100.00"));
    CheapestDateOffer cheapOffer = new CheapestDateOffer();
    cheapOffer.setPrice(30000);
    cheapOffer.setDepartureDate("2026-05-10");

    when(cheapestFlightDataProvider.getCheapestDateOffers(any(), any()))
        .thenReturn(List.of(cheapOffer));
    SearchData mockSearchData = mock(SearchData.class);
    when(searchStore.getSearchDatas(searchId)).thenReturn(mockSearchData);
    List<FlightOfferDTO> newOffers = List.of(new FlightOfferDTO());
    when(flightService.getFlightOffers(any(), eq(token), eq(searchId)))
        .thenReturn(CompletableFuture.completedFuture(newOffers));

    CompletableFuture<List<FlightOfferDTO>> result =
        cheapestFlightDateService.checkForCheaperOfferAndGetIt(
            originalSearch, token, searchId, currentOffers);

    assertThat(result.join()).isEqualTo(newOffers);
    verify(mockSearchData).setCheaperSearch(any(FlightSearch.class));
    verify(flightService).getFlightOffers(any(), any(), any());
  }

  private FlightOfferDTO createMockOffer(String priceTotal) {
    FlightOfferDTO offer = new FlightOfferDTO();
    FlightOfferDTO.TravelerPricing pricing = new FlightOfferDTO.TravelerPricing();
    pricing.setTravelerType("ADULT");
    FlightOfferDTO.Price price = new FlightOfferDTO.Price();
    price.setTotal(priceTotal);
    pricing.setPrice(price);
    offer.setTravelerPricings(List.of(pricing));
    return offer;
  }
}
