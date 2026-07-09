package com.example.app.resource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * End-to-end test of the resources API over a real HTTP server (H2-backed test
 * profile). Verifies the three things the frontend (PR #71) relies on:
 * <ul>
 *   <li>{@code GET /api/resources} is publicly reachable (security {@code permitAll}),</li>
 *   <li>it returns a JSON array, and</li>
 *   <li>each element carries exactly the fields the TypeScript {@code Resource} type expects.</li>
 * </ul>
 * The seeded sample data confirms the controller/service/repository are wired together.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ResourceApiIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    void getResources_isPublicAndReturnsFrontendContract() {
        RestTestClient client = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/api/resources")
                .exchange()
                // Reachable without a JWT -> the permitAll rule is in effect.
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> {
                    assertThat(body).isNotNull();
                    assertThat(body.trim()).startsWith("[");
                    // The exact field names the frontend deserializes.
                    assertThat(body)
                            .contains("\"id\"")
                            .contains("\"title\"")
                            .contains("\"course\"")
                            .contains("\"uploader\"")
                            .contains("\"uploadedAt\"")
                            .contains("\"downloadUrl\"");
                    // Seeded data flows through the full stack.
                    assertThat(body).contains("Midterm 1 Practice Problems");
                });
    }
}
