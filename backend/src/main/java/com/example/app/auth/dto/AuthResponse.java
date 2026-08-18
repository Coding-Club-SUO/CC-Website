/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.auth.dto;

import com.example.app.user.dto.UserProfileResponse;

/**
 *
 * @author rashi
 */
public record AuthResponse(
        String refreshToken,
        String accessToken,
        UserProfileResponse userData
){}
