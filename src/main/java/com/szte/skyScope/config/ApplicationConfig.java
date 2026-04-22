package com.szte.skyScope.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
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

  @Value("${amadeus_flight_offer_search_api:noApi}")
  private String amadeusFlightOfferSearchApi;

  @Value("${amadeus_cheapest_date_search:noApi}")
  private String amadeusCheapestDateSearch;

  @Value("${amadeus_final_price:noApi}")
  private String amadeusFinalPrice;

  @Value("${amadeus_test_client_id:noApi}")
  private String amadeusTestClientId;

  @Value("${amadeus_test_client_secret:noApi}")
  private String amadeusTestClientSecret;

  @Value("${amadeus_create_order:noApi}")
  private String amadeusCreateOrder;

  @Value("${amadeus_test_auth_url:noApi}")
  private String amadeusTestAuthUrl;

  @Value("${amadeus_airline_code:noApi}")
  private String amadeusAirlineCode;

  @Value("${stripe_secret:noApi}")
  private String stripeSecret;
}
