package com.example.app.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank String email,
    @NotBlank String password,
    Boolean rememberUser
) {}
