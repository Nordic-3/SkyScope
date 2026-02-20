package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.factories.FlightOfferDTOFactory;
import com.szte.skyScope.services.impl.SortResultServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class SortResultServiceTest {

  private final SortResultServiceImpl sortResultService = new SortResultServiceImpl();

  @Test
  void sortOffersByDeffault_transferTimeAsc() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithTransferNumber(),
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByDeffault(offers);
    assertThat(
            sortedOffers
                .getFirst()
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("British Airways");
  }

  @Test
  void sortOffersByPriceASC() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithLufthansa(),
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceASC(offers);
    assertThat(sortedOffers.getFirst().getPrice().getTotal()).isEqualTo("150000.0");
  }

  @Test
  void sortOffersByPriceASC_equalPrice() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceASC(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByPriceASC_alreadyOrdered() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceASC(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByPriceDSC() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways(),
            FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceDSC(offers);
    assertThat(sortedOffers.getFirst().getPrice().getTotal()).isEqualTo("300000");
  }

  @Test
  void sortOffersByPriceDSC_equalPrice() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithLufthansa();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceDSC(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByPriceDSC_alreadyOrdered() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithLufthansa();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByPriceDSC(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByFlyTimeAsc() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithLufthansa(),
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeAsc(offers);
    assertThat(
            sortedOffers
                .getFirst()
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("British Airways");
  }

  @Test
  void sortOffersByFlyTimeAsc_equalTime() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeAsc(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByFlyTimeAsc_alreadyOrdered() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeAsc(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByFlyTimeDsc() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways(),
            FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeDsc(offers);
    assertThat(
            sortedOffers
                .getFirst()
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("Lufthansa");
  }

  @Test
  void sortOffersByFlyTimeDsc_equalTime() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithLufthansa();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithLufthansa());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeDsc(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByFlyTimeDsc_alreadyOrdered() {
    FlightOfferDTO firstOffer = FlightOfferDTOFactory.createFlightOfferWithLufthansa();
    List<FlightOfferDTO> offers =
        Arrays.asList(firstOffer, FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByFlyTimeDsc(offers);
    assertThat(sortedOffers.getFirst()).isEqualTo(firstOffer);
  }

  @Test
  void sortOffersByTransferTimeDsc() {
    List<FlightOfferDTO> offers =
        Arrays.asList(
            FlightOfferDTOFactory.createFlightOfferWithLufthansa(),
            FlightOfferDTOFactory.createFlightOfferWithBritishAirways());
    List<FlightOfferDTO> sortedOffers = sortResultService.sortOffersByTransferTimeDsc(offers);
    assertThat(
            sortedOffers
                .getFirst()
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("Lufthansa");
  }
}
