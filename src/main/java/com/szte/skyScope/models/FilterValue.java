package com.szte.skyScope.models;

import com.szte.skyScope.utils.FlightOfferFormatter;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FilterValue {
  private List<String> allAirline;
  private List<String> transferNumbers;
  private List<Duration> layoverTimes;
  private List<String> airplaneTypes;
  private String maxPrice;
  private boolean isCurrentlyFlying;

  public String getFormattedLayoverTime(Duration duration) {
    return FlightOfferFormatter.formatDuration(duration.toString());
  }
}
