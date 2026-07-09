package com.example.app.resource;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds a small set of sample resources on startup so the frontend Resources
 * page shows real backend data. The insert is guarded by a count check, so it
 * is idempotent across restarts (which reuse the same schema via ddl-auto=update).
 */
@Configuration
public class ResourceSeeder {

    @Bean
    CommandLineRunner seedResources(ResourceRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    new Resource("Midterm 1 Practice Problems", "COSC 121", "Jane Doe",
                            Instant.parse("2026-06-18T14:30:00Z"), "#"),
                    new Resource("Final Exam Review Sheet", "MATH 151", "John Smith",
                            Instant.parse("2026-06-20T09:00:00Z"), "#"),
                    new Resource("Lab 3 Solutions", "PHYS 101", "Alice Chen",
                            Instant.parse("2026-06-22T16:45:00Z"), "#"),
                    new Resource("Regression Cheat Sheet", "STAT 260", "Sam Lee",
                            Instant.parse("2026-06-24T11:15:00Z"), "#")));
        };
    }
}
