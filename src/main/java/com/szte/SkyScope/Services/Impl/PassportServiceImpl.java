package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.DTOs.PassportDTO;
import com.szte.SkyScope.Models.Passport;
import com.szte.SkyScope.Repositories.PassportRepository;
import com.szte.SkyScope.Services.PassportService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassportServiceImpl implements PassportService {
  private final PassportRepository passportRepository;

  @Autowired
  public PassportServiceImpl(PassportRepository passportRepository) {
    this.passportRepository = passportRepository;
  }

  @Override
  public Optional<PassportDTO> getPassportById(long id) {
    return passportRepository.findById(id).map(this::convertPassportToDTO);
  }

  @Override
  public PassportDTO savePassport(PassportDTO passportDTO) {
    return convertPassportToDTO(passportRepository.save(convertDTOToPassport(passportDTO)));
  }

  @Override
  public void deleteById(long id) {
    passportRepository.deleteById(id);
  }

  private PassportDTO convertPassportToDTO(Passport passport) {
    return new PassportDTO(
        passport.getId(),
        passport.getDocumentType(),
        passport.getBirthPlace(),
        passport.getIssuanceLocation(),
        passport.getIssuanceDate(),
        passport.getNumber(),
        passport.getExpiryDate(),
        passport.getIssuanceCountry(),
        passport.getValidityCountry(),
        passport.getNationality(),
        passport.isHolder());
  }

  private Passport convertDTOToPassport(PassportDTO passportDTO) {
    return new Passport(
        passportDTO.id(),
        passportDTO.documentType(),
        passportDTO.birthPlace(),
        passportDTO.issuanceLocation(),
        passportDTO.issuanceDate(),
        passportDTO.number(),
        passportDTO.expiryDate(),
        passportDTO.issuanceCountry(),
        passportDTO.validityCountry(),
        passportDTO.nationality(),
        passportDTO.holder());
  }
}
