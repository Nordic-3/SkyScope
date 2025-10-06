package com.szte.SkyScope.Models;

import com.szte.SkyScope.Utils.FlightOfferFormatter;
import java.time.Duration;
import java.util.List;

public class FilterValue {
  private List<String> allAirline;
  private List<String> transferNumbers;
  private List<Duration> layoverTimes;
  private List<String> airplaneTypes;
  private String maxPrice;

  public FilterValue(
      List<String> allAirline,
      List<String> transferNumbers,
      List<Duration> layoverTimes,
      List<String> airplaneTypes,
      String maxPrice) {
    this.allAirline = allAirline;
    this.transferNumbers = transferNumbers;
    this.layoverTimes = layoverTimes;
    this.airplaneTypes = airplaneTypes;
    this.maxPrice = maxPrice;
  }

  public List<String> getAirplaneTypes() {
    return airplaneTypes;
  }

  public void setAirplaneTypes(List<String> airplaneTypes) {
    this.airplaneTypes = airplaneTypes;
  }

  public List<String> getAllAirline() {
    return allAirline;
  }

  public void setAllAirline(List<String> allAirline) {
    this.allAirline = allAirline;
  }

  public List<String> getTransferNumbers() {
    return transferNumbers;
  }

  public void setTransferNumbers(List<String> transferNumbers) {
    this.transferNumbers = transferNumbers;
  }

  public List<Duration> getLayoverTimes() {
    return layoverTimes;
  }

  public void setLayoverTimes(List<Duration> layoverTimes) {
    this.layoverTimes = layoverTimes;
  }

  public String getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(String maxPrice) {
    this.maxPrice = maxPrice;
  }

  public String getFormattedLayoverTime(Duration duration) {
    return FlightOfferFormatter.formatDuration(duration.toString());
  }
}
