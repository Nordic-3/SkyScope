package com.szte.skyScope.factories;

import com.szte.skyScope.models.Passport;
import com.szte.skyScope.models.Traveller;
import java.time.LocalDate;

public class TravellerFactory {

  public static Traveller createTraveller() {
    Traveller traveller = new Traveller();
    traveller.setGender("M");
    traveller.setDateOfBirth("1990-01-01");
    Traveller.Name name = new Traveller.Name();
    name.setFirstName("John");
    name.setLastName("Doe");
    traveller.setName(name);
    Traveller.Contact contact = new Traveller.Contact();
    contact.setEmailAddress("test@example.com");
    Traveller.Phone phone = new Traveller.Phone();
    phone.setCountryCallingCode("36");
    phone.setNumber("123456789");
    contact.setPhones(java.util.List.of(phone));
    traveller.setContact(contact);
    Passport passport = new Passport();
    passport.setIssuanceCountry("HU");
    passport.setNumber("P123");
    passport.setDocumentType("Passport");
    passport.setBirthPlace("Budapest");
    passport.setIssuanceDate(LocalDate.now().minusYears(5).toString());
    passport.setExpiryDate(LocalDate.now().plusYears(5).toString());
    passport.setNationality("HU");
    passport.setValidityCountry("HU");
    traveller.setDocuments(java.util.List.of(passport));
    return traveller;
  }
}
