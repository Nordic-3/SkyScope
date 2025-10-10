package com.szte.SkyScope.DTOs;

public record PassportDTO(
    long id,
    String documentType,
    String birthPlace,
    String issuanceLocation,
    String issuanceDate,
    String number,
    String expiryDate,
    String issuanceCountry,
    String validityCountry,
    String nationality,
    boolean holder) {}
