package com.pixellabs.inventory.domain.exceptions;

import java.io.Serial;

public class InventoryDomainException extends Exception {

	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	public InventoryDomainException() {
		super();
	}

	public InventoryDomainException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InventoryDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public InventoryDomainException(String message) {
		super(message);
	}

	public InventoryDomainException(Throwable cause) {
		super(cause);
	}
}
