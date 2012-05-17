package com.collibra.dgc.core.exceptions;

import java.io.Serializable;

public class InconsistentTaxonomyException extends ResourceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 700702392024577802L;

	public InconsistentTaxonomyException(String message, Serializable id, String clazz, String errorCode) {
		super(message, id, clazz, errorCode);
	}

	public InconsistentTaxonomyException(Serializable id, String clazz, String errorCode) {
		super("There is a taxonomical inconsistency", id, clazz, errorCode);
	}
}
