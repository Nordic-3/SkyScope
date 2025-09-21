package com.szte.SkyScope.Utils;

import com.szte.SkyScope.Models.FlightOffers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlightOfferFormatter {

    public static void formatFlightOfferFields(List<FlightOffers> flightOffers) {
        flightOffers
                .stream()
                .flatMap(i -> i.getItineraries().stream())
                .forEach(itinerary -> {
                    if (itinerary.getSegments().size() > 1) {
                        itinerary.setLayoverTime(calculateLayoverTime(itinerary.getSegments()));
                    }
                });
    }

    private static List<String> calculateLayoverTime(List<FlightOffers.Segment> segments) {
        List<String> layoverTimes = new ArrayList<>();
        for (int i = 0; i < segments.size() - 1; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
            LocalDateTime arrival = LocalDateTime.parse(segments.get(i).getArrival().getAt(), formatter);
            LocalDateTime departure = LocalDateTime.parse(segments.get(i+1).getDeparture().getAt(), formatter);
            layoverTimes.add(formatDuration(Duration.between(arrival, departure).toString()));
        }
        return layoverTimes;
    }

    private static String formatDuration(String duration) {
        long hours =  Duration.parse(duration).toHours();
        long minutes =  Duration.parse(duration).minusHours(hours).toMinutes();
        return hours + " óra " + minutes + " perc";
    }
}
