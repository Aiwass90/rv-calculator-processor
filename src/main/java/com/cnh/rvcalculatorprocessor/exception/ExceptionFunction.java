package com.cnh.rvcalculatorprocessor.exception;

public class ExceptionFunction extends Exception {

	/**
	 * 
	 */
	private String message;
	private String code;

	private static final long serialVersionUID = -5098388476470339301L;

	public ExceptionFunction() {
	}

	public ExceptionFunction(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionFunction(String message) {
		super(message);
	}

	public ExceptionFunction(Throwable cause) {
		super(cause);
	}

	public ExceptionFunction(String message, String code) {

		this.message = message;
		this.code = code;
	}
}