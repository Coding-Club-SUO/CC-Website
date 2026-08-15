package com.example.app.resource.storage;

import org.springframework.core.io.Resource;

/**
 * Abstraction over where uploaded files physically live. The service layer
 * depends on this interface, never on a concrete backend, so the store can be
 * swapped (local volume today; object storage later) without touching business
 * logic. See {@link LocalFileStorage} for the default implementation.
 */
public interface FileStorage {

    /**
     * Persists the given bytes and returns an opaque storage key that can later
     * be passed to {@link #load(String)}. The key is an internal identifier — it
     * is never a raw filesystem path exposed to clients.
     *
     * @param bytes            the file content
     * @param originalFilename the client-supplied name, used only to preserve the extension
     * @return the storage key to persist alongside the resource metadata
     */
    String store(byte[] bytes, String originalFilename);

    /**
     * Loads previously stored bytes by their storage key.
     *
     * @throws StorageException if no file exists for the key or it cannot be read
     */
    Resource load(String storageKey);
}
