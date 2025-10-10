package com.szte.SkyScope.DTOs;

public record UserCreationDTO(
    String dateOfBirth,
    String firstName,
    String lastName,
    String email,
    String gender,
    String phoneNumber,
    String password) {}
