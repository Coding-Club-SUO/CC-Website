package com.example.app.resource;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link Resource}. Provides {@code findAll}, {@code save},
 * {@code count}, etc. out of the box.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
