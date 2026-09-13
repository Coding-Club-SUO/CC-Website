/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.storage.presign.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author rashi
 */
public record PresignedURL(
        @NotBlank String key,
        @NotBlank String url
) {}
