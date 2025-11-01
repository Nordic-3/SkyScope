package com.szte.skyScope.services.impl;

import com.szte.skyScope.dtos.UserCreationDTO;
import com.szte.skyScope.dtos.UserDTO;
import com.szte.skyScope.models.User;
import com.szte.skyScope.repositories.UserRepository;
import com.szte.skyScope.services.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  @Override
  public UserDTO saveUser(UserCreationDTO userCreationDTO) {
    return convertUserToDTO(userRepository.save(convertDTOToUser(userCreationDTO)));
  }

  @Override
  @Transactional
  public void deleteByEmail(String email) {
    userRepository.deleteByEmail(email);
  }

  @Override
  public UserDTO updateUser(UserCreationDTO userCreationDTO) {
    User user = userRepository.getUserByEmail(userCreationDTO.email()).orElseThrow();
    user.setPassword(passwordEncoder.encode(userCreationDTO.password()));
    return convertUserToDTO(userRepository.save(user));
  }

  private UserDTO convertUserToDTO(User user) {
    return new UserDTO(user.getId(), user.getEmail());
  }

  private User convertDTOToUser(UserCreationDTO userCreationDTO) {
    return new User(userCreationDTO.email(), passwordEncoder.encode(userCreationDTO.password()));
  }
}
