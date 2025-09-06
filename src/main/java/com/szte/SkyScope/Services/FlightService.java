package com.szte.SkyScope.Services;

import com.szte.SkyScope.Models.AmadeusApiCred;

public interface FlightService {
    AmadeusApiCred getToken();
    String getIataCodeFromApi(String city, String token);
    String getIataCodeFromJson(String city);
}
