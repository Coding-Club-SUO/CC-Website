package com.example.app.resource.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Filesystem-backed {@link FileStorage}. Files are written under a single root
 * directory configured by {@code app.storage.location}. In production that root
 * is a mounted Docker volume, so uploads survive container restarts; only the
 * mount path changes, not this code.
 *
 * <p>Stored keys are random UUIDs (extension preserved), so client filenames
 * never dictate paths — this avoids collisions and path-traversal via crafted
 * upload names. The original filename is kept in the database, not on disk.
 */
@Component
public class LocalFileStorage implements FileStorage {

    private final Path root;

    public LocalFileStorage(@Value("${app.storage.location:uploads}") String location) {
        this.root = Paths.get(location).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new StorageException("Could not initialise storage directory: " + root, e);
        }
    }

    @Override
    public String store(byte[] bytes, String originalFilename) {
        String key = UUID.randomUUID() + extensionOf(originalFilename);
        Path target = root.resolve(key).normalize();
        // Defensive: the resolved path must stay inside the root.
        if (!target.getParent().equals(root)) {
            throw new StorageException("Resolved storage path escapes the root directory");
        }
        try {
            Files.write(target, bytes);
        } catch (IOException e) {
            throw new StorageException("Failed to write file to storage", e);
        }
        return key;
    }

    @Override
    public Resource load(String storageKey) {
        Path target = root.resolve(storageKey).normalize();
        if (!target.getParent().equals(root)) {
            throw new StorageException("Storage key escapes the root directory: " + storageKey);
        }
        try {
            Resource resource = new UrlResource(target.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new StorageException("File not found or unreadable for key: " + storageKey);
            }
            return resource;
        } catch (IOException e) {
            throw new StorageException("Failed to read file from storage: " + storageKey, e);
        }
    }

    /** Returns the lowercased extension including the dot (e.g. ".pdf"), or "" if none. */
    private static String extensionOf(String filename) {
        String ext = StringUtils.getFilenameExtension(filename);
        return ext == null || ext.isBlank() ? "" : "." + ext.toLowerCase();
    }
}
