package com.example.app.resource;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds a small set of sample resources on startup so the frontend Resources
 * page shows real backend data. The insert is guarded by a count check, so it
 * is idempotent across restarts (which reuse the same schema via ddl-auto=update).
 *
 * <p>Seeding is enabled by default but can be turned off — e.g. in production, to
 * avoid injecting placeholder data into the real database — by setting
 * {@code app.seed-sample-resources=false}.
 *
 * <p>These are metadata-only rows with no backing file, so they list normally but
 * expose no download link (the frontend renders them as unavailable). Real,
 * downloadable rows come from uploads via {@code POST /api/resources}.
 */
@Configuration
public class ResourceSeeder {

    @Bean
    @ConditionalOnProperty(name = "app.seed-sample-resources", havingValue = "true", matchIfMissing = true)
    CommandLineRunner seedResources(ResourceRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    new Resource("Midterm 1 Practice Problems", "COSC 121", "Jane Doe",
                            Instant.parse("2026-06-18T14:30:00Z")),
                    new Resource("Final Exam Review Sheet", "MATH 151", "John Smith",
                            Instant.parse("2026-06-20T09:00:00Z")),
                    new Resource("Lab 3 Solutions", "PHYS 101", "Alice Chen",
                            Instant.parse("2026-06-22T16:45:00Z")),
                    new Resource("Regression Cheat Sheet", "STAT 260", "Sam Lee",
                            Instant.parse("2026-06-24T11:15:00Z"))));
        };
    }
}
