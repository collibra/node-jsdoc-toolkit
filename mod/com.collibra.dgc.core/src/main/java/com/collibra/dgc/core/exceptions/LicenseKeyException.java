package com.collibra.dgc.core.exceptions;

/**
 * Exception class used for throwing when license is not valid.
 * @author amarnath
 * @author pmalarme
 * 
 */
public class LicenseKeyException extends DGCException {

	private static final long serialVersionUID = -5805909540741407997L;

	public LicenseKeyException(Exception ex, String errorCode, Object... params) {
		super(ex, errorCode, params);
	}

	public LicenseKeyException(String errorCode, Object... params) {
		super(errorCode, params);
	}
}
