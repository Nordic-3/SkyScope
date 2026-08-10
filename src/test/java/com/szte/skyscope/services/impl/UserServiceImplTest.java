package com.szte.skyscope.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szte.skyscope.config.KeykloackConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Keycloak keycloak;

  @Mock private KeykloackConfig keykloackConfig;

  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(keycloak, keykloackConfig);
  }

  @Test
  void deleteById_Success() {
    String userId = "user-123-id";
    String realmName = "test-realm";

    when(keykloackConfig.getRealm()).thenReturn(realmName);

    userService.deleteById(userId);

    verify(keycloak.realm(realmName).users().get(userId)).remove();
  }
}
