package com.szte.skyScope.config;

import com.szte.skyScope.services.*;
import com.szte.skyScope.services.providers.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataProviderConfig {

  @Bean
  @ConditionalOnExpression("'${amadeus_client_id:}' != '' && '${use_apis:false}' == 'true'")
  public CheapestFlightDataProvider apiCheapestFlightDataProvider(ApplicationConfig config) {
    return new ApiCheapestFlightData(config);
  }

  @Bean
  @ConditionalOnMissingBean(CheapestFlightDataProvider.class)
  public CheapestFlightDataProvider jsonCheapestFlightDataProvider(JsonReaderService jsonReader) {
    return new JsonCheapestFlightData(jsonReader);
  }

  @Bean
  @ConditionalOnExpression("'${geo_names_key:}' != '' && '${use_apis:false}' == 'true'")
  public CityProvider apiCityProvider(ApplicationConfig config) {
    return new ApiCity(config);
  }

  @Bean
  @ConditionalOnMissingBean(CityProvider.class)
  public CityProvider jsonCityProvider(JsonReaderService jsonReader) {
    return new JsonCity(jsonReader);
  }

  @Bean
  @ConditionalOnExpression("'${amadeus_client_id:}' != '' && '${use_apis:false}' == 'true'")
  public CreateFlightOrderProvider apiCreateFlightOrderProvider(ApplicationConfig config) {
    return new ApiCreateFlightOrder(config);
  }

  @Bean
  @ConditionalOnMissingBean(CreateFlightOrderProvider.class)
  public CreateFlightOrderProvider jsonCreateFlightOrderProvider(JsonReaderService jsonReader) {
    return new JsonCreateFlightOrder(jsonReader);
  }

  @Bean
  @ConditionalOnExpression("'${opensky_baseurl:}' != '' && '${use_apis:false}' == 'true'")
  public PlanePositionProvider apiPlanePositionProvider(ApplicationConfig config) {
    return new ApiPlanePosition(config);
  }

  @Bean
  @ConditionalOnMissingBean(PlanePositionProvider.class)
  public PlanePositionProvider jsonPlanePositionProvider(JsonReaderService jsonReader) {
    return new JsonPlanePosition(jsonReader);
  }

  @Bean
  @ConditionalOnExpression("'${amadeus_client_id:}' != '' && '${use_apis:false}' == 'true'")
  public CachedApiCallsProvider apiCachedApiCallsProvider(ApplicationConfig config) {
    return new ApiCachedCalls(config);
  }

  @Bean
  @ConditionalOnMissingBean(CachedApiCallsProvider.class)
  public CachedApiCallsProvider jsonCachedApiCallsProvider(JsonReaderService jsonReader) {
    return new JsonCachedCalls(jsonReader);
  }

  @Bean
  @ConditionalOnExpression("'${amadeus_client_id:}' != '' && '${use_apis:false}' == 'true'")
  public FlightServiceProvider apiFlightServiceProvider(ApplicationConfig config) {
    return new ApiFlightService(config);
  }

  @Bean
  @ConditionalOnMissingBean(FlightServiceProvider.class)
  public FlightServiceProvider jsonFlightServiceProvider(JsonReaderService jsonReader) {
    return new JsonFlightService(jsonReader);
  }
}
