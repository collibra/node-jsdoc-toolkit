package com.collibra.dgc.core.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.collibra.dgc.core.component.i18n.I18nComponent;

/**
 * Generic DGC Exception.
 * 
 * @author pmalarme
 * @author amarnath
 */
public class DGCException extends RuntimeException {

	private static final long serialVersionUID = -7265530687165013498L;
	protected final String errorCode;
	protected Object[] params;

	public DGCException(Exception ex, String errorCode, Object... params) {
		super(ex);
		this.errorCode = errorCode;
		this.params = params;
	}

	public DGCException(String errorCode, Object... params) {
		super(errorCode);
		this.errorCode = errorCode;
		this.params = params;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object[] getParams() {
		return params;
	}

	/**
	 * Get the localized message for the exception.
	 * 
	 * @param locale The identifier for a particular combination of language and region
	 * @param i18nComponent The {@link I18nComponent}
	 * @return The localized message
	 */
	public String getLocalizedMessage(String locale, I18nComponent i18nComponent) {
		return i18nComponent.getMessage(locale, errorCode, params);
	}

	/**
	 * Get the exception's message using the default localization of {@link I18nComponent}.
	 * 
	 * @param i18nComponent The {@link I18nComponent}
	 * @return The localized message
	 */
	public String getLocalizedMessage(I18nComponent i18nComponent) {
		return i18nComponent.getDefaultLocalizedMessage(errorCode, params);
	}

	/**
	 * Get the exception's message using the current user localization.
	 * 
	 * @param i18nComponent The {@link I18nComponent}
	 * @param req The {@link HttpServletRequest}
	 * @return The localized message
	 */
	public String getLocalizedMessage(I18nComponent i18nComponent, HttpServletRequest req) {
		return i18nComponent.getMessage(i18nComponent.getUserLocalization(req), errorCode, params);
	}
}
