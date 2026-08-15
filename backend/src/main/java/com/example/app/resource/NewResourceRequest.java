package com.example.app.resource;

import org.springframework.web.multipart.MultipartFile;

/**
 * The inbound payload for creating a resource: the three metadata fields plus
 * the uploaded file. Assembled by {@link ResourceController} from the
 * multipart request and handed to the service, which owns validation.
 */
public record NewResourceRequest(String title, String course, String uploader, MultipartFile file) {
}
