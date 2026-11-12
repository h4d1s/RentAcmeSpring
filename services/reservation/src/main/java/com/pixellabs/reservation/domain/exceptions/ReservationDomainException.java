package com.pixellabs.reservation.domain.exceptions;

import java.io.Serial;

public class ReservationDomainException extends Exception {

	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	public ReservationDomainException() {
		super();
	}

	public ReservationDomainException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReservationDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReservationDomainException(String message) {
		super(message);
	}

	public ReservationDomainException(Throwable cause) {
		super(cause);
	}
}
