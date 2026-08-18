package com.example.app.user.exceptions;

/**
 * Thrown if an {@link UserDetailsService} implementation cannot locate a {@link User} by
 * its email.
 *
 */
public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String email) {
        super("User with email '" + email + "' was not found");
    }
}