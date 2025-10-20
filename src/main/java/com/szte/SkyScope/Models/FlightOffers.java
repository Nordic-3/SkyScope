package com.szte.SkyScope.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOffers {
  private List<Itinerary> itineraries = new ArrayList<>();
  private Price price = new Price();
  private List<TravelerPricing> travelerPricings = new ArrayList<>();
  private String id = "";
  private String type = "";
  private String source = "";
  private List<String> validatingAirlineCodes = new ArrayList<>();

  public FlightOffers() {}

  public FlightOffers(FlightOffers flightOffers) {
    this.itineraries = flightOffers.getItineraries().stream().map(Itinerary::new).toList();
    this.price = new Price(flightOffers.getPrice());
    this.travelerPricings =
        flightOffers.getTravelerPricings().stream().map(TravelerPricing::new).toList();
    this.id = flightOffers.getId();
    this.type = flightOffers.getType();
    this.source = flightOffers.getSource();
    this.validatingAirlineCodes.addAll(flightOffers.getValidatingAirlineCodes());
  }

  public List<String> getValidatingAirlineCodes() {
    return validatingAirlineCodes;
  }

  public void setValidatingAirlineCodes(List<String> validatingAirlineCodes) {
    this.validatingAirlineCodes = validatingAirlineCodes;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Itinerary> getItineraries() {
    return itineraries;
  }

  public void setItineraries(List<Itinerary> itineraries) {
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
  public static class Itinerary {

    private List<Segment> segments = new ArrayList<>();
    private String duration = "";
    private List<Duration> layoverTime = new ArrayList<>();

    public Itinerary() {}

    public Itinerary(Itinerary itinerary) {
      this.segments = itinerary.getSegments().stream().map(Segment::new).toList();
      this.duration = itinerary.getDuration();
      this.layoverTime = new ArrayList<>(itinerary.getLayoverTime());
    }

    public List<Duration> getLayoverTime() {
      return layoverTime;
    }

    public List<String> getFormattedLayoverTime() {
      return this.layoverTime.stream()
          .map(layoverTime -> FlightOfferFormatter.formatDuration(layoverTime.toString()))
          .toList();
    }

    public void setLayoverTime(List<Duration> layoverTime) {
      this.layoverTime = layoverTime;
    }

    public String getDuration() {
      return duration;
    }

    public void setDuration(String duration) {
      this.duration = duration;
    }

    public List<Segment> getSegments() {
      return segments;
    }

    public void setSegments(List<Segment> segments) {
      this.segments = segments;
    }

    public String getTransferNumber() {
      return " " + (segments.size() - 1) + " átszállás";
    }
  }

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

    public Segment() {}

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

    public boolean isCurrentlyFlying() {
      return isCurrentlyFlying;
    }

    public void setCurrentlyFlying(boolean currentlyFlying) {
      isCurrentlyFlying = currentlyFlying;
    }

    public String getCallSign() {
      return callSign;
    }

    public void setCallSign(String callSign) {
      this.callSign = callSign;
    }

    public String getCarrierName() {
      return carrierName;
    }

    public void setCarrierName(String carrierName) {
      this.carrierName = carrierName;
    }

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
      this.duration = duration;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FlightSchedule {

    private String iataCode = "";
    private String terminal = "";
    private String at = "";
    private String airportName = "";

    public FlightSchedule() {}

    public FlightSchedule(FlightSchedule flightSchedule) {
      this.iataCode = flightSchedule.getIataCode();
      this.terminal = flightSchedule.getTerminal();
      this.at = flightSchedule.getAt();
      this.airportName = flightSchedule.getAirportName();
    }

    public String getAirportName() {
      return airportName;
    }

    public void setAirportName(String airportName) {
      this.airportName = airportName;
    }

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
      this.at = at;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Operating {

    private String carrierCode = "";
    private String carrierName = "";

    public Operating() {}

    public Operating(Operating operating) {
      this.carrierCode = operating.getCarrierCode();
      this.carrierName = operating.getCarrierName();
    }

    public String getCarrierName() {
      return carrierName;
    }

    public void setCarrierName(String carrierName) {
      this.carrierName = carrierName;
    }

    public String getCarrierCode() {
      return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
      this.carrierCode = carrierCode;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Aircraft {

    private String code = "";
    private String name = "";

    public Aircraft() {}

    public Aircraft(Aircraft aircraft) {
      this.code = aircraft.getCode();
      this.name = aircraft.getName();
    }

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

    private String currency = "";
    private String total = "";

    public Price() {}

    public Price(Price price) {
      this.currency = price.getCurrency();
      this.total = price.getTotal();
    }

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

    private String travelerId = "";
    private String fareOption = "";
    private String travelerType = "";
    private String traveller = "";
    private Price price = new Price();
    private List<FareDetailsBySegment> fareDetailsBySegment = new ArrayList<>();

    public TravelerPricing() {}

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

    public String getTraveller() {
      return traveller;
    }

    public void setTraveller(String traveller) {
      this.traveller = traveller;
    }

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

    public List<FareDetailsBySegment> getFareDetailBySegmentId(String segmentId) {
      return fareDetailsBySegment.stream()
          .filter(details -> segmentId.equals(details.segmentId))
          .toList();
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FareDetailsBySegment {
    private String segmentId = "";
    private String cabin = "";
    private IncludedBags includedCheckedBags = new IncludedBags();
    private IncludedBags includedCabinBags = new IncludedBags();

    public FareDetailsBySegment() {}

    public FareDetailsBySegment(FareDetailsBySegment fareDetailsBySegment) {
      this.segmentId = fareDetailsBySegment.getSegmentId();
      this.cabin = fareDetailsBySegment.getCabin();
      this.includedCheckedBags = new IncludedBags(fareDetailsBySegment.getIncludedCheckedBags());
      this.includedCabinBags = new IncludedBags(fareDetailsBySegment.getIncludedCabinBags());
    }

    public IncludedBags getIncludedCabinBags() {
      return includedCabinBags;
    }

    public void setIncludedCabinBags(IncludedBags includedCabinBags) {
      this.includedCabinBags = includedCabinBags;
    }

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

    public IncludedBags getIncludedCheckedBags() {
      return includedCheckedBags;
    }

    public void setIncludedCheckedBags(IncludedBags includedCheckedBags) {
      this.includedCheckedBags = includedCheckedBags;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class IncludedBags {
    private String quantity = "0";

    public IncludedBags() {}

    public IncludedBags(IncludedBags includedBags) {
      this.quantity = includedBags.getQuantity();
    }

    public String getQuantity() {
      return quantity;
    }

    public void setQuantity(String quantity) {
      this.quantity = quantity;
    }
  }
}
