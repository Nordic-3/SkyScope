package com.szte.SkyScope.DTOs;

public record UserDTO(
    Long id,
    String dateOfBirth,
    String firstName,
    String lastName,
    String email,
    String gender,
    String phoneNumber) {}
