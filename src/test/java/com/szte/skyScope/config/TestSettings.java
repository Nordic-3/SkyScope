package com.szte.skyScope.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test.user")
@Data
public class TestSettings {
  private String email;
  private String password;
  private String firstName;
  private String lastName;
}
