package com.example.app.resource;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Persistence model (the "M" in MVC) for a shared course resource.
 * This entity is never exposed directly over HTTP — {@link ResourceResponse}
 * is the API-facing type, keeping the web layer decoupled from the schema.
 *
 * <p>The uploaded file itself lives in {@link com.example.app.resource.storage.FileStorage};
 * this row stores only the metadata needed to list it and to serve it back:
 * an opaque {@code storageKey} (never exposed to clients), the original filename,
 * content type, and size. The client-facing download URL is derived from the id
 * in {@link ResourceResponse}, not stored here.
 */
@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String course;
    // TODO: becomes a reference to the authenticated user once auth is wired up.
    private String uploader;
    private Instant uploadedAt;

    /** Opaque key into {@link com.example.app.resource.storage.FileStorage}. Null for metadata-only (seed) rows. */
    private String storageKey;
    /** The name the file was uploaded under, used for the download's Content-Disposition. */
    private String originalFilename;
    private String contentType;
    private Long fileSize;

    /** Required no-arg constructor for JPA. */
    protected Resource() {
    }

    /**
     * Metadata-only row with no backing file. Used by the seeder and tests; such
     * rows list normally but have no download (their {@link ResourceResponse#downloadUrl()} is null).
     */
    public Resource(String title, String course, String uploader, Instant uploadedAt) {
        this.title = title;
        this.course = course;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
    }

    /** Full row backed by a stored file, produced by an upload. */
    public Resource(String title, String course, String uploader, Instant uploadedAt,
            String storageKey, String originalFilename, String contentType, Long fileSize) {
        this(title, course, uploader, uploadedAt);
        this.storageKey = storageKey;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public String getUploader() {
        return uploader;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }
}
