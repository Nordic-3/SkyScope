package com.szte.skyScope.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinalPriceResponse {

  private Data data;
  private Dictionaries dictionaries;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public Dictionaries getDictionaries() {
    return dictionaries;
  }

  public void setDictionaries(Dictionaries dictionaries) {
    this.dictionaries = dictionaries;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {
    private String type;
    private List<FlightOffer> flightOffers;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<FlightOffer> getFlightOffers() {
      return flightOffers;
    }

    public void setFlightOffers(List<FlightOffer> flightOffers) {
      this.flightOffers = flightOffers;
    }
  }

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

    public boolean isPaymentCardRequired() {
      return paymentCardRequired;
    }

    public void setPaymentCardRequired(boolean paymentCardRequired) {
      this.paymentCardRequired = paymentCardRequired;
    }

    public List<TravelerPricing> getTravelerPricings() {
      return travelerPricings;
    }

    public void setTravelerPricings(List<TravelerPricing> travelerPricings) {
      this.travelerPricings = travelerPricings;
    }

    public List<String> getValidatingAirlineCodes() {
      return validatingAirlineCodes;
    }

    public void setValidatingAirlineCodes(List<String> validatingAirlineCodes) {
      this.validatingAirlineCodes = validatingAirlineCodes;
    }

    public PricingOptions getPricingOptions() {
      return pricingOptions;
    }

    public void setPricingOptions(PricingOptions pricingOptions) {
      this.pricingOptions = pricingOptions;
    }

    public Price getPrice() {
      return price;
    }

    public void setPrice(Price price) {
      this.price = price;
    }

    public List<Itinerary> getItineraries() {
      return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
      this.itineraries = itineraries;
    }

    public String getLastTicketingDate() {
      return lastTicketingDate;
    }

    public void setLastTicketingDate(String lastTicketingDate) {
      this.lastTicketingDate = lastTicketingDate;
    }

    public boolean isNonHomogeneous() {
      return nonHomogeneous;
    }

    public void setNonHomogeneous(boolean nonHomogeneous) {
      this.nonHomogeneous = nonHomogeneous;
    }

    public boolean isInstantTicketingRequired() {
      return instantTicketingRequired;
    }

    public void setInstantTicketingRequired(boolean instantTicketingRequired) {
      this.instantTicketingRequired = instantTicketingRequired;
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

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Itinerary {
      private List<Segment> segments;

      public List<Segment> getSegments() {
        return segments;
      }

      public void setSegments(List<Segment> segments) {
        this.segments = segments;
      }

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

        public String getId() {
          return id;
        }

        public void setId(String id) {
          this.id = id;
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

        public String getDuration() {
          return duration;
        }

        public void setDuration(String duration) {
          this.duration = duration;
        }

        public int getNumberOfStops() {
          return numberOfStops;
        }

        public void setNumberOfStops(int numberOfStops) {
          this.numberOfStops = numberOfStops;
        }

        public DepartureArrival getDeparture() {
          return departure;
        }

        public void setDeparture(DepartureArrival departure) {
          this.departure = departure;
        }

        public DepartureArrival getArrival() {
          return arrival;
        }

        public void setArrival(DepartureArrival arrival) {
          this.arrival = arrival;
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

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DepartureArrival {
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
            this.at = at;
          }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Aircraft {
          private String code;

          public String getCode() {
            return code;
          }

          public void setCode(String code) {
            this.code = code;
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
      }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
      private String currency;
      private String total;
      private String base;
      private List<Fee> fees;
      private String grandTotal;
      private String billingCurrency;

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

      public String getBase() {
        return base;
      }

      public void setBase(String base) {
        this.base = base;
      }

      public List<Fee> getFees() {
        return fees;
      }

      public void setFees(List<Fee> fees) {
        this.fees = fees;
      }

      public String getGrandTotal() {
        return grandTotal;
      }

      public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
      }

      public String getBillingCurrency() {
        return billingCurrency;
      }

      public void setBillingCurrency(String billingCurrency) {
        this.billingCurrency = billingCurrency;
      }

      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Fee {
        private String amount;
        private String type;

        public String getAmount() {
          return amount;
        }

        public void setAmount(String amount) {
          this.amount = amount;
        }

        public String getType() {
          return type;
        }

        public void setType(String type) {
          this.type = type;
        }
      }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PricingOptions {
      private List<String> fareType;
      private boolean includedCheckedBagsOnly;

      public List<String> getFareType() {
        return fareType;
      }

      public void setFareType(List<String> fareType) {
        this.fareType = fareType;
      }

      public boolean isIncludedCheckedBagsOnly() {
        return includedCheckedBagsOnly;
      }

      public void setIncludedCheckedBagsOnly(boolean includedCheckedBagsOnly) {
        this.includedCheckedBagsOnly = includedCheckedBagsOnly;
      }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricing {
      private String travelerId;
      private String fareOption;
      private String travelerType;
      private Price price;
      private List<FareDetailsBySegment> fareDetailsBySegment;

      public String getTravelerId() {
        return travelerId;
      }

      public void setTravelerId(String travelerId) {
        this.travelerId = travelerId;
      }

      public String getFareOption() {
        return fareOption;
      }

      public void setFareOption(String fareOption) {
        this.fareOption = fareOption;
      }

      public String getTravelerType() {
        return travelerType;
      }

      public void setTravelerType(String travelerType) {
        this.travelerType = travelerType;
      }

      public Price getPrice() {
        return price;
      }

      public void setPrice(Price price) {
        this.price = price;
      }

      public List<FareDetailsBySegment> getFareDetailsBySegment() {
        return fareDetailsBySegment;
      }

      public void setFareDetailsBySegment(List<FareDetailsBySegment> fareDetailsBySegment) {
        this.fareDetailsBySegment = fareDetailsBySegment;
      }

      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class FareDetailsBySegment {
        private String segmentId;
        private String cabin;

        @JsonProperty("class")
        private String travelClass;

        private String fareBasis;
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

        public String getTravelClass() {
          return travelClass;
        }

        public void setTravelClass(String travelClass) {
          this.travelClass = travelClass;
        }

        public String getFareBasis() {
          return fareBasis;
        }

        public void setFareBasis(String fareBasis) {
          this.fareBasis = fareBasis;
        }

        public IncludedCheckedBags getIncludedCheckedBags() {
          return includedCheckedBags;
        }

        public void setIncludedCheckedBags(IncludedCheckedBags includedCheckedBags) {
          this.includedCheckedBags = includedCheckedBags;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IncludedCheckedBags {
          private int quantity;

          public int getQuantity() {
            return quantity;
          }

          public void setQuantity(int quantity) {
            this.quantity = quantity;
          }
        }
      }
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Dictionaries {
    private Map<String, Location> locations;

    public Map<String, Location> getLocations() {
      return locations;
    }

    public void setLocations(Map<String, Location> locations) {
      this.locations = locations;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
      private String cityCode;
      private String countryCode;

      public String getCityCode() {
        return cityCode;
      }

      public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
      }

      public String getCountryCode() {
        return countryCode;
      }

      public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
      }
    }
  }
}
