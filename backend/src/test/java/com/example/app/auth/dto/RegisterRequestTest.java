package com.example.app.auth.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class RegisterRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validRequest_passesValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user-1",
                "valid.email@example.com",
                "securePassword123",
                true
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void blankUsername_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "   ",
                "valid@example.com",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username")
                && v.getMessage().equals("Username is required"));
    }

    @Test
    void usernameTooShort_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "ab",
                "valid@example.com",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username")
                && v.getMessage().equals("Username must be between 3 and 30 characters"));
    }

    @Test
    void usernameTooLong_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "a".repeat(31),
                "valid@example.com",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username")
                && v.getMessage().equals("Username must be between 3 and 30 characters"));
    }

    @Test
    void usernameWithDisallowedCharacters_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "invalid user!",
                "valid@example.com",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username")
                && v.getMessage().equals("Username can only contain letters, numbers, underscores, and hyphens"));
    }

    @Test
    void blankEmail_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                "   ",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("Email is required"));
    }

    @Test
    void invalidEmailFormat_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                "not-an-email",
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("Email must be a valid email address"));
    }

    @Test
    void emailTooLong_failsValidation() {
        String longEmail = "a".repeat(243) + "@example.com";
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                longEmail,
                "securePassword123",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("Email cannot exceed 254 characters"));
    }

    @Test
    void blankPassword_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                "valid@example.com",
                "   ",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().equals("Password is required"));
    }

    @Test
    void passwordTooShort_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                "valid@example.com",
                "short7",
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().equals("Password must be between 8 and 128 characters"));
    }

    @Test
    void passwordTooLong_failsValidation() {
        RegisterRequest request = new RegisterRequest(
                "valid_user",
                "valid@example.com",
                "a".repeat(129),
                false
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().equals("Password must be between 8 and 128 characters"));
    }
}

