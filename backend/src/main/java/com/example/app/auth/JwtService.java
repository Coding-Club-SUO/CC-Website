package com.example.app.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${spring.jwt.secret.access}")
    private String accessTokenSecretKeyString;
    @Value("${spring.jwt.secret.refresh}")
    private String refreshTokenSecretKeyString;

    @Value("${spring.jwt.access.expiration}")
    private long accessExpiration;
    @Value("${spring.jwt.refresh.expiration}")
    private long refreshExpiration;


    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void init() {
        // This runs AFTER secretKeyString is injected
        try {
            byte[] accessTokenKeyBytes = Decoders.BASE64URL.decode(this.accessTokenSecretKeyString);
            this.accessTokenKey = Keys.hmacShaKeyFor(accessTokenKeyBytes);
            byte[] refreshTokenKeyBytes = Decoders.BASE64URL.decode(this.refreshTokenSecretKeyString);
            this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenKeyBytes);
        } catch (Exception e) {
            logger.error("Failed to extract access and refresh token secrets: {}", e);
        }
    }

    public String generateAccessToken(String userid) {
        return generateToken(userid, accessTokenKey, accessExpiration, "Access");
    }

    public String generateRefreshToken(String userid) {
        return generateToken(userid, refreshTokenKey, refreshExpiration, "Refresh");
    }

    // Extracts userid from any token
    public String extractUserId(String token, boolean isRefreshToken) {
        SecretKey key = isRefreshToken ? refreshTokenKey : accessTokenKey;
        String type = isRefreshToken ? "Refresh" : "Access";
        try {
            return extractAllClaims(token, key, type).getSubject();
        } catch (JwtException e) {
            logger.error("Failed to extract userid: {}", e.getMessage());
            return null;
        }
    }

    public boolean isAccessTokenValid(String token) {
        try {
            // If the token is successfully parsed, it means the signature is 
            // valid and the expiration date has not passed.
            extractAllClaims(token, accessTokenKey, "Access");
            return true; 
        } catch (JwtException | IllegalArgumentException e) {
            // Catches ExpiredJwtException, SignatureException, MalformedJwtException, etc.
            logger.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            // If the token is successfully parsed, it means the signature is 
            // valid and the expiration date has not passed.
            extractAllClaims(token, refreshTokenKey, "Refresh");
            return true; 
        } catch (JwtException | IllegalArgumentException e) {
            // Catches ExpiredJwtException, SignatureException, MalformedJwtException, etc.
            logger.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    // Helper method
    private Claims extractAllClaims(String token, SecretKey key, String type) {
        return Jwts.parser()
                .verifyWith(key)
                .require("type", type)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Helper method
    private String generateToken(String userid, SecretKey key, long exp, String type) {
        return Jwts.builder()
                .subject(userid)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp)) // 1 hour
                .signWith(key)
                .compact();
    }
}