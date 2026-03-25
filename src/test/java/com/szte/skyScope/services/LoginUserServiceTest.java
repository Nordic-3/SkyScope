package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.szte.skyScope.models.User;
import com.szte.skyScope.services.impl.LoginUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

  @Mock private UserService userService;

  @InjectMocks private LoginUserService loginUserService;

  @Test
  void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
    String email = "test@example.com";
    User mockUser = new User();
    mockUser.setEmail(email);
    mockUser.setPassword("encodedPassword");

    when(userService.getUserByEmail(email)).thenReturn(Optional.of(mockUser));
    UserDetails result = loginUserService.loadUserByUsername(email);

    assertThat(email).isEqualTo(result.getUsername());
    assertThat("encodedPassword").isEqualTo(result.getPassword());
    assertThat(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")))
        .isTrue();
  }

  @Test
  void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
    String email = "notfound@example.com";
    when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> loginUserService.loadUserByUsername(email));
  }
}
