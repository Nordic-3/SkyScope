package com.szte.skyscope.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.factories.FlightOfferDTOFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;

class FlightOfferFormatterTest {

  @Test
  void formatFlightOfferFields_Success() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithTransferNumber();
    FlightOfferDTO.Itinerary itinerary = offer.getItineraries().getFirst();

    itinerary.setDuration("PT5H30M");

    FlightOfferDTO.Segment seg1 = itinerary.getSegments().getFirst();
    seg1.setDuration("PT2H");
    seg1.getDeparture().setAirportName("Budapest");
    seg1.getDeparture().setTerminal("2A");
    seg1.getArrival().setAirportName("Paris");
    seg1.getArrival().setTerminal("");

    FlightOfferDTO.Segment seg2 = itinerary.getSegments().get(1);
    seg2.setDuration("PT1H30M");
    seg2.getDeparture().setAirportName("Paris");
    seg2.getDeparture().setTerminal("3");
    seg2.getArrival().setAirportName("London");
    seg2.getArrival().setTerminal("5");

    List<FlightOfferDTO> offers = List.of(offer);

    List<FlightOfferDTO> result = FlightOfferFormatter.formatFlightOfferFields(offers);

    assertThat(result).isNotNull().hasSize(1);

    FlightOfferDTO formattedOffer = result.getFirst();

    assertThat(formattedOffer.getPrice().getTotal()).isEqualTo("100 000");

    FlightOfferDTO.Itinerary formattedItinerary = formattedOffer.getItineraries().getFirst();

    assertThat(formattedItinerary.getDuration()).isEqualTo("5 óra 30 perc");

    assertThat(formattedItinerary.getLayoverTime()).isNotNull().hasSize(1);
    assertThat(formattedItinerary.getLayoverTime().getFirst().toHours()).isEqualTo(3);

    FlightOfferDTO.Segment firstSegment = formattedItinerary.getSegments().getFirst();
    assertThat(firstSegment.getDuration()).isEqualTo("2 óra 0 perc");
    assertThat(firstSegment.getDeparture().getAirportName()).isEqualTo("Budapest 2A terminál");
    assertThat(firstSegment.getArrival().getAirportName()).isEqualTo("Paris");

    String expectedDateStr =
        LocalDate.now(ZoneId.of(Constants.ZONE_ID)).plusDays(1).toString().replace("-", ". ");
    assertThat(firstSegment.getDeparture().getAt()).isEqualTo(expectedDateStr + ". 09:00:00");
  }

  @Test
  void calculateLayoverTime_Success() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithTransferNumber();
    List<FlightOfferDTO.Segment> segments = offer.getItineraries().getFirst().getSegments();

    List<Duration> layovers = FlightOfferFormatter.calculateLayoverTime(segments);

    assertThat(layovers).hasSize(1);
    assertThat(layovers.getFirst().toHours()).isEqualTo(3);
  }

  @Test
  void formatDuration_Success() {
    String validDuration = "PT2H45M";

    String result = FlightOfferFormatter.formatDuration(validDuration);

    assertThat(result).isEqualTo("2 óra 45 perc");
  }

  @Test
  void formatDuration_Exception() {
    String invalidDuration = "HIBAS_IDOTARTAM";

    String result = FlightOfferFormatter.formatDuration(invalidDuration);

    assertThat(result).isEqualTo(invalidDuration);
  }

  @Test
  void formatPrice_Success() {
    String priceWithDecimal = "1534500.99";
    String priceWithoutDecimal = "75000";

    String result1 = FlightOfferFormatter.formatPrice(priceWithDecimal);
    String result2 = FlightOfferFormatter.formatPrice(priceWithoutDecimal);

    assertThat(result1).isEqualTo("1 534 500");
    assertThat(result2).isEqualTo("75 000");
  }

  @Test
  void formatPrice_Exception() {
    String invalidPrice = "NEM_SZAM";
    String result = FlightOfferFormatter.formatPrice(invalidPrice);

    assertThat(result).isEqualTo(invalidPrice);
  }

  @Test
  void formatAndSetSingleOfferDuration_Success() {
    FlightOfferDTO offer = FlightOfferDTOFactory.createFlightOfferWithBritishAirways();
    FlightOfferDTO.Itinerary itinerary = offer.getItineraries().getFirst();

    itinerary.getSegments().getFirst().setDuration("PT1H30M");
    itinerary.getSegments().getFirst().getDeparture().setAt("2026-08-10T10:00:00");
    itinerary.getSegments().getFirst().getArrival().setAt("2026-08-10T11:30:00");

    assertDoesNotThrow(() -> FlightOfferFormatter.formatAndSetSingleOfferDuration(offer));

    assertThat(itinerary.getDuration()).isEqualTo("1 óra 30 perc");

    FlightOfferDTO.Segment firstSegment = itinerary.getSegments().getFirst();
    assertThat(firstSegment.getDuration()).isEqualTo("1 óra 30 perc");
    assertThat(firstSegment.getDeparture().getAt()).isEqualTo("2026. 08. 10. 10:00:00");
  }
}
