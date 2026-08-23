/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.auth;

import com.example.app.auth.dto.AuthResponse;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.RefreshRequest;
import com.example.app.auth.dto.RegisterRequest;
import com.example.app.auth.dto.TokenResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author rashi
 */
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse result = authService.registerUser(request);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.userData().id())
                .toUri();
        
        return ResponseEntity.created(location).body(new TokenResponse(
                result.accessToken(), result.refreshToken(), result.userData()
        ));
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @ModelAttribute LoginRequest request) {
        AuthResponse result = authService.loginUser(request);
        
        return ResponseEntity.ok().body(new TokenResponse(
                result.accessToken(), result.refreshToken(), result.userData()
        ));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshRequest request,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid or missing Bearer token");
        }
        authService.logoutUser(authHeader.substring(7), request.refreshToken());
        return ResponseEntity.ok().body("logout successful");
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(
            @Valid @RequestBody RefreshRequest request) {
        AuthResponse result = authService.issueAccessToken(request.refreshToken());
        return ResponseEntity.ok().body(new TokenResponse(
                result.accessToken(), result.refreshToken(), 
                result.reftreshTTL(), result.userData()
        ));
    }
    
}
