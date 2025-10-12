package com.szte.SkyScope.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "passports")
public class Passport {
  @Id private long id;
  private String documentType;
  private String birthPlace;
  private String issuanceLocation;
  private String issuanceDate;
  private String number;
  private String expiryDate;
  private String issuanceCountry;
  private String validityCountry;
  private String nationality;
  private boolean holder = true;

  public Passport() {}

  public Passport(
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
      boolean holder) {
    this.id = id;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public String getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public String getIssuanceLocation() {
    return issuanceLocation;
  }

  public void setIssuanceLocation(String issuanceLocation) {
    this.issuanceLocation = issuanceLocation;
  }

  public String getIssuanceDate() {
    return issuanceDate;
  }

  public void setIssuanceDate(String issuanceDate) {
    this.issuanceDate = issuanceDate;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public String getIssuanceCountry() {
    return issuanceCountry;
  }

  public void setIssuanceCountry(String issuanceCountry) {
    this.issuanceCountry = issuanceCountry.toUpperCase();
  }

  public String getValidityCountry() {
    return validityCountry;
  }

  public void setValidityCountry(String validityCountry) {
    this.validityCountry = validityCountry.toUpperCase();
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality.toUpperCase();
  }

  public boolean isHolder() {
    return holder;
  }

  public void setHolder(boolean holder) {
    this.holder = holder;
  }
}
