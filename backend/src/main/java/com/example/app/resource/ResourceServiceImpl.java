package com.example.app.resource;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.resource.storage.FileStorage;
import com.example.app.resource.storage.StoredFile;

/**
 * Default {@link ResourceService} implementation. Owns upload validation, maps
 * entities to {@link ResourceResponse} DTOs, delegates file bytes to
 * {@link FileStorage} and metadata to {@link ResourceRepository}.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    /** Accepted upload extensions, mapped to the content type served on download. */
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "pdf", "application/pdf",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    private final ResourceRepository repository;
    private final FileStorage storage;

    public ResourceServiceImpl(ResourceRepository repository, FileStorage storage) {
        this.repository = repository;
        this.storage = storage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getAllResources() {
        return repository.findAll().stream()
                .map(ResourceResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public ResourceResponse createResource(NewResourceRequest request) {
        String title = requireText(request.title(), "title");
        String course = requireText(request.course(), "course");
        String uploader = requireText(request.uploader(), "uploader");

        MultipartFile file = request.file();
        if (file == null || file.isEmpty()) {
            throw new BadUploadException("A file is required.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        extension = extension == null ? "" : extension.toLowerCase();
        if (!ALLOWED_TYPES.containsKey(extension)) {
            throw new BadUploadException(
                    "That file type isn't allowed. Accepted: " + String.join(", ", ALLOWED_TYPES.keySet()) + ".");
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BadUploadException("Could not read the uploaded file.");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "upload." + extension : file.getOriginalFilename());
        // Trust our own extension mapping over the client-declared content type.
        String contentType = ALLOWED_TYPES.get(extension);

        String storageKey = storage.store(bytes, originalFilename);

        Resource saved = repository.save(new Resource(
                title, course, uploader, Instant.now(),
                storageKey, originalFilename, contentType, (long) bytes.length));

        return ResourceResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StoredFile loadForDownload(Long id) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (resource.getStorageKey() == null) {
            // Metadata-only row (e.g. seed data) — nothing to download.
            throw new ResourceNotFoundException(id);
        }
        return new StoredFile(
                storage.load(resource.getStorageKey()),
                resource.getOriginalFilename(),
                resource.getContentType());
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BadUploadException("Missing required field: " + field);
        }
        return value.trim();
    }
}
