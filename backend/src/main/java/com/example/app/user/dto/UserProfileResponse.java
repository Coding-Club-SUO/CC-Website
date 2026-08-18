package com.example.app.user.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record UserProfileResponse(
    String id,
    String username,
    String email,
    String photoLink,
    String faculty,
    String bio,
    Set<String> activeIn,
    Date dateJoined,
    Integer posts,
    Integer comments
) {}