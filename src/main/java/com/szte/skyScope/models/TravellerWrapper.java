package com.szte.skyScope.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TravellerWrapper {
  private List<Traveller> travellers = new ArrayList<>();
}
