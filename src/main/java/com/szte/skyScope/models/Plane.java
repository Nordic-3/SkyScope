package com.szte.skyScope.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Plane {
  private String callsign;
  private String longitude;
  private String latitude;
  private double groundSpeedInMS;
  private double heading;
}
