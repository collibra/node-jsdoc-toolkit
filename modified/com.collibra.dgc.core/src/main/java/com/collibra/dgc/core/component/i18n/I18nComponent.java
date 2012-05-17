package com.collibra.dgc.core.component.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * Public API for internationalization.
 * 
 * @author pmalarme
 * 
 */
public interface I18nComponent {

	/**
	 * Get the message for the given key, using the given parameters and localization.
	 * 
	 * <br />
	 * <br />
	 * If the localized (e.g. fr_FR) message cannot be found for the key, it looks at the message for the same language
	 * (e.g. fr). If this message is not found, it check the default localization of the message and after at the
	 * English ('en') version of the message.
	 * 
	 * @param localeString The identifier for a particular combination of language and region
	 * @param key The key of the message
	 * @param params The parameters of the message (e.g. the {@link DGCErrorCode#TERM_NOT_FOUND})
	 * @return {@link String} message
	 */
	String getMessage(String localeString, String key, Object... params);

	/**
	 * Get the message for the given key, using the given parameters and the default localization.
	 * 
	 * <br />
	 * <br />
	 * If the localized (e.g. fr_FR) message cannot be found for the key, it looks at the message for the same language
	 * (e.g. fr). If this message is not found, it check the default localization of the message and after at the
	 * English ('en') version of the message.
	 * 
	 * @param key The key of the message (e.g. the {@link DGCErrorCode#TERM_NOT_FOUND})
	 * @param params
	 * @return {@link String message}
	 */
	String getDefaultLocalizedMessage(String key, Object... params);

	/**
	 * Get the localization of the current logged-in user.
	 * 
	 * @param req The {@link HttpServletRequest} of the current HTTP Request
	 * @return {@link Locale}
	 */
	String getUserLocalization(HttpServletRequest req);

	/**
	 * Clean the localization cache except for the default localization.
	 */
	void cleanCache();

	/**
	 * Cache a specific localization.
	 * 
	 * @param localeString The identifier for a particular combination of language and region
	 */
	void cache(String localeString);

	/**
	 * Set the default localeString to be used.
	 * 
	 * <br />
	 * <br />
	 * Initially the default localization is set to English ('en').
	 * 
	 * @param localeString
	 */
	void setDefaultLocalization(String defaultLocaleString);

	/**
	 * Get the default localization.
	 * 
	 * @return The default {@link Locale}
	 */
	Locale getDefaultLocalization();
}
