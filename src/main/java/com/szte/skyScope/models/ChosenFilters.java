package com.szte.skyScope.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChosenFilters {
  private String maxPrice = "";
  private List<String> airlines = new ArrayList<>();
  private String transferNumber = "";
  private String transferDuration = "";
  private List<String> airplanes = new ArrayList<>();
  private boolean isCurrentlyFlying = false;
}
