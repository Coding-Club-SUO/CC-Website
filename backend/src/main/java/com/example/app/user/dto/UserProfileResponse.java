package com.example.app.user.dto;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app.user.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record UserProfileResponse(
    String username,
    String email,
    String photoLink,
    String faculty,
    String bio,
    Set<String> activeIn,
    Date dateJoined,
    Integer posts,
    Integer comments
) implements UserDetails {

    public UserProfileResponse(User user) {
        this(user.getUsername(),
            user.getEmail(), 
            user.getPhotoLink(), 
            user.getFaculty().getAction(), 
            user.getBio(), 
            user.getActiveIn(), 
            user.getDateJoined(), 
            user.getPosts(), 
            user.getComments());
    }

    // SPRING SECURITY INTERFACE METHODS (Fulfills full UserDetails contract)

    @Override
    public String getUsername() { 
        return this.username; 
    }

    @Override
    public String getPassword() { 
        throw new UnsupportedOperationException("This operation is not supported"); 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        throw new UnsupportedOperationException("This operation is not supported"); 
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}