package com.szte.SkyScope.Utils;

import com.szte.SkyScope.Models.FlightOffers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightOfferFormatter {

    public static void formatFlightOfferFields(List<FlightOffers> flightOffers) {
        flightOffers.forEach(flightOffer -> {
            flightOffer.getItineraries().forEach(itinerary -> {
                if (itinerary.getSegments().size() > 1) {
                    itinerary.setLayoverTime(calculateLayoverTime(itinerary.getSegments()));
                }
                itinerary.setDuration(formatDuration(itinerary.getDuration()));

                itinerary.getSegments().forEach(FlightOfferFormatter::formatAndSetSegmentData);
            });
            formatPrice(flightOffer);
        });
    }

    private static List<String> calculateLayoverTime(List<FlightOffers.Segment> segments) {
        List<String> layoverTimes = new ArrayList<>();
        for (int i = 0; i < segments.size() - 1; i++) {
            LocalDateTime arrival = LocalDateTime.parse(segments.get(i).getArrival().getAt());
            LocalDateTime departure = LocalDateTime.parse(segments.get(i+1).getDeparture().getAt());
            layoverTimes.add(formatDuration(Duration.between(arrival, departure).toString()));
        }
        return layoverTimes;
    }

    private static String formatDuration(String duration) {
        try {
            long hours =  Duration.parse(duration).toHours();
            long minutes =  Duration.parse(duration).minusHours(hours).toMinutes();
            return hours + " óra " + minutes + " perc";
        } catch (Exception e) {
            e.printStackTrace();
            return duration;
        }
    }

    private static void formatPrice(FlightOffers flightOffers) {
       try {
           DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
           decimalFormatSymbol.setGroupingSeparator(' ');
           flightOffers.getPrice().setTotal(
                   new DecimalFormat("###,###,###", decimalFormatSymbol)
                           .format(Integer.parseInt(flightOffers.getPrice().getTotal().split("\\.")[0])));
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    private static void formatAndSetSegmentData(FlightOffers.Segment segment) {
        segment.setDuration(formatDuration(segment.getDuration()));

        segment.getDeparture().setAirportName(formatAirportName(
                segment.getDeparture().getAirportName(),
                segment.getDeparture().getTerminal()
        ));
        segment.getArrival().setAirportName(formatAirportName(
                segment.getArrival().getAirportName(),
                segment.getArrival().getTerminal()
        ));

        segment.getDeparture().setAt(
                formatTime(segment.getDeparture().getAt())
        );
        segment.getArrival().setAt(
                formatTime(segment.getArrival().getAt()));
    }

    private static String formatAirportName(String airportName, String terminal) {
        return airportName + (terminal != null ? " " + terminal + " terminál" : "");
    }

    private static String formatTime(String time) {
        try {
            return time.split("T")[0].replace("-", ". ") + ". " + time.split("T")[1];
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
    }
}
