package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.AmadeusApiCred;
import com.szte.SkyScope.Models.FlightSearch;

public interface FlightService {
    AmadeusApiCred getToken();
    String getIataCodeFromApi(String city, String token);
    String getIataCodeFromJson(String city);
    void setIataCodes(FlightSearch flightSearch, String token);
}
