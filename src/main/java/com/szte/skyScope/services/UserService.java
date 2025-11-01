package com.szte.skyScope.services;

import com.szte.skyScope.dtos.UserCreationDTO;
import com.szte.skyScope.dtos.UserDTO;
import com.szte.skyScope.models.User;
import java.util.Optional;

public interface UserService {
  Optional<User> getUserByEmail(String email);

  UserDTO saveUser(UserCreationDTO userCreationDTO);

  void deleteByEmail(String email);

  UserDTO updateUser(UserCreationDTO userCreationDTO);
}
