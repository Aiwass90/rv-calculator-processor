package com.cnh.rvcalculatorprocessor.exception;

public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private String message;
	private String code;

	private static final long serialVersionUID = -5098388476470339301L;

	public ApplicationException() {
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, String code) {

		this.message = message;
		this.code = code;
	}
}