/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.storage.presign;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 *
 * @author rashi
 */
@ConfigurationProperties(prefix = "seaweedfs.presign")
public record PresignedUrlProperties(
        String s3PublicEndpoint,
        Duration uploadExpiry,
        Duration downloadExpiry
) {}
