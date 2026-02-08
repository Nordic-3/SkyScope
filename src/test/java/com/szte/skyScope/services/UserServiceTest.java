package com.szte.skyScope.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.szte.skyScope.dtos.UserCreationDTO;
import com.szte.skyScope.dtos.UserDTO;
import com.szte.skyScope.models.User;
import com.szte.skyScope.repositories.UserRepository;
import com.szte.skyScope.services.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserServiceImpl userService;

  @Test
  void getUserByEmail() {
    User user = new User("test@test.com", "pw");
    when(userRepository.getUserByEmail("test@test.com")).thenReturn(Optional.of(user));

    Optional<User> result = userService.getUserByEmail("test@test.com");

    assertThat(result).isPresent();
    assertThat(result.get()).isSameAs(user);
  }

  @Test
  void saveUser() {
    UserCreationDTO dto = new UserCreationDTO("test@test.com", "secret", "secret", "", true);
    when(passwordEncoder.encode("secret")).thenReturn("encodedSecret");
    when(userRepository.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User user = invocation.getArgument(0);
              user.setId(42L);
              return user;
            });

    UserDTO saved = userService.saveUser(dto);

    assertThat(saved).isNotNull();
    assertThat(saved.id()).isEqualTo(42L);
    assertThat(saved.email()).isEqualTo("test@test.com");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User passedToSave = captor.getValue();
    assertThat(passedToSave.getEmail()).isEqualTo("test@test.com");
    assertThat(passedToSave.getPassword()).isEqualTo("encodedSecret");
  }

  @Test
  void deleteByEmail() {
    doNothing().when(userRepository).deleteByEmail("test@test.com");

    userService.deleteByEmail("test@test.com");

    verify(userRepository).deleteByEmail("test@test.com");
  }

  @Test
  void updateUserUpdatesPasswordAndReturnsDto() {
    UserCreationDTO dto = new UserCreationDTO("test@test.com", "newpass", "newpass", "", true);
    User existing = new User("test@test.com", "oldpass");
    existing.setId(7L);

    when(userRepository.getUserByEmail("test@test.com")).thenReturn(Optional.of(existing));
    when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserDTO updated = userService.updateUser(dto);

    assertThat(updated).isNotNull();
    assertThat(updated.id()).isEqualTo(7L);
    assertThat(updated.email()).isEqualTo("test@test.com");
    assertThat(existing.getPassword()).isEqualTo("encodedNew");

    verify(userRepository).getUserByEmail("test@test.com");
    verify(userRepository).save(existing);
  }

  @Test
  void updateUserThrowsWhenUserNotFound() {
    UserCreationDTO dto = new UserCreationDTO("test@test.com", "p", "p", "", true);
    when(userRepository.getUserByEmail("test@test.com")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> userService.updateUser(dto));
  }
}
