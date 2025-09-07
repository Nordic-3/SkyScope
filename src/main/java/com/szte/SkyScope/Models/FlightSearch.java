package com.szte.SkyScope.Models;

public class FlightSearch {
    private String originCity;
    private String destinationCity;
    private boolean oneWay;
    private String departureDate;
    private String returnDate;
    private String originCityIata;
    private String destinationCityIata;

    public FlightSearch() {
    }

    public String getOriginCityIata() {
        return originCityIata;
    }

    public void setOriginCityIata(String originCityIata) {
        this.originCityIata = originCityIata;
    }

    public String getDestinationCityIata() {
        return destinationCityIata;
    }

    public void setDestinationCityIata(String destinationCityIata) {
        this.destinationCityIata = destinationCityIata;
    }

    public String getOriginCity() {
        return originCity;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
