package com.example.app.auth;

import com.example.app.redis.RedisService;
import com.example.app.user.entity.Permissions;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    @Value("${spring.jwt.max.refresh.expiration}")
    private long maxRefreshExpiration;
    private final RedisService redisService;
    
    private final static String REFRESH_TOKEN_TYPE = "refresh";
    private final static String ACCESS_TOKEN_TYPE = "access";
    
    private final static String REDIS_KEY = "SESSION";
    private final static String BLACKLIST_REDIS_KEY = "BLACKLISTED";

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;
    
    public JwtService(RedisService redisService) {
        this.redisService = redisService;
    }

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
    
    public void blacklistToken(String token, boolean isRefreshToken) {
        try {
            SecretKey tokenKey = isRefreshToken ? refreshTokenKey : accessTokenKey;
            String type = isRefreshToken ? REFRESH_TOKEN_TYPE : ACCESS_TOKEN_TYPE;
        
        
            Claims payload = this.extractAllClaims(token, tokenKey, type);
        
            String key = REDIS_KEY + ":" + BLACKLIST_REDIS_KEY + ":" + payload.getId();
            Date expDate = payload.getExpiration();
        
            long remainingMs = expDate.getTime() - System.currentTimeMillis();
            if (remainingMs <= 0) {
                return;
            }
            redisService.set(key, payload.getSubject(), remainingMs / 1000);
        } catch (JwtException e) {
            logger.warn("Invalid JWT: {}", e.getMessage());
        }
    }
    
    public boolean isBlacklisted(String token, boolean isRefreshToken) {
        try {
            SecretKey tokenKey = isRefreshToken ? refreshTokenKey : accessTokenKey;
            String type = isRefreshToken ? REFRESH_TOKEN_TYPE : ACCESS_TOKEN_TYPE;
        
            Claims payload = this.extractAllClaims(token, tokenKey, type);
            String key = REDIS_KEY + ":" + BLACKLIST_REDIS_KEY + ":" + payload.getId();
        
            return (redisService.get(key) != null);
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT: {}", e.getMessage());
            return true;
        }
    }

    public String generateAccessToken(String userid, Set<Permissions> authorities) {
        return generateToken(userid, authorities, accessTokenKey, accessExpiration, ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(String userid, boolean maxExp) {
        return generateToken(
                userid, null, 
                refreshTokenKey, maxExp ? maxRefreshExpiration : refreshExpiration, 
                REFRESH_TOKEN_TYPE
        );
    }
    
    public String generateRefreshToken(String userid, long ttl) {
        return generateToken(
                userid, null, 
                refreshTokenKey, ttl, 
                REFRESH_TOKEN_TYPE
        );
    }

    public String extractUserId(String token, boolean isRefreshToken) {
        SecretKey key = isRefreshToken ? refreshTokenKey : accessTokenKey;
        String type = isRefreshToken ? REFRESH_TOKEN_TYPE : ACCESS_TOKEN_TYPE;
        try {
            return extractAllClaims(token, key, type).getSubject();
        } catch (JwtException e) {
            logger.warn("Failed to extract userid: {}", e.getMessage());
            return null;
        }
    }
    
    public long extractTTL(String token, boolean isRefreshToken) {
        SecretKey key = isRefreshToken ? refreshTokenKey : accessTokenKey;
        String type = isRefreshToken ? REFRESH_TOKEN_TYPE : ACCESS_TOKEN_TYPE;

        try {
            Date expiration = extractAllClaims(token, key, type).getExpiration();
            if (expiration == null) {
                return 0;
            }

            long ttlInSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(0, ttlInSeconds);
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Failed to extract token expiration: {}", e.getMessage());
            return 0;
        }
    }

    public boolean isAccessTokenValid(String token) {
        return !isBlacklisted(token, false);
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            if (isBlacklisted(token, true)) {
                return false;
            }
            Claims payload = extractAllClaims(token, refreshTokenKey, REFRESH_TOKEN_TYPE);
            if (payload == null || payload.getId() == null) {
                return false;
            }
            String key = REDIS_KEY + ":" + payload.getId();
            String currrentFingerprint = payload.getId() + ":" + this.getCurrentUserAgent();
            String hashedFingerprint = hashSHA256(currrentFingerprint);
            String storedFingerprint = redisService.get(key);
            return !(storedFingerprint == null || !storedFingerprint.equals(hashedFingerprint)); 
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }
    
    private static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available in this JRE", e);
        }
    }
    
    private String getCurrentUserAgent() {
        ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader(HttpHeaders.USER_AGENT);
        }

        return "Unknown";
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
    private String generateToken(
            String userid, Set<Permissions> authorities, 
            SecretKey key, long ttlSeconds, String type) {
        String jti = UUID.randomUUID().toString();
        JwtBuilder builderClaims = Jwts.builder()
                .id(jti)
                .subject(userid)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (ttlSeconds * 1000)))
                .signWith(key);
        
        if (type.equals(REFRESH_TOKEN_TYPE)) {
            String fingerprint = hashSHA256(jti + ":" + this.getCurrentUserAgent());
            redisService.set(REDIS_KEY + ":" + jti, fingerprint, ttlSeconds);
        } else {
             builderClaims.claim("authorities", authorities);
        }
        
        return builderClaims.compact();
    }
}