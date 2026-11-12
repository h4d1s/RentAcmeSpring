package com.pixellabs.user.domain.userAggregate.exceptions;

import java.io.Serial;

public class UserDomainException extends Exception {

	/**
	 *
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	public UserDomainException() {
		super();
	}

	public UserDomainException(String message, Throwable cause, boolean enableSuppression,
									  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDomainException(String message) {
		super(message);
	}

	public UserDomainException(Throwable cause) {
		super(cause);
	}
}
