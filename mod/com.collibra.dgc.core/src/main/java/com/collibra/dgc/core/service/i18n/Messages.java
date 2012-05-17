/**
 * 
 */
package com.collibra.dgc.core.service.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The object containing messages.
 * @author dieterwachters
 */
public class Messages {
	private static final Logger log = LoggerFactory.getLogger(Messages.class);

	private final I18nService i18nService;
	private final Locale locale;
	private Properties localOverrides = null;
	private final Map<String, String> messages;
	private long overridesLastTaken = -1;

	public Messages(I18nService i18nService, Locale locale) {
		this.i18nService = i18nService;
		this.locale = locale;
		this.messages = new HashMap<String, String>();
	}

	/**
	 * Create a translations object and immediately read the properties in the given input stream.
	 */
	public Messages(I18nService i18nService, Locale locale, InputStream input) {
		this.i18nService = i18nService;
		this.locale = locale;
		this.messages = new HashMap<String, String>();

		final Properties props = new Properties();
		try {
			props.load(input);
			for (final Object key : props.keySet()) {
				messages.put((String) key, props.getProperty((String) key));
			}
		} catch (IOException e) {
			log.error("Error while reading translation messages.", e);
		}
	}

	/**
	 * Create a translations object and immediately read the properties in the given map.
	 */
	public Messages(I18nService i18nService, Locale locale, Map<String, String> messages) {
		this.i18nService = i18nService;
		this.locale = locale;
		this.messages = messages;
	}

	/**
	 * Add a translation.
	 */
	public void put(String key, String value) {
		messages.put(key, value);
	}

	/**
	 * Get the translated key and replace the parameters.
	 * @param key The key to lookup.
	 * @param params The parameters to replace (optional).
	 * @return The translated string or the key itself if not found.
	 */
	public String get(String key, Object[] params) {
		checkOverrides();
		if (localOverrides != null && localOverrides.containsKey(key)) {
			return MessageFormat.format((String) localOverrides.get(key), params);
		}

		// Not present? just return the key.
		if (!messages.containsKey(key)) {
			return key;
		}
		return MessageFormat.format(messages.get(key), params);
	}

	/**
	 * Get the messages map.
	 */
	public Map<String, String> getMessagesMap() {
		return messages;
	}

	/**
	 * Checks if the overrides have been changed in the meanwhile.
	 */
	private void checkOverrides() {
		final long overridesLastModified = i18nService.getOverridesLastModified();
		if (overridesLastModified > overridesLastTaken) {
			// First we check the overrides.
			localOverrides = i18nService.getOverrides(locale.toString());
			if (localOverrides == null) {
				localOverrides = i18nService.getOverrides(locale.getLanguage());
			}
			if (localOverrides == null) {
				localOverrides = i18nService.getOverrides("en");
			}

			overridesLastTaken = overridesLastModified;
		}
	}
}
