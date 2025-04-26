package com.szte.SkyScope.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${use_apis}")
    private boolean useApis;
    @Value("${geo_names_key:noApi}")
    private String geoNamesApiKey;

    public boolean useApis() {
        return useApis;
    }

    public String getGeoNamesApiKey() {
        return geoNamesApiKey;
    }
}
