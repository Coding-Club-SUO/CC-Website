package com.example.app.resource;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default {@link ResourceService} implementation. Delegates persistence to the
 * repository and maps entities to {@link ResourceResponse} DTOs.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;

    public ResourceServiceImpl(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getAllResources() {
        return repository.findAll().stream()
                .map(ResourceResponse::from)
                .toList();
    }
}
