package com.szte.SkyScope.Repositories;

import com.szte.SkyScope.Models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> getUserByEmail(String email);

  void deleteByEmail(String email);
}
