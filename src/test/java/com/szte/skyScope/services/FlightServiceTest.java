package com.szte.skyScope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.factories.FlightOfferDTOFactory;
import com.szte.skyScope.factories.FlightSearchFactory;
import com.szte.skyScope.models.*;
import com.szte.skyScope.services.impl.FlightServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
  @Mock SearchStore searchStore;
  @Mock CityService cityService;
  @Mock CachedApiCalls cachedApiCalls;
  @Mock FlightServiceProvider flightServiceProvider;
  @InjectMocks FlightServiceImpl flightService;

  @Test
  void getToken() {
    AmadeusApiCred apiCred = new AmadeusApiCred();
    apiCred.setAccess_token("access_token");
    when(cachedApiCalls.getAmadeusApiCred()).thenReturn(apiCred);
    assertThat(flightService.getToken()).isEqualTo("access_token");
  }

  @Test
  void setIataCodes() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    City mockedCity = new City();
    mockedCity.setName("Budapest");
    when(cityService.getCity("Budapest")).thenReturn(mockedCity);
    City mockedCity1 = new City();
    mockedCity1.setName("London");
    when(cityService.getCity("London")).thenReturn(mockedCity1);
    when(cachedApiCalls.getIataCode("Budapest", "access_token")).thenReturn("BUD");
    when(cachedApiCalls.getIataCode("London", "access_token")).thenReturn("LON");
    flightService.setIataCodes(flightSearch, "access_token");
    assertThat(flightSearch.getOriginCityIata()).isEqualTo("BUD");
    assertThat(flightSearch.getDestinationCityIata()).isEqualTo("LON");
  }

  @Test
  void getFlightOffers() {
    FlightSearch flightSearch = FlightSearchFactory.createValidFlightSearch();
    when(flightServiceProvider.getFlightOffers(flightSearch, "access_token"))
        .thenReturn(
            "{\"meta\":{\"count\":1,\"links\":{\"self\":\"self\"}},\"data\":[{\"type\":\"flight-offer\",\"id\":\"1\",\"source\":\"GDS\",\"instantTicketingRequired\":false,\"nonHomogeneous\":false,\"oneWay\":false,\"lastTicketingDate\":\"2026-04-01\",\"numberOfBookableSeats\":9,\"itineraries\":[{\"duration\":\"PT2H35M\",\"segments\":[{\"departure\":{\"iataCode\":\"BUD\",\"terminal\":\"2B\",\"at\":\"2026-05-10T06:15:00\"},\"arrival\":{\"iataCode\":\"LTN\",\"at\":\"2026-05-10T07:50:00\"},\"carrierCode\":\"W6\",\"number\":\"2201\",\"aircraft\":{\"code\":\"321\"},\"operating\":{\"carrierCode\":\"W6\"},\"duration\":\"PT2H35M\",\"id\":\"1\",\"numberOfStops\":0,\"blacklistedInEU\":false}]},{\"duration\":\"PT2H40M\",\"segments\":[{\"departure\":{\"iataCode\":\"LTN\",\"at\":\"2026-05-17T20:30:00\"},\"arrival\":{\"iataCode\":\"BUD\",\"terminal\":\"2B\",\"at\":\"2026-05-18T00:10:00\"},\"carrierCode\":\"W6\",\"number\":\"2202\",\"aircraft\":{\"code\":\"321\"},\"operating\":{\"carrierCode\":\"W6\"},\"duration\":\"PT2H40M\",\"id\":\"2\",\"numberOfStops\":0,\"blacklistedInEU\":false}]}],\"price\":{\"currency\":\"HUF\",\"total\":\"45900.00\",\"base\":\"32000.00\",\"grandTotal\":\"45900.00\"},\"pricingOptions\":{\"fareType\":[\"PUBLISHED\"],\"includedCheckedBagsOnly\":false},\"validatingAirlineCodes\":[\"W6\"],\"travelerPricings\":[{\"travelerId\":\"1\",\"fareOption\":\"STANDARD\",\"travelerType\":\"ADULT\",\"price\":{\"currency\":\"HUF\",\"total\":\"45900.00\",\"base\":\"32000.00\"},\"fareDetailsBySegment\":[{\"segmentId\":\"1\",\"cabin\":\"ECONOMY\",\"class\":\"W\",\"includedCheckedBags\":{\"quantity\":0}},{\"segmentId\":\"2\",\"cabin\":\"ECONOMY\",\"class\":\"W\",\"includedCheckedBags\":{\"quantity\":0}}]}]}],\"dictionaries\":{\"locations\":{\"BUD\":{\"cityCode\":\"BUD\",\"countryCode\":\"HU\"},\"LTN\":{\"cityCode\":\"LON\",\"countryCode\":\"GB\"}},\"aircraft\":{\"321\":\"AIRBUS A321\"},\"currencies\":{\"HUF\":\"HUNGARIAN FORINT\"},\"carriers\":{\"W6\":\"WIZZ AIR\"}}}");
    SearchData searchData = new SearchData();
    when(searchStore.getSearchDatas("searchId")).thenReturn(searchData);
    CompletableFuture<List<FlightOfferDTO>> offersFuture =
        flightService.getFlightOffers(flightSearch, "access_token", "searchId");
    List<FlightOfferDTO> result = offersFuture.join();
    assertThat(
            result
                .getFirst()
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getDeparture()
                .getIataCode())
        .isEqualTo("BUD");
    assertThat(searchData.getAircraftDictionary()).isNotNull();
    assertThat(searchData.getLocationDictionary()).isNotNull();
    assertThat(searchData.getCarrierDictionary()).isNotNull();
  }

  @Test
  void setAircraftType() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();
    Map<String, String> aircraftDictionary = Map.of("B747", "BOEING 747");
    flightService.setAircraftType(Arrays.asList(offer), aircraftDictionary);
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().getAircraft().getName())
        .isEqualTo("Boeing 747");
  }

  @Test
  void setCarrierNames() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();
    Map<String, String> carrierDictionary = Map.of("UA", "United Airlines");
    flightService.setCarrierNames(Arrays.asList(offer), carrierDictionary);
    assertThat(
            offer
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("United Airlines");
  }

  @Test
  void getAirportNamesByItsIata() {
    when(cachedApiCalls.getAirportName("BUD", "access_token")).thenReturn("Budapest Airport");
    Map<String, String> result =
        flightService.getAirportNamesByItsIata(Map.of("BUD", new Location()), "access_token");
    assertThat(result.get("BUD")).isEqualTo("Budapest Airport");
  }

  @Test
  void setAirportNames() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();
    offer.getItineraries().getFirst().getSegments().getFirst().getDeparture().setIataCode("BUD");
    Map<String, String> airportNamesByIata = Map.of("BUD", "Budapest Airport");
    flightService.setAirportNames(Arrays.asList(offer), airportNamesByIata);
    assertThat(
            offer
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getDeparture()
                .getAirportName())
        .isEqualTo("Budapest Airport");
  }

  @Test
  void setFlightOffersAttributes() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();
    offer.getItineraries().getFirst().getSegments().getFirst().getDeparture().setIataCode("BUD");
    SearchData searchData = new SearchData();
    searchData.setAircraftDictionary(Map.of("B747", "BOEING 747"));
    searchData.setCarrierDictionary(Map.of("UA", "United Airlines"));
    searchData.setLocationDictionary(Map.of("BUD", new Location()));
    when(searchStore.getSearchDatas("searchId")).thenReturn(searchData);
    when(cachedApiCalls.getAirportName("BUD", "access_token")).thenReturn("Budapest Airport");
    flightService.setFlightOffersAttributes(Arrays.asList(offer), "searchId", "access_token");
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().getAircraft().getName())
        .isEqualTo("Boeing 747");
    assertThat(
            offer
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getOperating()
                .getCarrierName())
        .isEqualTo("United Airlines");
    assertThat(
            offer
                .getItineraries()
                .getFirst()
                .getSegments()
                .getFirst()
                .getDeparture()
                .getAirportName())
        .isEqualTo("Budapest Airport");
    assertThat(offer.getTravelerPricings().getFirst().getTraveller()).isEqualTo("Felnőtt");
  }

  @Test
  void getIcaoCodes() {
    when(flightServiceProvider.getIcaoCodes(Map.of("UA", "United Airlines"), "access_token"))
        .thenReturn(Map.of("UA", "UAL"));
    Map<String, String> result =
        flightService.getIcaoCodes(Map.of("UA", "United Airlines"), "access_token");
    assertThat(result.get("UA")).isEqualTo("UAL");
  }

  @Test
  void setCallsigns() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithDepartureAndArrival();
    offer.getItineraries().getFirst().getSegments().getFirst().getOperating().setCarrierCode("UA");
    offer.getItineraries().getFirst().getSegments().getFirst().setNumber("123");
    Map<String, String> icaoCodes = Map.of("UA", "UAL");
    flightService.setCallsigns(Arrays.asList(offer), icaoCodes);
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().getCallSign())
        .isEqualTo("UAL123");
  }

  @Test
  void setIsCurrentlyFlying() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createOfferWithCallsign();
    Map<String, Plane> planePositions = Map.of("LH123", new Plane());
    flightService.setIsCurrentlyFlying(Arrays.asList(offer), planePositions);
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().isCurrentlyFlying())
        .isTrue();
  }

  @Test
  void setIsCurrentlyFlying_planePositionNull() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createOfferWithCallsign();
    flightService.setIsCurrentlyFlying(Arrays.asList(offer), null);
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().isCurrentlyFlying())
        .isFalse();
  }

  @Test
  void setIsCurrentlyFlying_notFlying() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createOfferWithCallsign();
    Map<String, Plane> planePositions = Map.of("BA123", new Plane());
    flightService.setIsCurrentlyFlying(Arrays.asList(offer), planePositions);
    assertThat(offer.getItineraries().getFirst().getSegments().getFirst().isCurrentlyFlying())
        .isFalse();
  }
}
