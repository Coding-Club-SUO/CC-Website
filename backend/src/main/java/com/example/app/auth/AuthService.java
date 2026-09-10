package com.example.app.auth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.auth.dto.AuthResponse;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.RegisterRequest;
import com.example.app.redis.RedisService;
import com.example.app.user.UserService;
import com.example.app.user.dto.UserCreate;
import com.example.app.user.entity.User;
import com.example.app.user.exceptions.BadCredentialsException;
import com.example.app.user.mappers.UserMapper;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ExecutorService cryptoExecutor;
    
    public AuthService(
            UserService userService, JwtService jwtService, 
            PasswordEncoder passwordEncoder, UserMapper userMapper,
            RedisService redisService, 
            @Qualifier("cryptoExecutor") ExecutorService cryptoExecutor) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.cryptoExecutor = cryptoExecutor;
    }
    
    private AuthResponse generateAuthResponse(User user, boolean rememberUser) {
        String userId = user.getId();
        String accessToken = jwtService.generateAccessToken(userId, user.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(userId, rememberUser);
        
        return new AuthResponse(refreshToken, accessToken, userMapper.toDto(user));
    }

    public AuthResponse registerUser(RegisterRequest req) {
        User user = userService.createUser(new UserCreate(req.username(), req.email(), req.password()));
        return generateAuthResponse(user, req.rememberUser());
    }
    
    public AuthResponse loginUser(LoginRequest req) {
        User found = req.identifier().contains("@") ? 
                userService.loadUserByEmail(req.identifier()) : 
                userService.loadUserByUsername(req.identifier());
        
        boolean matches = CompletableFuture.supplyAsync(() -> 
            passwordEncoder.matches(req.password(), found.getPassword()), cryptoExecutor
        ).join();
        if (!matches) {
            throw new BadCredentialsException("invalid credentials");
        }
        return generateAuthResponse(found, req.rememberUser());
    }
    
    public AuthResponse issueAccessToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return null;
        }
        
        String userId = jwtService.extractUserId(refreshToken, true);
        long tokenTTL = jwtService.extractTTL(refreshToken, true);
        User user = userService.loadUserById(userId);
        String accessToken = jwtService.generateAccessToken(userId, user.getAuthorities());
        String newRefreshToken = jwtService.generateRefreshToken(userId, tokenTTL);
        jwtService.blacklistToken(refreshToken, true);
        return new AuthResponse(newRefreshToken, accessToken, tokenTTL, userMapper.toDto(user));
    }
    
    public void logoutUser(String accessToken, String refreshToken) {
        jwtService.blacklistToken(accessToken, false);
        jwtService.blacklistToken(refreshToken, true);
    }
}
