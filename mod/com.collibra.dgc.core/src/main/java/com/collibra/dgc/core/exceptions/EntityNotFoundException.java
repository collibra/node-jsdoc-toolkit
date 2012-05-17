package com.collibra.dgc.core.exceptions;


/**
 * {@link Exception} that is thrown when the entity with specified natural key cannot be found.
 * @author amarnath
 * 
 */
public class EntityNotFoundException extends DGCException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8240719611615692235L;

	public EntityNotFoundException(Exception ex, String errorCode, Object... params) {
		super(ex, errorCode, params);
	}

	public EntityNotFoundException(String errorCode, Object... params) {
		super(errorCode, params);
	}
}
