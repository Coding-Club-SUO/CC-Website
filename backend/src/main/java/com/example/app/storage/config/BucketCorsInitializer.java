/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.storage.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.PutBucketCorsRequest;
import software.amazon.awssdk.services.s3.model.CORSConfiguration;

/**
 *
 * @author rashi
 */
@Component
@Profile("!test")
public class BucketCorsInitializer {
    
    private static final String IMAGES_BUCKET = "images";

    private final S3Client s3Client;

    public BucketCorsInitializer(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostConstruct
    public void configureCors() {
        CORSRule rule = CORSRule.builder()
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "HEAD")
                .allowedHeaders("*")
                .exposeHeaders("ETag")
                .maxAgeSeconds(3000)
                .build();

        PutBucketCorsRequest request = PutBucketCorsRequest.builder()
                .bucket(IMAGES_BUCKET)
                .corsConfiguration(CORSConfiguration.builder()
                        .corsRules(rule)
                        .build())
                .build();

        s3Client.putBucketCors(request);
    }
}