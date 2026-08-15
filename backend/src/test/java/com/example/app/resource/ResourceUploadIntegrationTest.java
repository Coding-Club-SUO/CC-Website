package com.example.app.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.jayway.jsonpath.JsonPath;

/**
 * End-to-end test of the upload → list → download loop over a real HTTP server
 * (H2 for metadata, an isolated temp directory for file bytes). This is the whole
 * point of the vertical slice: a file goes up and comes back clickable.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = "app.seed-sample-resources=false")
class ResourceUploadIntegrationTest {

    /** Isolated storage root so the test never writes into the real ./uploads dir. */
    private static Path storageRoot;

    @DynamicPropertySource
    static void storageProperties(DynamicPropertyRegistry registry) throws IOException {
        storageRoot = Files.createTempDirectory("cc-uploads-test");
        registry.add("app.storage.location", () -> storageRoot.toString());
    }

    @AfterAll
    static void cleanUp() {
        if (storageRoot != null) {
            FileSystemUtils.deleteRecursively(storageRoot.toFile());
        }
    }

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ResourceRepository repository;

    private RestTestClient client;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        client = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    /** Builds a multipart form; a null part is simply omitted (to exercise missing-field cases). */
    private static MultiValueMap<String, Object> multipart(byte[] content, String filename, MediaType type,
            String title, String course, String uploader) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        if (content != null) {
            ByteArrayResource filePart = new ByteArrayResource(content) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(type);
            parts.add("file", new HttpEntity<>(filePart, fileHeaders));
        }
        if (title != null) {
            parts.add("title", title);
        }
        if (course != null) {
            parts.add("course", course);
        }
        if (uploader != null) {
            parts.add("uploader", uploader);
        }
        return parts;
    }

    @Test
    void upload_thenList_thenDownload_roundTrip() {
        byte[] content = "%PDF-1.4 pretend pdf body".getBytes();

        AtomicReference<String> created = new AtomicReference<>();
        client.post().uri("/api/resources")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipart(content, "midterm.pdf", MediaType.APPLICATION_PDF,
                        "Midterm 1 Practice Problems", "COSC 121", "Jane Doe"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).value(created::set);

        String body = created.get();
        Integer id = JsonPath.read(body, "$.id");
        String downloadUrl = JsonPath.read(body, "$.downloadUrl");
        // downloadUrl is a backend-issued path derived from the id, not a filesystem location.
        assertThat(downloadUrl).isEqualTo("/api/resources/" + id + "/download");
        assertThat((String) JsonPath.read(body, "$.title")).isEqualTo("Midterm 1 Practice Problems");
        assertThat((String) JsonPath.read(body, "$.uploader")).isEqualTo("Jane Doe");

        // It shows up in the list with the same download URL.
        AtomicReference<String> listBody = new AtomicReference<>();
        client.get().uri("/api/resources")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(listBody::set);
        assertThat((String) JsonPath.read(listBody.get(), "$[0].title"))
                .isEqualTo("Midterm 1 Practice Problems");
        assertThat((String) JsonPath.read(listBody.get(), "$[0].downloadUrl"))
                .isEqualTo(downloadUrl);

        // And the file comes back, byte-for-byte, as an attachment.
        client.get().uri(downloadUrl)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueMatches(HttpHeaders.CONTENT_DISPOSITION, ".*attachment.*midterm\\.pdf.*")
                .expectBody(byte[].class).isEqualTo(content);
    }

    @Test
    void download_missingId_returns404WithErrorShape() {
        AtomicReference<String> err = new AtomicReference<>();
        client.get().uri("/api/resources/99999/download")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(err::set);
        assertThat(err.get()).contains("\"error\"");
    }

    @Test
    void upload_disallowedType_returns400WithErrorShape() {
        AtomicReference<String> err = new AtomicReference<>();
        client.post().uri("/api/resources")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipart("hi".getBytes(), "notes.txt", MediaType.TEXT_PLAIN,
                        "X", "Y", "Z"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).value(err::set);
        assertThat(err.get()).contains("\"error\"");
    }

    @Test
    void upload_missingUploader_returns400() {
        client.post().uri("/api/resources")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipart("body".getBytes(), "notes.pdf", MediaType.APPLICATION_PDF,
                        "X", "Y", null))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
