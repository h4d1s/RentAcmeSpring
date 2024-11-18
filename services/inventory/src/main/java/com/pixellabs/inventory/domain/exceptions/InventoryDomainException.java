package com.pixellabs.inventory.domain.exceptions;

public class InventoryDomainException extends Exception {

	/**
	 * 
	 */
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
