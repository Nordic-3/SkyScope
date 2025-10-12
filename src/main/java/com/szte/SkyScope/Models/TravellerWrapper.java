package com.szte.SkyScope.Models;

import java.util.ArrayList;
import java.util.List;

public class TravellerWrapper {
  private List<Traveller> travellers = new ArrayList<>();

  public List<Traveller> getTravellers() {
    return travellers;
  }

  public void setTravellers(List<Traveller> travellers) {
    this.travellers = travellers;
  }
}
