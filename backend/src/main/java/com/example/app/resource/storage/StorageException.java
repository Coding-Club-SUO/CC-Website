package com.example.app.resource.storage;

/**
 * Raised when the storage backend fails to read or write a file (I/O error,
 * missing directory, unreadable key). This is an infrastructure failure — it
 * maps to a 500, unlike a bad client upload which is a 400.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }
}
