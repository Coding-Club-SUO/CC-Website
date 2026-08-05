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
 */
@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String course;
    private String uploader;
    private Instant uploadedAt;
    private String downloadUrl;

    /** Required no-arg constructor for JPA. */
    protected Resource() {
    }

    public Resource(String title, String course, String uploader, Instant uploadedAt, String downloadUrl) {
        this.title = title;
        this.course = course;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
        this.downloadUrl = downloadUrl;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
