package com.example.app.resource;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.resource.storage.StoredFile;

/**
 * REST controller (the "C" in MVC) for course resources: list, upload, download.
 * Consumed by the frontend Resources and Upload pages.
 *
 * <ul>
 *   <li>{@code GET  /api/resources} — list all resources as JSON.</li>
 *   <li>{@code POST /api/resources} — multipart upload, returns 201 + the created object.</li>
 *   <li>{@code GET  /api/resources/{id}/download} — the raw file as an attachment.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<ResourceResponse> getResources() {
        return resourceService.getAllResources();
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> uploadResource(
            @RequestParam String title,
            @RequestParam String course,
            @RequestParam String uploader,
            @RequestParam("file") MultipartFile file) {
        ResourceResponse created = resourceService.createResource(
                new NewResourceRequest(title, course, uploader, file));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadResource(@PathVariable Long id) {
        StoredFile stored = resourceService.loadForDownload(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(stored.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + stored.filename() + "\"")
                .body(stored.body());
    }
}
