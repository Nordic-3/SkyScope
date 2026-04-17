package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.factories.FlightOfferDTOFactory;
import com.szte.skyScope.factories.FlightSearchFactory;
import com.szte.skyScope.factories.TravellerFactory;
import com.szte.skyScope.models.FlightSearch;
import com.szte.skyScope.models.Traveller;
import com.szte.skyScope.models.TravellerWrapper;
import com.szte.skyScope.services.impl.InputValidationServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InputValidationServiceTest {

  private InputValidationServiceImpl validationService;

  @BeforeEach
  void setUp() {
    validationService = new InputValidationServiceImpl();
  }

  @Test
  void validateInputFields() {
    FlightSearch empty = new FlightSearch();
    empty.setNumberOfAdults("");
    String response = validationService.validateInputFields(empty);
    assertThat(response).contains("mező kitöltése kötelező!");

    FlightSearch valid = FlightSearchFactory.createValidFlightSearch();

    String response2 = validationService.validateInputFields(valid);
    assertThat(response2).isEmpty();
  }

  @Test
  void validateIataCodes() {
    FlightSearch flightSearch = FlightSearchFactory.createFlighSearchInvalidOriginCty();
    String response = validationService.validateIataCodes(flightSearch);
    assertThat(response).isEqualTo("Helyeten város név!");

    FlightSearch flightSearch2 = new FlightSearch();
    flightSearch2.setOriginCityIata("BUD");
    flightSearch2.setDestinationCityIata("LON");
    flightSearch2.setOneWay(true);
    String response2 = validationService.validateIataCodes(flightSearch2);
    assertThat(response2).isEmpty();
  }

  @Test
  void validateIataCodes_destinationCityInvalid() {
    FlightSearch flightSearch = FlightSearchFactory.createFlighSearchInvalidDestinationCity();
    String response = validationService.validateIataCodes(flightSearch);
    assertThat(response).isEqualTo("Helyeten város név!");
  }

  @Test
  void validateFlightSearchDates() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    flightSearch.setDepartureDate("2020-01-01");
    String response = validationService.validateInputFields(flightSearch);
    assertThat(response).contains("Az indulás napja nem lehet korábbi a mai napnál!");
  }

  @Test
  void validateFlightSearchDates_returnDateBeforeDeparture() {
    FlightSearch flightSearch = new FlightSearch();
    flightSearch.setDepartureDate(LocalDate.now().plusDays(10).toString());
    flightSearch.setReturnDate(LocalDate.now().plusDays(1).toString());
    String response = validationService.validateInputFields(flightSearch);
    assertThat(response).contains("A visszaút napja nem lehet korábbi az indulás napjánál!");
  }

  @Test
  void validateFlightSearchDates_invalidInfantsNumber() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    flightSearch.setNumberOfInfants("-1");
    String response = validationService.validateInputFields(flightSearch);
    assertThat(response).contains("Csak pozitív egész számot használjon!");
  }

  @Test
  void validateFlightSearchDates_invalidChildrenNumber() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    flightSearch.setNumberOfChildren("-1");
    String response = validationService.validateInputFields(flightSearch);
    assertThat(response).contains("Csak pozitív egész számot használjon!");
  }

  @Test
  void validateFlightSearchDates_invalidMaxPriceNumber() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    flightSearch.setMaxPrice("-1");
    String response = validationService.validateInputFields(flightSearch);
    assertThat(response).contains("Csak pozitív egész számot használjon!");
  }

  @Test
  void validateTravellers() {
    Traveller validTraveller = TravellerFactory.createTraveller();

    TravellerWrapper validWrapper = new TravellerWrapper();
    validWrapper.setTravellers(java.util.List.of(validTraveller));

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();

    TravellerWrapper wrapper = new TravellerWrapper();
    Traveller traveller = new Traveller();
    wrapper.setTravellers(java.util.List.of(traveller));

    String response = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response).contains("Minden mező kitöltése kötelező");

    String response1 = validationService.validateTravellers(validWrapper, flightOffer);
    assertThat(response1).isEmpty();
  }

  @Test
  void validateTravellers_invalidIssuanceContryCode() {
    Traveller traveller = TravellerFactory.createTraveller();
    traveller.getDocuments().getFirst().setIssuanceCountry("invalid");

    TravellerWrapper wrapper = new TravellerWrapper();
    wrapper.setTravellers(java.util.List.of(traveller));

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();

    String response = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response)
        .contains("Az állampolgárság és kiállító ország mezőbe két betűs rövidítést használjon!");
  }

  @Test
  void validateTravellers_expiredDocument() {
    Traveller traveller = TravellerFactory.createTraveller();
    traveller.getDocuments().getFirst().setExpiryDate(LocalDate.now().plusDays(4).toString());

    TravellerWrapper wrapper = new TravellerWrapper();
    wrapper.setTravellers(java.util.List.of(traveller));

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();

    String response = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response).contains("Az úti okmány nem érvényes az utazás idején!");

    traveller.getDocuments().getFirst().setExpiryDate("invalid");
    String response1 = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response1).contains("Nem érvényes dátum formátum!");

    traveller.getDocuments().getFirst().setExpiryDate(LocalDate.now().minusYears(10).toString());
    String response2 = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response2)
        .contains("Az úti okmány lejárati dátuma hamarabb van, mint a kiállítása!");
  }

  @Test
  void validateTravellers_travelDocumentExpireBeforeIssue() {
    Traveller traveller = TravellerFactory.createTraveller();
    traveller.getDocuments().getFirst().setExpiryDate(LocalDate.now().plusDays(4).toString());

    TravellerWrapper wrapper = new TravellerWrapper();
    wrapper.setTravellers(java.util.List.of(traveller));

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();

    traveller.getDocuments().getFirst().setExpiryDate(LocalDate.now().minusYears(10).toString());
    String response = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response).contains("Az úti okmány lejárati dátuma hamarabb van, mint a kiállítása!");

    traveller.getDocuments().getFirst().setExpiryDate("invalid");
    String response1 = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response1).contains("Nem érvényes dátum formátum!");
  }
}
