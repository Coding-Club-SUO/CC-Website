package com.example.app.user.exceptions;

import java.io.Serial;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an {@link UserDetailsService} implementation cannot locate a {@link User} by
 * its email.
 *
 */
public class EmailNotFoundException extends AuthenticationException{
    
	@Serial
	private static final long serialVersionUID = 1410688585992297006L;

	private static final String DEFAULT_USER_NOT_FOUND_MESSAGE = "email not found";
    
    private final @Nullable String name;


	/**
	 * Constructs a <code>EmailNotFoundException</code> with the specified message.
	 * @param msg the detail message.
	 */
	public EmailNotFoundException(String msg) {
		super(msg);
		this.name = null;
	}

	/**
	 * Constructs a {@code EmailNotFoundException} with the specified message and root
	 * cause.
	 * @param msg the detail message.
	 * @param cause root cause
	 */
	public EmailNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
		this.name = null;
	}

	private EmailNotFoundException(String msg, String name) {
		super(msg);
		this.name = name;
	}

	private EmailNotFoundException(String msg, String name, Throwable cause) {
		super(msg, cause);
		this.name = name;
	}

	/**
	 * Construct an exception based on a specific email
	 * @param email the invalid email
	 * @return the {@link EmailNotFoundException}
	 * @since 7.0
	 */
	public static EmailNotFoundException fromEmail(String email) {
		return new EmailNotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE, email);
	}

	/**
	 * Construct an exception based on a specific email
	 * @param email the invalid email
	 * @param cause any underlying cause
	 * @return the {@link EmailNotFoundException}
	 * @since 7.0
	 */
	public static EmailNotFoundException fromEmail(String email, Throwable cause) {
		return new EmailNotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE, email, cause);
	}

	/**
	 * Get the email that couldn't be found
	 * @return the email
	 * @since 7.0
	 */
	public @Nullable String getName() {
		return this.name;
	}

}
