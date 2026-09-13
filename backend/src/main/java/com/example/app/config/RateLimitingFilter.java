/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author rashi
 */
@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private static final Set<String> AUTH_PATHS = Set.of(
            "/api/v1/login",
            "/api/v1/register"
    );

    // Create a bucket configuration: 3 requests per minute, refilling 3 tokens every minute
    private Bucket createNewAuthBucket() {
        return Bucket.builder()
            .addLimit(limit -> limit
                    .capacity(3)
                    .refillIntervally(3, Duration.ofMinutes(1)))
            .build();
    }
    
    // Create a bucket configuration: 10 requests per minute, refilling 10 tokens every minute
    private Bucket createNewBucket() {
        return Bucket.builder()
            .addLimit(limit -> limit
                    .capacity(10)
                    .refillIntervally(10, Duration.ofMinutes(1)))
            .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Target only sensitive endpoints like authentication
        if (AUTH_PATHS.contains(httpRequest.getRequestURI())) {
            String ip = httpRequest.getRemoteAddr();
            Bucket bucket = cache.computeIfAbsent(ip, k -> createNewAuthBucket());

            // Try to consume 1 token from the client's bucket
            if (!bucket.tryConsume(1)) {
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Too many requests. Please try again later.\"}");
                return; // Block the request pipeline
            }
        }

        chain.doFilter(request, response);
    }
}
