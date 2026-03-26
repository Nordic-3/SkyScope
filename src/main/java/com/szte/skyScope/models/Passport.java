package com.szte.skyScope.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Passport {
  @Setter private String documentType;
  @Setter private String birthPlace;
  @Setter private String issuanceLocation;
  @Setter private String issuanceDate;
  @Setter private String number;
  @Setter private String expiryDate;
  private String issuanceCountry;
  private String validityCountry;
  private String nationality;
  @Setter private boolean holder = true;

  public Passport(
      String documentType,
      String birthPlace,
      String issuanceLocation,
      String issuanceDate,
      String number,
      String expiryDate,
      String issuanceCountry,
      String validityCountry,
      String nationality,
      boolean holder) {
    this.documentType = documentType;
    this.birthPlace = birthPlace;
    this.issuanceLocation = issuanceLocation;
    this.issuanceDate = issuanceDate;
    this.number = number;
    this.expiryDate = expiryDate;
    this.issuanceCountry = issuanceCountry.toUpperCase();
    this.validityCountry = validityCountry.toUpperCase();
    this.nationality = nationality.toUpperCase();
    this.holder = holder;
  }

  public void setIssuanceCountry(String issuanceCountry) {
    this.issuanceCountry = issuanceCountry.toUpperCase();
  }

  public void setValidityCountry(String validityCountry) {
    this.validityCountry = validityCountry.toUpperCase();
  }

  public void setNationality(String nationality) {
    this.nationality = nationality.toUpperCase();
  }
}
