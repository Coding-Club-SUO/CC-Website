/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.user.dto;

import com.example.app.common.Faculty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author rashi
 */
public record UserCreate(
    @NotBlank
    String username,

    @NotBlank
    String password,

    @NotBlank
    @Email
    String email,

    Faculty faculty,

    @Nullable
    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    String bio,

    @Nullable
    String photoLink
) {
    public UserCreate(String username, String email, String password) {
        this(username, email, password, Faculty.NA, null, null);
    }
}
