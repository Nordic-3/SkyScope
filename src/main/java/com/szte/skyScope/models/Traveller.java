package com.szte.skyScope.models;

import java.util.ArrayList;
import java.util.List;

public class Traveller {
  private String id;
  private String type;
  private String dateOfBirth;
  private Name name = new Name();
  private String gender;
  private Contact contact = new Contact();
  private List<Passport> documents = new ArrayList<>();

  public Traveller(String id, String type) {
    this.id = id;
    this.type = type;
  }

  public Traveller() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public List<Passport> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Passport> documents) {
    this.documents = documents;
  }

  public static class Name {
    private String firstName;
    private String lastName;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
  }

  public static class Contact {
    private String emailAddress;
    private List<Phone> phones = new ArrayList<>();

    public String getEmailAddress() {
      return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
    }

    public List<Phone> getPhones() {
      return phones;
    }

    public void setPhones(List<Phone> phones) {
      this.phones = phones;
    }
  }

  public static class Phone {
    private String deviceType = "MOBILE";
    private String countryCallingCode;
    private String number;

    public String getDeviceType() {
      return deviceType;
    }

    public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
    }

    public String getCountryCallingCode() {
      return countryCallingCode;
    }

    public void setCountryCallingCode(String countryCallingCode) {
      this.countryCallingCode = countryCallingCode;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }
  }
}
