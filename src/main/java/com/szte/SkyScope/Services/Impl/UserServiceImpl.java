package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.DTOs.UserCreationDTO;
import com.szte.SkyScope.DTOs.UserDTO;
import com.szte.SkyScope.Models.User;
import com.szte.SkyScope.Repositories.UserRepository;
import com.szte.SkyScope.Services.UserService;
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
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public UserDTO updateUser(UserCreationDTO userCreationDTO) {
    User user = userRepository.getUserByEmail(userCreationDTO.email()).orElseThrow();
    user.setPhoneNumber(userCreationDTO.phoneNumber());
    user.setFirstName(userCreationDTO.firstName());
    user.setLastName(userCreationDTO.lastName());
    user.setPassword(passwordEncoder.encode(userCreationDTO.password()));
    return convertUserToDTO(userRepository.save(user));
  }

  private UserDTO convertUserToDTO(User user) {
    return new UserDTO(
        user.getId(),
        user.getDateOfBirth(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getGender(),
        user.getPhoneNumber());
  }

  private User convertDTOToUser(UserCreationDTO userCreationDTO) {
    return new User(
        userCreationDTO.dateOfBirth(),
        userCreationDTO.firstName(),
        userCreationDTO.lastName(),
        userCreationDTO.email(),
        userCreationDTO.gender(),
        passwordEncoder.encode(userCreationDTO.password()),
        userCreationDTO.phoneNumber());
  }
}
