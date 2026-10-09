/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.storage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CORSConfiguration;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketCorsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


/**
 *
 * @author rashi
 */
@Component
@Profile("!test")
public class BucketCorsInitializer {

    private static final Logger log = LoggerFactory.getLogger(BucketCorsInitializer.class);
    private static final String IMAGES_BUCKET = "images";

    private final S3Client s3Client;

    public BucketCorsInitializer(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostConstruct
    public void configureCors() {
        try {
            ensureBucketExists();

            CORSRule rule = CORSRule.builder()
                    .id("AllowLocalhost3000")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "HEAD")
                    .allowedHeaders("*")
                    .exposeHeaders("ETag")
                    .maxAgeSeconds(3000)
                    .build();

            s3Client.putBucketCors(PutBucketCorsRequest.builder()
                    .bucket(IMAGES_BUCKET)
                    .corsConfiguration(CORSConfiguration.builder().corsRules(rule).build())
                    .build());

            log.info("CORS configured on bucket '{}'", IMAGES_BUCKET);
        } catch (S3Exception e) {
            log.warn("Could not configure CORS on bucket '{}': {} (HTTP {})",
                    IMAGES_BUCKET, e.awsErrorDetails().errorMessage(), e.statusCode());
            System.out.println("Could not configure CORS on bucket: " + e);
        }
    }

    private void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(IMAGES_BUCKET).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(IMAGES_BUCKET).build());
            log.info("Created bucket '{}'", IMAGES_BUCKET);
        }
    }
}