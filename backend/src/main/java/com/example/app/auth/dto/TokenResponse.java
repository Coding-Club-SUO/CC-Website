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
public record TokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    UserProfileResponse userData
) {
    public TokenResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }
    
    public TokenResponse(String accessToken, String refreshToken, long expiresIn, UserProfileResponse userData) {
        this(accessToken, refreshToken, "Bearer", expiresIn, userData);
    }
    
    public TokenResponse(String accessToken, String refreshToken, UserProfileResponse userData) {
        this(accessToken, refreshToken, "Bearer", -1, userData);
    }
}
