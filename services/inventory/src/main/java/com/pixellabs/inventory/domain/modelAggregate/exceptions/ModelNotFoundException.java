package com.pixellabs.inventory.domain.modelAggregate.exceptions;

public class ModelNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModelNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public ModelNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ModelNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ModelNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ModelNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
