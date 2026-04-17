package com.szte.skyScope.services.impl;

import com.szte.skyScope.config.KeykloackConfig;
import com.szte.skyScope.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Keycloak keycloak;
  private final KeykloackConfig keykloackConfig;

  @Override
  public void deleteById(String id) {
    keycloak.realm(keykloackConfig.getRealm()).users().get(id).remove();
  }
}
