/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.auth;

import com.example.app.auth.dto.AuthResponse;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.RegisterRequest;
import com.example.app.auth.dto.TokenResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
    @Value("${spring.jwt.refresh.expiration}")
    private long refreshExpiration;
    @Value("${spring.jwt.max.refresh.expiration}")
    private long maxRefreshExpiration;
    @Value("#{environment.acceptsProfiles('prod')}")
    private boolean prodProfile;
    
    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    private String setRefreshToken(String token, boolean rememberUser) {
        Duration exp = Duration.ofSeconds(rememberUser ? maxRefreshExpiration : refreshExpiration);
        return ResponseCookie.from(REFRESH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(prodProfile)
                .path("/api/v1/auth/refresh")
                .maxAge(exp)
                .sameSite("Strict")
                .build().toString();
    }
    
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse result = authService.registerUser(request);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.userData().id())
                .toUri();
        
        return ResponseEntity.created(location)
                .header(HttpHeaders.SET_COOKIE, setRefreshToken(result.refreshToken(), request.rememberUser()))
                .body(new TokenResponse(result.accessToken(), "Bearer", result.userData()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @ModelAttribute LoginRequest request) {
        AuthResponse result = authService.loginUser(request);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.userData().id())
                .toUri();
        
        return ResponseEntity.created(location)
                .header(HttpHeaders.SET_COOKIE, setRefreshToken(result.refreshToken(), request.rememberUser()))
                .body(new TokenResponse(result.accessToken(), "Bearer", result.userData()));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @CookieValue(REFRESH_COOKIE_NAME) String refreshToken,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid or missing Bearer token");
        }
        authService.logoutUser(authHeader.substring(7), refreshToken);
        return ResponseEntity.ok().body("logout successful");
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponse result = authService.issueAccessToken(refreshToken);
        return ResponseEntity.ok().body(new TokenResponse(result.accessToken(), "Bearer", result.userData()));
    }
    
}
