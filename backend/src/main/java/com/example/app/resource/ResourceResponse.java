package com.example.app.resource;

import java.time.Instant;

/**
 * API-facing DTO returned by {@link ResourceController}. Its component names map
 * one-to-one onto the TypeScript {@code Resource} type consumed by the frontend:
 * { id, title, course, uploader, uploadedAt, downloadUrl }.
 *
 * <p>Keeping this separate from the {@link Resource} entity decouples the public
 * contract from the database schema — the entity can change without breaking the API.
 *
 * <p>{@code downloadUrl} is <em>derived</em> from the id ({@code /api/resources/{id}/download}),
 * never a raw filesystem location, and is null for metadata-only rows that have no
 * backing file — the frontend renders those as unavailable.
 */
public record ResourceResponse(
        Long id,
        String title,
        String course,
        String uploader,
        Instant uploadedAt,
        String downloadUrl) {

    /** Maps a persistence entity onto its API representation. */
    public static ResourceResponse from(Resource resource) {
        String downloadUrl = resource.getStorageKey() == null
                ? null
                : "/api/resources/" + resource.getId() + "/download";
        return new ResourceResponse(
                resource.getId(),
                resource.getTitle(),
                resource.getCourse(),
                resource.getUploader(),
                resource.getUploadedAt(),
                downloadUrl);
    }
}
