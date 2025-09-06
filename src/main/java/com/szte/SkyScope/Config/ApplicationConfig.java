package com.szte.SkyScope.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${use_apis}")
    private boolean useApis;
    @Value("${geo_names_key:noApi}")
    private String geoNamesApiKey;
    @Value("${opensky_baseurl:noApi}")
    private String openskyApiKey;
    @Value("${amadeus_client_id:noApi}")
    private String amadeusClientId;
    @Value("${amadeus_client_secret:noApi}")
    private String amadeusClientSecret;
    @Value("${amadeus_auth_url:noApi}")
    private String amadeusAuthUrl;
    @Value("${amadeus_city_search_api:noApi}")
    private String amadeusCitySearchApi;

    public boolean useApis() {
        return useApis;
    }

    public String getGeoNamesApiKey() {
        return geoNamesApiKey;
    }

    public String getOpenskyApiKey() {
        return openskyApiKey;
    }

    public String getAmadeusClientId() {
        return amadeusClientId;
    }

    public String getAmadeusClientSecret() {
        return amadeusClientSecret;
    }

    public String getAmadeusAuthUrl() {
        return amadeusAuthUrl;
    }

    public String getAmadeusCitySearchApi() {
        return amadeusCitySearchApi;
    }
}
