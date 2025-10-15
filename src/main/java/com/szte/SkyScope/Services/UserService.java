package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.UserCreationDTO;
import com.szte.SkyScope.DTOs.UserDTO;
import com.szte.SkyScope.Models.User;
import java.util.Optional;

public interface UserService {
  Optional<User> getUserByEmail(String email);

  UserDTO saveUser(UserCreationDTO userCreationDTO);

  void deleteByEmail(String email);

  UserDTO updateUser(UserCreationDTO userCreationDTO);
}
