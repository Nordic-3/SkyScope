package com.szte.SkyScope.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOffers {
    private List<Itinerariy> itineraries;
    private Price price;
    private List<TravelerPricing> travelerPricings;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Itinerariy> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerariy> itineraries) {
        this.itineraries = itineraries;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<TravelerPricing> getTravelerPricings() {
        return travelerPricings;
    }

    public void setTravelerPricings(List<TravelerPricing> travelerPricings) {
        this.travelerPricings = travelerPricings;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Itinerariy {

        private List<Segment> segments;
        private String duration;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = formatDuration(duration);
        }

        public List<Segment> getSegments() {
            return segments;
        }

        public void setSegments(List<Segment> segments) {
            this.segments = segments;
        }

        public static String formatDuration(String duration) {
            long hours =  Duration.parse(duration).toHours();
            long minutes =  Duration.parse(duration).minusHours(hours).toMinutes();
            return hours + " óra " + minutes + " perc";
        }

        public String getTransferNumber() {
            return " " + (segments.size() - 1)  + " átszállás";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {

        private String id;
        private FlightSchedule departure;
        private FlightSchedule arrival;
        private String carrierCode;
        private String number;
        private Aircraft aircraft;
        private Operating operating;
        private String duration;
        private int numberOfStops;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FlightSchedule getDeparture() {
            return departure;
        }

        public void setDeparture(FlightSchedule departure) {
            this.departure = departure;
        }

        public FlightSchedule getArrival() {
            return arrival;
        }

        public void setArrival(FlightSchedule arrival) {
            this.arrival = arrival;
        }

        public String getCarrierCode() {
            return carrierCode;
        }

        public void setCarrierCode(String carrierCode) {
            this.carrierCode = carrierCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public Aircraft getAircraft() {
            return aircraft;
        }

        public void setAircraft(Aircraft aircraft) {
            this.aircraft = aircraft;
        }

        public Operating getOperating() {
            return operating;
        }

        public void setOperating(Operating operating) {
            this.operating = operating;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = Itinerariy.formatDuration(duration);
        }

        public int getNumberOfStops() {
            return numberOfStops;
        }

        public void setNumberOfStops(int numberOfStops) {
            this.numberOfStops = numberOfStops;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightSchedule {

        private String iataCode;
        private String terminal;
        private String at;

        public String getIataCode() {
            return iataCode;
        }

        public void setIataCode(String iataCode) {
            this.iataCode = iataCode;
        }

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public String getAt() {
            return at;
        }

        public void setAt(String at) {
            this.at = at.split("T")[0].replace("-", ". ") + ". " + at.split("T")[1];
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operating {

        private String carrierCode;

        public String getCarrierCode() {
            return carrierCode;
        }

        public void setCarrierCode(String carrierCode) {
            this.carrierCode = carrierCode;
        }
    }

    public static class Aircraft {

        private String code;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {

        private String currency;
        private String total;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricing {

        private String travelerId;
        private String fareOption;
        private String travelerType;
        private Price price;
        private List<FareDetailsBySegment> fareDetailsBySegment;

        public List<FareDetailsBySegment> getFareDetailsBySegment() {
            return fareDetailsBySegment;
        }

        public void setFareDetailsBySegment(List<FareDetailsBySegment> fareDetailsBySegment) {
            this.fareDetailsBySegment = fareDetailsBySegment;
        }

        public String getTravelerId() {
            return travelerId;
        }

        public void setTravelerId(String travelerId) {
            this.travelerId = travelerId;
        }

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public String getTravelerType() {
            return travelerType;
        }

        public void setTravelerType(String travelerType) {
            this.travelerType = travelerType;
        }

        public String getFareOption() {
            return fareOption;
        }

        public void setFareOption(String fareOption) {
            this.fareOption = fareOption;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FareDetailsBySegment {
        private String segmentId;
        private String cabin;
        private IncludedCheckedBags includedCheckedBags;

        public String getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(String segmentId) {
            this.segmentId = segmentId;
        }

        public String getCabin() {
            return cabin;
        }

        public void setCabin(String cabin) {
            this.cabin = cabin;
        }

        public IncludedCheckedBags getIncludedCheckedBags() {
            return includedCheckedBags == null ? new IncludedCheckedBags() : includedCheckedBags;
        }

        public void setIncludedCheckedBags(IncludedCheckedBags includedCheckedBags) {
            this.includedCheckedBags = includedCheckedBags;
        }
    }

    public static class IncludedCheckedBags {
        private String quantity = "0";

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }
}
