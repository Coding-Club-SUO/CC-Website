package com.example.app.user.models;

import com.example.app.common.Faculty;

import jakarta.validation.constraints.Size;

public record UserUpdate(
    String username,
    String email,
    String password,
    Faculty faculty,
    @Size(max = 250, message = "Bio cannot exceed 250 characters") String bio
) {}