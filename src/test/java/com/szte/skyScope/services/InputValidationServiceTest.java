package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.szte.skyScope.config.SecurityConfig;
import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.dtos.UserCreationDTO;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class InputValidationServiceTest {

  @Mock private SecurityConfig securityConfig;

  @Mock private PasswordEncoder passwordEncoder;

  private InputValidationServiceImpl validationService;

  @BeforeEach
  void setUp() {
    validationService = new InputValidationServiceImpl(securityConfig);
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
  void validatePasswordAndEmail() {
    UserCreationDTO missingEmail = new UserCreationDTO("", "p", "p", "", true);
    String response1 = validationService.validatePasswordAndEmail(missingEmail);
    assertThat(response1).contains("Email cím");

    UserCreationDTO valid = new UserCreationDTO("a@b.com", "Abcd1234", "Abcd1234", "", true);
    String response2 = validationService.validatePasswordAndEmail(valid);
    assertThat(response2).isEmpty();
  }

  @Test
  void validateUserCreation_emptyPassword() {
    UserCreationDTO missingPassword = new UserCreationDTO("test@test.hu", "", "", "", true);
    String response = validationService.validatePasswordAndEmail(missingPassword);
    assertThat(response).contains("Jelszó mező kitöltése kötelező!");
  }

  @Test
  void validateUserCreation_notMatchingPassword() {
    UserCreationDTO missingPassword = new UserCreationDTO("test@test.hu", "test", "tset", "", true);
    String response = validationService.validatePasswordAndEmail(missingPassword);
    assertThat(response).contains("A jelszavak nem egyeznek meg!");
  }

  @Test
  void validateUserCreation_tooShortPassword() {
    UserCreationDTO missingPassword = new UserCreationDTO("test@test.hu", "test", "test", "", true);
    String response = validationService.validatePasswordAndEmail(missingPassword);
    assertThat(response).contains("A jelszónak legalább 8 karakter hosszúnak kell lennie!");
  }

  @Test
  void validateUserCreation_weakPassword() {
    UserCreationDTO missingPassword =
        new UserCreationDTO("test@test.hu", "aaaaaaaa", "aaaaaaaa", "", true);
    String response = validationService.validatePasswordAndEmail(missingPassword);
    assertThat(response)
        .contains(
            "A jelszónak tartalmaznia kell legalább egy nagybetűt, egy kisbetűt és egy számot!");
  }

  @Test
  void validateUserCreation_gdprNotAccepted() {
    UserCreationDTO missingPassword =
        new UserCreationDTO("test@test.hu", "Alma1234", "Alma1234", "", false);
    String response = validationService.validatePasswordAndEmail(missingPassword);
    assertThat(response).contains("Az adatvédelmi tájékoztató elfogadása kötelező!");
  }

  @Test
  void validateTravellers() {
    Traveller validTraveller = TravellerFactory.createTraveller();

    TravellerWrapper validWrapper = new TravellerWrapper();
    validWrapper.setTravellers(java.util.List.of(validTraveller));

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDurations();

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

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDurations();

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

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDurations();

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

    FlightOfferDTO flightOffer = FlightOfferDTOFactory.createFlightOfferWithDurations();

    traveller.getDocuments().getFirst().setExpiryDate(LocalDate.now().minusYears(10).toString());
    String response = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response).contains("Az úti okmány lejárati dátuma hamarabb van, mint a kiállítása!");

    traveller.getDocuments().getFirst().setExpiryDate("invalid");
    String response1 = validationService.validateTravellers(wrapper, flightOffer);
    assertThat(response1).contains("Nem érvényes dátum formátum!");
  }

  @Test
  void validateOldPassword() {
    when(securityConfig.passwordEncoder()).thenReturn(passwordEncoder);
    UserCreationDTO missingOld = new UserCreationDTO("a@a.com", "p1", "p1", "", true);
    String response = validationService.validateOldPassword(missingOld, "stored");
    assertThat(response).contains("Jelenelgi jelszó");

    UserCreationDTO dto = new UserCreationDTO("u@u.com", "p1", "p1", "oldRaw", true);
    when(passwordEncoder.matches("oldRaw", "encodedStored")).thenReturn(true);
    String response1 = validationService.validateOldPassword(dto, "encodedStored");
    assertThat(response1).isEmpty();

    when(passwordEncoder.matches("oldRaw", "encodedStored")).thenReturn(false);
    String response2 = validationService.validateOldPassword(dto, "encodedStored");
    assertThat(response2).contains("A jelenlegi jelszó nem megfelelő");
  }
}
