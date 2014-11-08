package com.poorknight.exceptions;

public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -3165756825989850778L;


	public ReflectionException(final String message) {
		super(message);
	}


	public ReflectionException(final Throwable cause) {
		super(cause);
	}


	public ReflectionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
