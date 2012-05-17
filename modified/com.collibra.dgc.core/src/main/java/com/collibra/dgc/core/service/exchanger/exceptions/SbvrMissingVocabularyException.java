package com.collibra.dgc.core.service.exchanger.exceptions;

/**
 * Exception for an xmi that does not contain a package that can be used as a vocabulary.
 * 
 * @author dtrog
 * 
 */
public class SbvrMissingVocabularyException extends RuntimeException {

	public SbvrMissingVocabularyException() {
		super();
	}

	public SbvrMissingVocabularyException(String message, Throwable cause) {
		super(message, cause);
	}

	public SbvrMissingVocabularyException(String message) {
		super(message);
	}

	public SbvrMissingVocabularyException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8629603717122593713L;

}
