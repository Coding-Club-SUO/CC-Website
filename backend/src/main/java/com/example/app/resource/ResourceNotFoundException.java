package com.example.app.resource;

/**
 * Thrown when a resource id does not exist. Maps to HTTP 404 via
 * {@link ResourceExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Long id) {
        super("No resource found with id " + id);
    }
}
