package com.szte.skyscope.models;

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
