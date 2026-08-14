package com.example.app.resource;

import java.util.List;

/**
 * Service abstraction for reading resources. The controller depends on this
 * interface rather than a concrete class, keeping the web layer loosely coupled
 * from the persistence implementation.
 */
public interface ResourceService {

    /** Returns all resources as API response DTOs. */
    List<ResourceResponse> getAllResources();
}
