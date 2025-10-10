package com.szte.SkyScope.Services;

import com.szte.SkyScope.DTOs.PassportDTO;
import java.util.Optional;

public interface PassportService {
  Optional<PassportDTO> getPassportById(long id);

  PassportDTO savePassport(PassportDTO passportDTO);

  void deleteById(long id);
}
