package com.example.app.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.example.app.resource.storage.FileStorage;

/**
 * Unit tests for the service layer. The repository and file storage are mocked
 * so these tests stay fast and free of any Spring / database / filesystem coupling.
 */
@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository repository;

    @Mock
    private FileStorage storage;

    @InjectMocks
    private ResourceServiceImpl service;

    private static MockMultipartFile pdf() {
        return new MockMultipartFile("file", "notes.pdf", "application/pdf", "pretend-pdf-bytes".getBytes());
    }

    @Test
    void getAllResources_mapsEntitiesToResponseDtos() {
        // Metadata-only row (no stored file) -> no download URL.
        Resource entity = new Resource(
                "Midterm 1 Practice Problems", "COSC 121", "Jane Doe",
                Instant.parse("2026-06-18T14:30:00Z"));
        when(repository.findAll()).thenReturn(List.of(entity));

        List<ResourceResponse> result = service.getAllResources();

        assertThat(result).hasSize(1);
        ResourceResponse dto = result.get(0);
        assertThat(dto.title()).isEqualTo("Midterm 1 Practice Problems");
        assertThat(dto.course()).isEqualTo("COSC 121");
        assertThat(dto.uploader()).isEqualTo("Jane Doe");
        assertThat(dto.uploadedAt()).isEqualTo(Instant.parse("2026-06-18T14:30:00Z"));
        assertThat(dto.downloadUrl()).isNull();
    }

    @Test
    void getAllResources_returnsEmptyListWhenNoResources() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(service.getAllResources()).isEmpty();
    }

    @Test
    void createResource_storesFileAndPersistsMetadata() {
        when(storage.store(any(), any())).thenReturn("generated-key.pdf");
        // Simulate JPA returning the saved entity back.
        when(repository.save(any(Resource.class))).thenAnswer(inv -> inv.getArgument(0));

        ResourceResponse dto = service.createResource(
                new NewResourceRequest("Midterm 1", "COSC 121", "Jane Doe", pdf()));

        assertThat(dto.title()).isEqualTo("Midterm 1");
        assertThat(dto.course()).isEqualTo("COSC 121");
        assertThat(dto.uploader()).isEqualTo("Jane Doe");
        assertThat(dto.uploadedAt()).isNotNull();
        verify(storage).store(any(), any());
        verify(repository).save(any(Resource.class));
    }

    @Test
    void createResource_rejectsBlankTitle() {
        assertThatThrownBy(() -> service.createResource(
                new NewResourceRequest("   ", "COSC 121", "Jane Doe", pdf())))
                .isInstanceOf(BadUploadException.class);
        verify(storage, never()).store(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void createResource_rejectsMissingFile() {
        assertThatThrownBy(() -> service.createResource(
                new NewResourceRequest("Midterm 1", "COSC 121", "Jane Doe", null)))
                .isInstanceOf(BadUploadException.class);
        verify(storage, never()).store(any(), any());
    }

    @Test
    void createResource_rejectsDisallowedFileType() {
        MockMultipartFile txt = new MockMultipartFile("file", "notes.txt", "text/plain", "hi".getBytes());
        assertThatThrownBy(() -> service.createResource(
                new NewResourceRequest("Midterm 1", "COSC 121", "Jane Doe", txt)))
                .isInstanceOf(BadUploadException.class);
        verify(storage, never()).store(any(), any());
    }

    @Test
    void loadForDownload_throwsWhenIdUnknown() {
        when(repository.findById(404L)).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> service.loadForDownload(404L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
