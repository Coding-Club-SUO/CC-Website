package com.example.app.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Identifier is required")
    @Size(max = 254, message = "Identifier cannot exceed 254 characters")
    String identifier,

    @NotBlank(message = "Password is required")
    @Size(max = 128, message = "Password cannot exceed 128 characters")
    String password,

    Boolean rememberUser
) {}
