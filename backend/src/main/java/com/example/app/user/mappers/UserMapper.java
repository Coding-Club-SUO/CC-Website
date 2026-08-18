/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.user.mappers;

import com.example.app.user.dto.UserProfileResponse;
import com.example.app.user.entity.User;
import org.mapstruct.Mapper;

/**
 *
 * @author rashi
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileResponse toDto(User user);
}
