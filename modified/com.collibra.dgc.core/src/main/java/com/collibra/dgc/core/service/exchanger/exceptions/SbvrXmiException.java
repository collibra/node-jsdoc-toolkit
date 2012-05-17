package com.collibra.dgc.core.service.exchanger.exceptions;

public class SbvrXmiException extends RuntimeException {

	/**
	 * Standard XMI exception for whatever went wrong while traversing the XMI.
	 */
	private static final long serialVersionUID = -8532656370785014086L;

	public SbvrXmiException() {
		super();
	}

	public SbvrXmiException(String message, Throwable cause) {
		super(message, cause);
	}

	public SbvrXmiException(String message) {
		super(message);
	}

	public SbvrXmiException(Throwable cause) {
		super(cause);
	}

}
