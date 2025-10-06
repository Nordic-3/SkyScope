package com.szte.SkyScope.Enums;

import java.util.Arrays;

public enum TravellerTypes {
  ADULT("Felnőtt"),
  CHILD("Gyerek"),
  HELD_INFANT("Csecsemő, ölben");

  private final String value;

  TravellerTypes(String value) {
    this.value = value;
  }

  public static String getValueFromType(String type) {
    return Arrays.stream(values())
        .filter(e -> e.name().equalsIgnoreCase(type))
        .findFirst()
        .get()
        .value;
  }
}
