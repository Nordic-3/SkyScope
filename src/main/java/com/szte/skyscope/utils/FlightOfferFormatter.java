package com.szte.skyscope.utils;

import com.szte.skyscope.dtos.FlightOfferDTO;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class FlightOfferFormatter {

  public List<FlightOfferDTO> formatFlightOfferFields(List<FlightOfferDTO> flightOffers) {
    List<FlightOfferDTO> formattedFlightOffers =
        flightOffers.stream().map(FlightOfferDTO::new).toList();
    formattedFlightOffers.forEach(
        flightOffer -> {
          flightOffer
              .getItineraries()
              .forEach(
                  itinerary -> {
                    if (itinerary.getSegments().size() > 1) {
                      itinerary.setLayoverTime(calculateLayoverTime(itinerary.getSegments()));
                    }
                    itinerary.setDuration(formatDuration(itinerary.getDuration()));

                    itinerary.getSegments().forEach(FlightOfferFormatter::formatAndSetSegmentData);
                  });
          formatAndSetPrice(flightOffer);
        });
    return formattedFlightOffers;
  }

  public List<Duration> calculateLayoverTime(List<FlightOfferDTO.Segment> segments) {
    List<Duration> layoverTimes = new ArrayList<>();
    for (int i = 0; i < segments.size() - 1; i++) {
      ZonedDateTime arrival =
          LocalDateTime.parse(segments.get(i).getArrival().getAt())
              .atZone(ZoneId.of(Constants.ZONE_ID));
      ZonedDateTime departure =
          LocalDateTime.parse(segments.get(i + 1).getDeparture().getAt())
              .atZone(ZoneId.of(Constants.ZONE_ID));
      layoverTimes.add(Duration.between(arrival, departure));
    }
    return layoverTimes;
  }

  public String formatDuration(String duration) {
    try {
      long hours = Duration.parse(duration).toHours();
      long minutes = Duration.parse(duration).minusHours(hours).toMinutes();
      return hours + " óra " + minutes + " perc";
    } catch (Exception exception) {
      log.error("Error while formatting duration {}", exception.getMessage(), exception);
      return duration;
    }
  }

  public String formatPrice(String price) {
    try {
      DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
      decimalFormatSymbol.setGroupingSeparator(' ');
      return new DecimalFormat("###,###,###", decimalFormatSymbol)
          .format(Integer.parseInt(price.split("\\.")[0]));
    } catch (Exception exception) {
      log.error("Error while formatting price {}", exception.getMessage(), exception);
    }
    return price;
  }

  public void formatAndSetSingleOfferDuration(FlightOfferDTO flightOffer) {
    flightOffer
        .getItineraries()
        .forEach(
            itinerary -> {
              itinerary.setDuration(formatDuration(itinerary.getDuration()));
              itinerary.getSegments().forEach(FlightOfferFormatter::formatAndSetSegmentDurations);
            });
  }

  private void formatAndSetPrice(FlightOfferDTO flightOffer) {
    flightOffer.getPrice().setTotal(formatPrice(flightOffer.getPrice().getTotal()));
  }

  private void formatAndSetSegmentData(FlightOfferDTO.Segment segment) {
    segment
        .getDeparture()
        .setAirportName(
            formatAirportName(
                segment.getDeparture().getAirportName(), segment.getDeparture().getTerminal()));
    segment
        .getArrival()
        .setAirportName(
            formatAirportName(
                segment.getArrival().getAirportName(), segment.getArrival().getTerminal()));
    formatAndSetSegmentDurations(segment);
  }

  private void formatAndSetSegmentDurations(FlightOfferDTO.Segment segment) {
    segment.setDuration(formatDuration(segment.getDuration()));
    segment.getDeparture().setAt(formatTime(segment.getDeparture().getAt()));
    segment.getArrival().setAt(formatTime(segment.getArrival().getAt()));
  }

  private String formatAirportName(String airportName, String terminal) {
    return airportName + (!terminal.isEmpty() ? " " + terminal + " terminál" : "");
  }

  private String formatTime(String time) {
    try {
      return time.split("T")[0].replace("-", ". ") + ". " + time.split("T")[1];
    } catch (Exception exception) {
      log.error("Error while formatting time {}", exception.getMessage(), exception);
      return time;
    }
  }
}
