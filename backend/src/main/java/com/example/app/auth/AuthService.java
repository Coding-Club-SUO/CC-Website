package com.example.app.auth;

import com.example.app.auth.dto.AuthResponse;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.RegisterRequest;
import com.example.app.redis.RedisService;
import com.example.app.user.UserService;
import com.example.app.user.dto.UserCreate;
import com.example.app.user.exceptions.BadCredentialsException;
import com.example.app.user.entity.User;
import com.example.app.user.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    
    public AuthService(
            UserService userService, JwtService jwtService, 
            PasswordEncoder passwordEncoder, UserMapper userMapper,
            RedisService redisService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
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
        if (!passwordEncoder.matches(req.password(), found.getPassword())) {
            throw new BadCredentialsException("invalid credentials");
        }
        return generateAuthResponse(found, req.rememberUser());
    }
    
    public AuthResponse issueAccessToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return null;
        }
        
        String userId = jwtService.extractUserId(refreshToken, true);
        User user = userService.loadUserById(userId);
        String accessToken = jwtService.generateAccessToken(userId, user.getAuthorities());
        return new AuthResponse(refreshToken, accessToken, userMapper.toDto(user));
    }
    
    public void logoutUser(String accessToken, String refreshToken) {
        jwtService.blacklistToken(accessToken, false);
        jwtService.blacklistToken(refreshToken, true);
    }
}
