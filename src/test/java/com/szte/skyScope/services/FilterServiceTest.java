package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.factories.FlightOfferDTOFactory;
import com.szte.skyScope.models.ChosenFilters;
import com.szte.skyScope.models.FilterValue;
import com.szte.skyScope.services.impl.FilterServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

  @Mock private SortResultService sortResultService;

  @InjectMocks private FilterServiceImpl filterService;

  @Test
  void filterOffers() {
    FlightOfferDTO offer1 = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    FlightOfferDTO offer2 = FlightOfferDTOFactory.createFlightOfferWithLufthansa();

    ChosenFilters filter = new ChosenFilters();
    filter.setAirlines(List.of("British Airways"));
    filter.setMaxPrice("200000");
    filter.setAirplanes(List.of("Boeing"));
    filter.setTransferNumber(" 1 átszállás");
    filter.setCurrentlyFlying(true);

    List<FlightOfferDTO> result = filterService.filterOffers(List.of(offer1, offer2), filter);

    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isSameAs(offer1);
  }

  @Test
  void filterOffers_transferDuration() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithTransferNumber();
    ChosenFilters filter = new ChosenFilters();
    filter.setTransferDuration("PT2H");
    List<FlightOfferDTO> result = filterService.filterOffers(List.of(offer), filter);
    assertThat(result).isEmpty();
  }

  @Test
  void getFilterOptions() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();

    when(sortResultService.sortOffersByPriceDSC(List.of(offer))).thenReturn(List.of(offer));

    FilterValue options = filterService.getFilterOptions(List.of(offer));

    assertThat(options.getAllAirline()).containsExactly("British Airways");
    assertThat(options.getTransferNumbers()).contains(" 0 átszállás");
    assertThat(options.getAirplaneTypes()).containsExactly("Boeing");
    assertThat(options.getMaxPrice()).isEqualTo("150 000Ft");
    assertThat(options.isCurrentlyFlying()).isTrue();
  }
}
