package com.szte.SkyScope.Models;

import java.util.ArrayList;
import java.util.List;

public class FilterAttribute {
    private String maxPrice = "";
    private List<String> airlines = new ArrayList<>();
    private String transferNumber = "";
    private String transferDuration = "";
    private List<String> airplanes = new ArrayList<>();

    public List<String> getAirplanes() {
        return airplanes;
    }

    public void setAirplanes(List<String> airplanes) {
        this.airplanes = airplanes;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<String> getAirlines() {
        return airlines;
    }

    public void setAirlines(List<String> airlines) {
        this.airlines = airlines;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public String getTransferDuration() {
        return transferDuration;
    }

    public void setTransferDuration(String transferDuration) {
        this.transferDuration = transferDuration;
    }
}
