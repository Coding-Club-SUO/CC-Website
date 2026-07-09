package com.example.app.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the service layer. The repository is mocked so these tests
 * stay fast and free of any Spring / database coupling.
 */
@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository repository;

    @InjectMocks
    private ResourceServiceImpl service;

    @Test
    void getAllResources_mapsEntitiesToResponseDtos() {
        Resource entity = new Resource(
                "Midterm 1 Practice Problems", "COSC 121", "Jane Doe",
                Instant.parse("2026-06-18T14:30:00Z"), "#");
        when(repository.findAll()).thenReturn(List.of(entity));

        List<ResourceResponse> result = service.getAllResources();

        assertThat(result).hasSize(1);
        ResourceResponse dto = result.get(0);
        assertThat(dto.title()).isEqualTo("Midterm 1 Practice Problems");
        assertThat(dto.course()).isEqualTo("COSC 121");
        assertThat(dto.uploader()).isEqualTo("Jane Doe");
        assertThat(dto.uploadedAt()).isEqualTo(Instant.parse("2026-06-18T14:30:00Z"));
        assertThat(dto.downloadUrl()).isEqualTo("#");
    }

    @Test
    void getAllResources_returnsEmptyListWhenNoResources() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(service.getAllResources()).isEmpty();
    }
}
