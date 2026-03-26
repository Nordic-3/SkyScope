package com.szte.skyScope.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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

  @Setter
  @Getter
  public static class Name {
    private String firstName;
    private String lastName;
  }

  @Setter
  @Getter
  public static class Contact {
    private String emailAddress;
    private List<Phone> phones = new ArrayList<>();
  }

  @Setter
  @Getter
  public static class Phone {
    private String deviceType = "MOBILE";
    private String countryCallingCode;
    private String number;
  }
}
