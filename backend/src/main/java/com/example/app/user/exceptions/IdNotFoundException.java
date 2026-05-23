package com.example.app.user.exceptions;

import java.io.Serial;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an {@link UserDetailsService} implementation cannot locate a {@link User} by
 * its email.
 *
 */
public class IdNotFoundException extends AuthenticationException{
        
	@Serial
	private static final long serialVersionUID = 1410688585992297006L;

	private static final String DEFAULT_USER_NOT_FOUND_MESSAGE = "id not found";
    
    private final @Nullable String name;


	/**
	 * Constructs a <code>IdNotFoundException</code> with the specified message.
	 * @param msg the detail message.
	 */
	public IdNotFoundException(String msg) {
		super(msg);
		this.name = null;
	}

	/**
	 * Constructs a {@code IdNotFoundException} with the specified message and root
	 * cause.
	 * @param msg the detail message.
	 * @param cause root cause
	 */
	public IdNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
		this.name = null;
	}

	private IdNotFoundException(String msg, String name) {
		super(msg);
		this.name = name;
	}

	private IdNotFoundException(String msg, String name, Throwable cause) {
		super(msg, cause);
		this.name = name;
	}

	/**
	 * Construct an exception based on a specific id
	 * @param id the invalid id
	 * @return the {@link IdNotFoundException}
	 * @since 7.0
	 */
	public static IdNotFoundException fromid(String id) {
		return new IdNotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE, id);
	}

	/**
	 * Construct an exception based on a specific id
	 * @param id the invalid id
	 * @param cause any underlying cause
	 * @return the {@link IdNotFoundException}
	 * @since 7.0
	 */
	public static IdNotFoundException fromid(String id, Throwable cause) {
		return new IdNotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE, id, cause);
	}

	/**
	 * Get the id that couldn't be found
	 * @return the id
	 * @since 7.0
	 */
	public @Nullable String getName() {
		return this.name;
	}

}
