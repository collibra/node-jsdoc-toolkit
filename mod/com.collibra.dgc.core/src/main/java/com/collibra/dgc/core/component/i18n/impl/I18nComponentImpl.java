package com.collibra.dgc.core.component.i18n.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.i18n.I18nComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.service.i18n.I18nService;
import com.collibra.dgc.core.service.i18n.Messages;
import com.collibra.dgc.core.util.Defense;

/**
 * Internationalization API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class I18nComponentImpl implements I18nComponent, InitializingBean {

	private static final String i18nDir = "/com/collibra/dgc/core/i18n/";

	@Autowired
	private I18nService i18nService;

	private final Map<String, Messages> localizationCacheMap;

	private Locale defaultLocale;

	public I18nComponentImpl() {
		this("en");
	}

	public I18nComponentImpl(String defaultLocaleString) {

		Defense.notEmpty(defaultLocaleString, DGCErrorCodes.I18N_LOCALE_STRING_NULL,
				DGCErrorCodes.I18N_LOCALE_STRING_EMPTY, "defaultLocaleString");

		this.defaultLocale = getLocale(defaultLocaleString);
		localizationCacheMap = new HashMap<String, Messages>();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// Cache the default locale
		doCache(defaultLocale);
	}

	@Override
	public String getMessage(String localeString, String key, Object... params) {

		Defense.notEmpty(localeString, DGCErrorCodes.I18N_LOCALE_STRING_NULL, DGCErrorCodes.I18N_LOCALE_STRING_EMPTY,
				"localeString");
		Defense.notEmpty(key, DGCErrorCodes.I18N_KEY_NULL, DGCErrorCodes.I18N_KEY_EMPTY, "key");

		Locale locale = getLocale(localeString);

		doCache(locale);

		return localizationCacheMap.get(locale.toString()).get(key, params);
	}

	@Override
	public String getDefaultLocalizedMessage(String key, Object... params) {

		Defense.notEmpty(key, DGCErrorCodes.I18N_KEY_NULL, DGCErrorCodes.I18N_KEY_EMPTY, "key");

		return localizationCacheMap.get(defaultLocale.toString()).get(key, params);
	}

	@Override
	public String getUserLocalization(HttpServletRequest req) {

		Defense.notNull(req, DGCErrorCodes.I18N_HTTP_SERVLET_REQUEST_NULL, "req");

		return i18nService.getUserLocalization(req, defaultLocale).toString();
	}

	@Override
	public void cleanCache() {
		Messages defaultLocalizationMessages = localizationCacheMap.remove(defaultLocale.toString());
		localizationCacheMap.clear();
		localizationCacheMap.put(defaultLocale.toString(), defaultLocalizationMessages);
	}

	@Override
	public void cache(String localeString) {

		Defense.notEmpty(localeString, DGCErrorCodes.I18N_LOCALE_STRING_NULL, DGCErrorCodes.I18N_LOCALE_STRING_EMPTY,
				"localeString");

		doCache(getLocale(localeString));
	}

	@Override
	public void setDefaultLocalization(String defaultLocaleString) {

		Defense.notEmpty(defaultLocaleString, DGCErrorCodes.I18N_LOCALE_STRING_NULL,
				DGCErrorCodes.I18N_LOCALE_STRING_EMPTY, "defaultLocaleString");

		Locale newDefaultLocale = getLocale(defaultLocaleString);

		// If the default locale is already set
		if (defaultLocale.equals(newDefaultLocale))
			return;

		// Else cache it
		defaultLocale = newDefaultLocale;
		doCache(defaultLocale);
	}

	@Override
	public Locale getDefaultLocalization() {
		return defaultLocale;
	}

	/**
	 * Execute the caching without defense
	 */
	private void doCache(Locale locale) {

		if (localizationCacheMap.containsKey(locale.toString()))
			return;

		// Try to cache the localized properties files and its overrides
		if (doCachePart(locale, null))
			return;

		// Try to cache the default localized properties files and its overrides
		if (!locale.equals(defaultLocale) && doCachePart(defaultLocale, locale))
			return;

		// Try to cache the English properties files and its overrides
		Locale englishLocale = new Locale("en");

		if (!englishLocale.equals(defaultLocale) && doCachePart(englishLocale, locale))
			return;
	}

	/**
	 * Check if it possible to cache the locale, after the language. If it is the case, it returns true, otherwise it
	 * returns false.
	 * 
	 * @param localeIO The locale to use for the {@link InputStream}
	 * @param localeToCache The locale to cache
	 */
	private boolean doCachePart(Locale localeIO, Locale localeToCache) {

		if (localeToCache == null)
			localeToCache = localeIO;

		// If the country is set
		if (localeIO.getCountry() != null && !localeIO.getCountry().isEmpty()) {

			// If it is an other locale to set and this other locale is already cached, use the cache
			if (!localeIO.equals(localeToCache) && localizationCacheMap.containsKey(localeIO.toString())) {

				localizationCacheMap.put(localeToCache.toString(), i18nService.createMessages(localeToCache,
						localizationCacheMap.get(localeIO.toString()).getMessagesMap()));
				return true;
			}

			// Get the localization file for language and country
			final InputStream inputStream = I18nComponentImpl.class
					.getResourceAsStream(getFilePath(localeIO.toString()));

			if (inputStream != null) {
				localizationCacheMap.put(localeToCache.toString(),
						i18nService.createMessages(localeToCache, inputStream));
				return true;
			}
		}

		// Try to get the locale from the language
		String language = localeIO.getLanguage();

		// If the language is already cached, use the cache
		if (localizationCacheMap.containsKey(language)) {

			localizationCacheMap.put(localeToCache.toString(),
					i18nService.createMessages(localeToCache, localizationCacheMap.get(language).getMessagesMap()));
			return true;

		}

		// Get the file for the language
		final InputStream inputStream = I18nComponentImpl.class.getResourceAsStream(getFilePath(language));

		if (inputStream != null) {
			localizationCacheMap.put(localeToCache.toString(), i18nService.createMessages(localeToCache, inputStream));
			return true;
		}

		return false;
	}

	/**
	 * Get the locale from the string.
	 */
	private Locale getLocale(String localeString) {
		// Replace "-" (RFC 2616 - HTTP 1.1) to "_" (Java Locale)
		return LocaleUtils.toLocale(localeString.replace("-", "_"));
	}

	/**
	 * Get the file path to the hardcoded locale properties file
	 */
	private String getFilePath(String fileName) {
		return i18nDir + fileName + ".properties";
	}
}
