package com.collibra.dgc.core.exceptions;

import java.io.Serializable;

/**
 * Exception that is thrown when a database constraint is violated while trying to save the resource.
 * @author dtrog
 * 
 */
public class ConstraintViolationException extends ResourceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5093408492947850262L;

	public ConstraintViolationException(String message, Serializable id, String clazz, String errorCode) {
		super(message, id, clazz, errorCode);
	}

	public ConstraintViolationException(Serializable id, String clazz, String errorCode) {
		super("A resource with the same natural key already exists", id, clazz, errorCode);
	}
}
