package com.example.app.auth.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validLoginRequest_passesValidation() {
        LoginRequest request = new LoginRequest(
                "valid_user@example.com",
                "password123",
                true
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void blankIdentifier_failsValidation() {
        LoginRequest request = new LoginRequest(
                "   ",
                "password123",
                false
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("identifier")
                && v.getMessage().equals("Identifier is required"));
    }

    @Test
    void identifierTooLong_failsValidation() {
        LoginRequest request = new LoginRequest(
                "a".repeat(255),
                "password123",
                false
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("identifier")
                && v.getMessage().equals("Identifier cannot exceed 254 characters"));
    }

    @Test
    void blankPassword_failsValidation() {
        LoginRequest request = new LoginRequest(
                "valid_user",
                "   ",
                false
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().equals("Password is required"));
    }

    @Test
    void passwordTooLong_failsValidation() {
        LoginRequest request = new LoginRequest(
                "valid_user",
                "a".repeat(129),
                false
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().equals("Password cannot exceed 128 characters"));
    }
}

