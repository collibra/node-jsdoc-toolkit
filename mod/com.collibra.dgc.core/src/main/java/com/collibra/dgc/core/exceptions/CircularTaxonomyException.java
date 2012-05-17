package com.collibra.dgc.core.exceptions;

import java.io.Serializable;

/**
 * This exception should be thrown when a circular taxonomy is being persisted. For example if B specializes A, and then
 * you make A specialize B again, that should trigger this exception.
 * 
 * @author dtrog
 * 
 */
public class CircularTaxonomyException extends InconsistentTaxonomyException {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -4234382787457971911L;

	public CircularTaxonomyException(String message, Serializable id, String clazz) {
		super(message, id, clazz, DGCErrorCodes.CIRCULAR_TAXONOMY);
	}

	public CircularTaxonomyException(Serializable id, String clazz) {
		super("Circular taxonomies are not allowed", id, clazz, DGCErrorCodes.CIRCULAR_TAXONOMY);
	}

}
