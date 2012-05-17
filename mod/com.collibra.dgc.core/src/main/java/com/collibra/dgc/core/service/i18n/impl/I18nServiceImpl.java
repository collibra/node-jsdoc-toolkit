/**
 * 
 */
package com.collibra.dgc.core.service.i18n.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.service.i18n.I18nService;
import com.collibra.dgc.core.service.i18n.Messages;
import com.collibra.dgc.core.util.FileMonitor;
import com.collibra.dgc.core.util.FileMonitor.EEvent;
import com.collibra.dgc.core.util.FileMonitor.IExecutor;

/**
 * @author dieterwachters
 * 
 */
@Service
public class I18nServiceImpl implements I18nService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(I18nServiceImpl.class);

	private static final Map<String, Properties> overrides = new HashMap<String, Properties>();
	public static long overridesLastModified = -1;
	private static final ReentrantReadWriteLock overridesLock = new ReentrantReadWriteLock();

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UserService userService;

	@Override
	public long getOverridesLastModified() {
		return overridesLastModified;
	}

	@Override
	public Properties getOverrides(String locale) {
		return overrides.get(locale);
	}

	/**
	 * Reread the translation overrides from the user home directory.
	 */
	private void refreshOverrides() {
		log.info("Refreshing the translation overrides as they seem changed");
		overridesLock.writeLock().lock();
		try {
			overrides.clear();
			if (!Application.TRANSLATIONS_DIR.exists()) {
				return;
			}
			final File[] translationFiles = Application.TRANSLATIONS_DIR.listFiles();
			for (final File translationFile : translationFiles) {
				if (translationFile.getName().contains(".")) {
					final String lang = translationFile.getName().substring(0,
							translationFile.getName().lastIndexOf("."));
					Properties props = new Properties();
					try {
						props.load(new FileInputStream(translationFile));
					} catch (Exception e) {
						log.error(
								"An error occurred while reading translation properties file '"
										+ translationFile.getAbsolutePath() + "'.", e);
					}
					overrides.put(lang, props);
				}
			}
			overridesLastModified = System.currentTimeMillis();
		} finally {
			overridesLock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			final int interval = configurationService.getInteger("core/i18n/overrides/polling-interval");
			log.info("Starting file monitor for translations in directory '"
					+ Application.TRANSLATIONS_DIR.getAbsolutePath() + "' with interval " + interval + ".");
			FileMonitor.startFileMonitor(Application.TRANSLATIONS_DIR, new IExecutor() {

				@Override
				public void run(File file, EEvent event) {
					refreshOverrides();
				}
			}, interval);
		} catch (Exception e) {
			log.error("Error while starting file monitor on '" + Application.TRANSLATIONS_DIR.getAbsolutePath() + "'.",
					e);
		}
		refreshOverrides();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.i18n.TranslationService#createMessages(java.util.Locale)
	 */
	@Override
	public Messages createMessages(Locale locale) {
		return new Messages(this, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.i18n.TranslationService#createMessages(java.util.Locale, java.io.InputStream)
	 */
	@Override
	public Messages createMessages(Locale locale, InputStream input) {
		return new Messages(this, locale, input);
	}

	@Override
	public Messages createMessages(Locale locale, Map<String, String> messages) {
		return new Messages(this, locale, messages);
	}

	@Override
	public Locale getUserLocalization(HttpServletRequest req, Locale defaultLocale) {

		final UserData currentUser = userService.getCurrentUser();

		// First check if there is an override in the URL
		if (req.getParameter("lang") != null) {

			try {

				final Locale locale = LocaleUtils.toLocale(req.getParameter("lang"));
				req.getSession().setAttribute("locale", locale);

				if (currentUser != null)
					userService.changeLanguage(currentUser.getUserName(), locale.toString());

				return locale;

			} catch (IllegalArgumentException ex) {
				// Do nothing
			}
		}

		// Check if the locale is not in the session already.
		if (req.getSession().getAttribute("locale") != null) {

			return (Locale) req.getSession().getAttribute("locale");
		}

		// Check if this user has a preferred language.
		if (currentUser != null && currentUser.getLanguage() != null) {

			try {

				final Locale locale = LocaleUtils.toLocale(currentUser.getLanguage());
				req.getSession().setAttribute("locale", locale);

				return locale;

			} catch (IllegalArgumentException ex) {
				// Do nothing
			}
		}

		// Check if the Accept-Language is set
		if (req.getHeader("Accept-Language") != null) {

			// Cf. RFC 2616 - HTTP 1.1 (http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html)
			String[] acceptLanguageArray = req.getHeader("Accept-Language").split(",");

			if (acceptLanguageArray.length > 0) {

				acceptLanguageArray = acceptLanguageArray[0].replace("-", "_").split(";");

				if (acceptLanguageArray.length > 0) {

					try {

						final Locale locale = LocaleUtils.toLocale(acceptLanguageArray[0]);
						req.getSession().setAttribute("locale", locale);

						return locale;

					} catch (IllegalArgumentException ex) {
						// Do nothing
					}
				}
			}
		}

		// Check if the default local is null
		if (defaultLocale != null)
			return defaultLocale;

		// The locale cannot be set -> English ('en') is set as the locale
		return new Locale("en");
	}

	@Override
	public Locale getUserLocalization(HttpServletRequest req) {
		return getUserLocalization(req, new Locale("en"));
	}
}
