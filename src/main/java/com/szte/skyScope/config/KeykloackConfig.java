package com.szte.skyScope.config;

import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeykloackConfig {
  @Getter
  @Value("${keycloak.auth-server-url}")
  private String authServerUrl;

  @Getter
  @Value("${keycloak.realm}")
  private String realm;

  @Getter
  @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
  private String clientSecret;

  @Getter
  @Value("${keycloak.auth-password-change-url}")
  private String authPasswordChangeUrl;

  @Getter
  @Value("${keycloack.kc_action}")
  private String kcAction;

  @Bean
  public Keycloak keycloakAdminClient() {
    return KeycloakBuilder.builder()
        .serverUrl(authServerUrl)
        .realm(realm)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .build();
  }
}
