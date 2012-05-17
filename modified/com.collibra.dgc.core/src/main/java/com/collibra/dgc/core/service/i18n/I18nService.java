/**
 * 
 */
package com.collibra.dgc.core.service.i18n;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * The service for handling translations. It supports override from the user home directory.
 * @author dieterwachters
 */
public interface I18nService {
	/**
	 * Gets the timestamp of when the overrides where last updated.
	 */
	public long getOverridesLastModified();

	/**
	 * Get the translation-overrides with the given locale
	 * @param locale The locale (string representation) to get the overrides for.
	 * @return The map of overrides or null if not found.
	 */
	public Properties getOverrides(String locale);

	/**
	 * Create {@link Messages} for the specified localization takes the content of the file contained in the
	 * translations folder.
	 * 
	 * @param locale The identifier for a particular combination of language and region as a {@link Locale} object
	 * @return {@link Messages}
	 */
	public Messages createMessages(Locale locale);

	/**
	 * Create {@link Messages} for the specified localization that takes the content of the {@link InputStream} and
	 * overrides it with the content of the file from the translations folder.
	 * 
	 * @param locale The identifier for a particular combination of language and region as a {@link Locale} object
	 * @return {@link Messages}
	 */
	public Messages createMessages(Locale locale, final InputStream input);

	/**
	 * Create {@link Messages} for the specified localization that takes the content of the {@link Map} and overrides it
	 * with the content of the file from the translations folder.
	 * 
	 * @param locale The identifier for a particular combination of language and region as a {@link Locale} object
	 * @return {@link Messages}
	 */
	public Messages createMessages(Locale locale, final Map<String, String> messages);

	/**
	 * Get the localization of the current user.
	 * 
	 * @param req The {@link HttpServletRequest} of the current HTTP Request
	 * @return {@link Locale}
	 */
	public Locale getUserLocalization(HttpServletRequest req);

	/**
	 * Get the localization of the current user and if it not set, use the defaultLocale.
	 * 
	 * @param req The {@link HttpServletRequest} of the current HTTP Request
	 * @param defaultLocale The default locale for DGC.
	 * @return {@link locale}
	 */
	public Locale getUserLocalization(HttpServletRequest req, Locale defaultLocale);
}
