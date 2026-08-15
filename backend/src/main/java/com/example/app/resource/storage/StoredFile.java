package com.example.app.resource.storage;

import org.springframework.core.io.Resource;

/**
 * A file retrieved from storage: its raw bytes (as a Spring {@link Resource})
 * together with the metadata the controller needs to serve a download.
 */
public record StoredFile(Resource body, String filename, String contentType) {
}
