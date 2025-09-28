package com.szte.SkyScope.Utils;

import com.szte.SkyScope.Models.FlightOffers;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightOfferFormatter {

  private static final Logger logger = Logger.getLogger(FlightOfferFormatter.class.getName());

  public static List<FlightOffers> formatFlightOfferFields(List<FlightOffers> flightOffers) {
    List<FlightOffers> formattedFlightOffers =
        flightOffers.stream().map(FlightOffers::new).toList();
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

  public static List<String> calculateLayoverTime(List<FlightOffers.Segment> segments) {
    List<String> layoverTimes = new ArrayList<>();
    for (int i = 0; i < segments.size() - 1; i++) {
      LocalDateTime arrival = LocalDateTime.parse(segments.get(i).getArrival().getAt());
      LocalDateTime departure = LocalDateTime.parse(segments.get(i + 1).getDeparture().getAt());
      layoverTimes.add(formatDuration(Duration.between(arrival, departure).toString()));
    }
    return layoverTimes;
  }

  public static String formatDuration(String duration) {
    try {
      long hours = Duration.parse(duration).toHours();
      long minutes = Duration.parse(duration).minusHours(hours).toMinutes();
      return hours + " óra " + minutes + " perc";
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return duration;
    }
  }

  public static String formatPrice(String price) {
    try {
      DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
      decimalFormatSymbol.setGroupingSeparator(' ');
      return new DecimalFormat("###,###,###", decimalFormatSymbol)
          .format(Integer.parseInt(price.split("\\.")[0]));
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
    return price;
  }

  private static void formatAndSetPrice(FlightOffers flightOffers) {
    flightOffers.getPrice().setTotal(formatPrice(flightOffers.getPrice().getTotal()));
  }

  private static void formatAndSetSegmentData(FlightOffers.Segment segment) {
    segment.setDuration(formatDuration(segment.getDuration()));

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

    segment.getDeparture().setAt(formatTime(segment.getDeparture().getAt()));
    segment.getArrival().setAt(formatTime(segment.getArrival().getAt()));
  }

  private static String formatAirportName(String airportName, String terminal) {
    return airportName + (!terminal.isEmpty() ? " " + terminal + " terminál" : "");
  }

  private static String formatTime(String time) {
    try {
      return time.split("T")[0].replace("-", ". ") + ". " + time.split("T")[1];
    } catch (Exception exception) {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
      return time;
    }
  }
}
