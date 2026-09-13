/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.storage.presign;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.app.storage.presign.dto.PresignedURL;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;


/**
 *
 * @author rashi
 */
@Service
public class PresignedUrlService {

    private static final String IMAGE_BUCKET = "images";
    private static final String RESOURCE_BUCKET = "resources";

    private final S3Presigner presigner;
    private final PresignedUrlProperties props;

    public PresignedUrlService(S3Presigner presigner, PresignedUrlProperties props) {
        this.presigner = presigner;
        this.props = props;
    }

    public PresignedURL generateImageUploadUrl(String contentType) {
        String key = "uploads/" + UUID.randomUUID() + imageExtensionFor(contentType);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(IMAGE_BUCKET)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(props.uploadExpiry())
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
        
        String url = presigned.url().toString();
        return new PresignedURL(key, url);
    }
    
    public PresignedURL generateResourceUploadUrl(String contentType) {
        String key = "uploads/" + UUID.randomUUID() + resourceExtensionFor(contentType);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(RESOURCE_BUCKET)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(props.uploadExpiry())
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
        
        String url = presigned.url().toString();
        return new PresignedURL(key, url);
    }
    
    public String generateImageDownloadUrl(String key) {
        return props.s3PublicEndpoint() + "/images/" + key;
    }

    public String generateResourceDownloadUrl(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(RESOURCE_BUCKET)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(props.downloadExpiry())
                .getObjectRequest(getRequest)
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);

        return presigned.url().toString();
    }

    private static String imageExtensionFor(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/svg+xml" -> ".svg";
            default -> throw new IllegalArgumentException("Unsupported content type: " + contentType);
        };
    }
    
    private static String resourceExtensionFor(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }

        return switch (contentType.toLowerCase().trim()) {
            // --- Documents & Texts ---
            case "application/pdf" -> ".pdf";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx";
            case "application/msword" -> ".doc";
            case "text/plain" -> ".txt";
            case "application/rtf", "text/rtf" -> ".rtf";
            case "application/vnd.oasis.opendocument.text" -> ".odt";

            // --- Presentations ---
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> ".pptx";
            case "application/vnd.ms-powerpoint" -> ".ppt";
            case "application/x-iwork-keynote-sffkey" -> ".key";
            case "application/vnd.oasis.opendocument.presentation" -> ".odp";

            // --- Spreadsheets & Data ---
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> ".xlsx";
            case "application/vnd.ms-excel" -> ".xls";
            case "text/csv" -> ".csv";
            case "application/vnd.oasis.opendocument.spreadsheet" -> ".ods";

            // --- Images ---
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";

            // --- Archives & Packages ---
            case "application/zip", "application/x-zip-compressed" -> ".zip";
            case "application/x-7z-compressed" -> ".7z";
            case "application/x-tar", "application/gzip" -> ".tar.gz";

            default -> throw new IllegalArgumentException("Unsupported content type: " + contentType);
        };
    }
}
