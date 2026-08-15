package com.example.app.resource;

import java.util.List;

import com.example.app.resource.storage.StoredFile;

/**
 * Service abstraction for reading and creating resources. The controller depends
 * on this interface rather than a concrete class, keeping the web layer loosely
 * coupled from the persistence and storage implementations.
 */
public interface ResourceService {

    /** Returns all resources as API response DTOs (empty list, never null). */
    List<ResourceResponse> getAllResources();

    /**
     * Validates the request, stores the file, persists the metadata, and returns
     * the created resource as a DTO.
     *
     * @throws BadUploadException if a field is missing or the file is empty/disallowed
     */
    ResourceResponse createResource(NewResourceRequest request);

    /**
     * Loads a resource's stored file for download.
     *
     * @throws ResourceNotFoundException if the id is unknown or has no backing file
     */
    StoredFile loadForDownload(Long id);
}
