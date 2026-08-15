package com.example.app.resource;

/**
 * Thrown when an upload request is invalid — missing fields, no file, a
 * disallowed file type, or an empty file. Maps to HTTP 400 via
 * {@link ResourceExceptionHandler}.
 */
public class BadUploadException extends RuntimeException {

    public BadUploadException(String message) {
        super(message);
    }
}
