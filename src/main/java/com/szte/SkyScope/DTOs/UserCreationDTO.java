package com.szte.SkyScope.DTOs;

public record UserCreationDTO(
    String email, String password, String rePassword, String oldPassword) {}
