package com.poorknight.exceptions;

/**
 * Runtime exception to be used if DAO class needs to throw an exception.
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public DaoException(final String exceptionDescription) {
		super(exceptionDescription);
	}


	public DaoException(final Throwable cause) {
		super(cause);
	}


	public DaoException(final String exceptionDescription, final Throwable cause) {
		super(exceptionDescription, cause);
	}
}
