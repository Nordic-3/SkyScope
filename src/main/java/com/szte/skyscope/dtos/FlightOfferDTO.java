package com.szte.skyscope.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.szte.skyscope.utils.FlightOfferFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOfferDTO {
  private List<Itinerary> itineraries = new ArrayList<>();
  private Price price = new Price();
  private List<TravelerPricing> travelerPricings = new ArrayList<>();
  private String id = "";
  private String type = "";
  private String source = "";
  private List<String> validatingAirlineCodes = new ArrayList<>();

  public FlightOfferDTO(FlightOfferDTO flightOffer) {
    this.itineraries = flightOffer.getItineraries().stream().map(Itinerary::new).toList();
    this.price = new Price(flightOffer.getPrice());
    this.travelerPricings =
        flightOffer.getTravelerPricings().stream().map(TravelerPricing::new).toList();
    this.id = flightOffer.getId();
    this.type = flightOffer.getType();
    this.source = flightOffer.getSource();
    this.validatingAirlineCodes.addAll(flightOffer.getValidatingAirlineCodes());
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Itinerary {

    private List<Segment> segments = new ArrayList<>();
    private String duration = "";
    private List<Duration> layoverTime = new ArrayList<>();

    public Itinerary(Itinerary itinerary) {
      this.segments = itinerary.getSegments().stream().map(Segment::new).toList();
      this.duration = itinerary.getDuration();
      this.layoverTime = new ArrayList<>(itinerary.getLayoverTime());
    }

    public List<String> getFormattedLayoverTime() {
      return this.layoverTime.stream()
          .map(time -> FlightOfferFormatter.formatDuration(time.toString()))
          .toList();
    }

    public String getTransferNumber() {
      return " " + (segments.size() - 1) + " átszállás";
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Segment {

    private String id = "";
    private FlightSchedule departure = new FlightSchedule();
    private FlightSchedule arrival = new FlightSchedule();
    private String carrierCode = "";
    private String carrierName = "";
    private String number = "";
    private Aircraft aircraft = new Aircraft();
    private Operating operating = new Operating();
    private String duration = "";
    private String callSign = "";
    private boolean isCurrentlyFlying = false;

    public Segment(Segment segment) {
      this.id = segment.getId();
      this.departure = new FlightSchedule(segment.getDeparture());
      this.arrival = new FlightSchedule(segment.getArrival());
      this.carrierCode = segment.getCarrierCode();
      this.carrierName = segment.getCarrierName();
      this.number = segment.getNumber();
      this.aircraft = new Aircraft(segment.getAircraft());
      this.operating = new Operating(segment.getOperating());
      this.duration = segment.getDuration();
      this.callSign = segment.getCallSign();
      this.isCurrentlyFlying = segment.isCurrentlyFlying();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FlightSchedule {
    private String iataCode = "";
    private String terminal = "";
    private String at = "";
    private String airportName = "";

    public FlightSchedule(FlightSchedule flightSchedule) {
      this.iataCode = flightSchedule.getIataCode();
      this.terminal = flightSchedule.getTerminal();
      this.at = flightSchedule.getAt();
      this.airportName = flightSchedule.getAirportName();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Operating {

    private String carrierCode = "";
    private String carrierName = "";

    public Operating(Operating operating) {
      this.carrierCode = operating.getCarrierCode();
      this.carrierName = operating.getCarrierName();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Aircraft {

    private String code = "";
    private String name = "";

    public Aircraft(Aircraft aircraft) {
      this.code = aircraft.getCode();
      this.name = aircraft.getName();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Price {
    private String currency = "";
    private String total = "";

    public Price(Price price) {
      this.currency = price.getCurrency();
      this.total = price.getTotal();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TravelerPricing {

    private String travelerId = "";
    private String fareOption = "";
    private String travelerType = "";
    private String traveller = "";
    private Price price = new Price();
    private List<FareDetailsBySegment> fareDetailsBySegment = new ArrayList<>();

    public TravelerPricing(TravelerPricing travelerPricing) {
      this.travelerId = travelerPricing.getTravelerId();
      this.fareOption = travelerPricing.getFareOption();
      this.travelerType = travelerPricing.getTravelerType();
      this.traveller = travelerPricing.getTraveller();
      this.price = new Price(travelerPricing.getPrice());
      this.fareDetailsBySegment =
          travelerPricing.getFareDetailsBySegment().stream()
              .map(FareDetailsBySegment::new)
              .toList();
    }

    public List<FareDetailsBySegment> getFareDetailBySegmentId(String segmentId) {
      return fareDetailsBySegment.stream()
          .filter(details -> segmentId.equals(details.segmentId))
          .toList();
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FareDetailsBySegment {
    private String segmentId = "";
    private String cabin = "";
    private IncludedBags includedCheckedBags = new IncludedBags();
    private IncludedBags includedCabinBags = new IncludedBags();

    public FareDetailsBySegment(FareDetailsBySegment fareDetailsBySegment) {
      this.segmentId = fareDetailsBySegment.getSegmentId();
      this.cabin = fareDetailsBySegment.getCabin();
      this.includedCheckedBags = new IncludedBags(fareDetailsBySegment.getIncludedCheckedBags());
      this.includedCabinBags = new IncludedBags(fareDetailsBySegment.getIncludedCabinBags());
    }
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class IncludedBags {
    private String quantity = "0";

    public IncludedBags(IncludedBags includedBags) {
      this.quantity = includedBags.getQuantity();
    }
  }
}
