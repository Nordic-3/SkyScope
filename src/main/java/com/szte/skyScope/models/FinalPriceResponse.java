package com.szte.skyScope.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinalPriceResponse {

  private Data data;
  private Dictionaries dictionaries;

  @Setter
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {
    private String type;
    private List<FlightOffer> flightOffers;
  }

  @Setter
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FlightOffer {
    private String type;
    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean nonHomogeneous;
    private String lastTicketingDate;
    private List<Itinerary> itineraries;
    private Price price;
    private PricingOptions pricingOptions;
    private List<String> validatingAirlineCodes;
    private List<TravelerPricing> travelerPricings;
    private boolean paymentCardRequired;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Itinerary {
      private List<Segment> segments;

      @Setter
      @Getter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Segment {
        private String id;
        private String carrierCode;
        private String number;
        private String duration;
        private int numberOfStops;
        private DepartureArrival departure;
        private DepartureArrival arrival;
        private Aircraft aircraft;
        private Operating operating;

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DepartureArrival {
          private String iataCode;
          private String terminal;
          private String at;
        }

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Aircraft {
          private String code;
        }

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Operating {
          private String carrierCode;
        }
      }
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
      private String currency;
      private String total;
      private String base;
      private List<Fee> fees;
      private String grandTotal;
      private String billingCurrency;

      @Setter
      @Getter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Fee {
        private String amount;
        private String type;
      }
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PricingOptions {
      private List<String> fareType;
      private boolean includedCheckedBagsOnly;
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricing {
      private String travelerId;
      private String fareOption;
      private String travelerType;
      private Price price;
      private List<FareDetailsBySegment> fareDetailsBySegment;

      @Setter
      @Getter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class FareDetailsBySegment {
        private String segmentId;
        private String cabin;

        @JsonProperty("class")
        private String travelClass;

        private String fareBasis;
        private IncludedCheckedBags includedCheckedBags;

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IncludedCheckedBags {
          private int quantity;
        }
      }
    }
  }

  @Setter
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Dictionaries {
    private Map<String, Location> locations;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
      private String cityCode;
      private String countryCode;
    }
  }
}
