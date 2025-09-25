package com.szte.SkyScope.Models;

public class Plane {
  private String callsign;
  private String longitude;
  private String latitude;
  private double groundSpeedInMS;
  private double heading;

  public Plane(
      String callsign, String longitude, String latitude, double groundSpeedInMS, double heading) {
    this.callsign = callsign;
    this.longitude = longitude;
    this.latitude = latitude;
    this.groundSpeedInMS = groundSpeedInMS;
    this.heading = heading;
  }

  public Plane() {}

  public String getCallsign() {
    return callsign;
  }

  public void setCallsign(String callsign) {
    this.callsign = callsign;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public double getHeading() {
    return heading;
  }

  public double getGroundSpeedInMS() {
    return groundSpeedInMS;
  }

  public void setGroundSpeedInMS(double groundSpeedInMS) {
    this.groundSpeedInMS = groundSpeedInMS;
  }

  public void setHeading(double heading) {
    this.heading = heading;
  }
}
