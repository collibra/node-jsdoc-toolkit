package com.collibra.dgc.core.exceptions;

/**
 * Exception for input validation check and constraint check.
 * 
 * @author pmalarme
 * @author amarnath
 * 
 */
public class IllegalArgumentException extends DGCException {

	private static final long serialVersionUID = 9108989798345933445L;

	public IllegalArgumentException(Exception ex, String errorCode, Object... params) {
		super(ex, errorCode, params);
	}

	public IllegalArgumentException(String errorCode, Object... params) {
		super(errorCode, params);
	}
}
