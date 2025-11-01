package com.szte.skyScope.dtos;

public record UserCreationDTO(
    String email, String password, String rePassword, String oldPassword, boolean gdprAccepted) {}
