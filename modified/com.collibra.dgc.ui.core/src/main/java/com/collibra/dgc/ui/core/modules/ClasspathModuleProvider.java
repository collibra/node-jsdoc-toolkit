/**
 * 
 */
package com.collibra.dgc.ui.core.modules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.service.ConfigurationService;

/**
 * Implementation of an {@link IModuleProvider} that will get its data from classpath.
 * @author dieterwachters
 */
@Component
public class ClasspathModuleProvider implements IModuleProvider {
	private static final Logger log = LoggerFactory.getLogger(ClasspathModuleProvider.class);

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ConfigurationService configurationService;

	private static final String TRANSLATIONS_FOLDER = "i18n";
	public static final String PREFIX = "internal/";

	private final Map<String, Module> cache = new HashMap<String, Module>();

	public Module getModule(String path) {
		String name = path;
		if (name.contains("/")) {
			name = name.substring(name.lastIndexOf("/") + 1);
		}

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		if (cache.containsKey(path)) {
			return cache.get(path);
		}

		final String basePath = "/" + path + "/";

		final String fullPath = basePath + name + ".vm";
		if (getClass().getResource(fullPath) != null) {
			try {
				long lastmodified = Application.DEVELOPER ? 0 : Application.STARTUP_DATE;
				List<String> javascripts = null;
				List<String> stylesheets = null;
				String template = null;
				String properties = null;
				Map<String, String> translations = null;

				final String prefix = PREFIX + path + "/";

				Resource[] resources = applicationContext.getResources("classpath:" + basePath + "*");
				for (final Resource resource : resources) {
					if (Application.DEVELOPER) {
						lastmodified = Math.max(lastmodified, resource.lastModified());
					}
					final String fname = prefix + resource.getFilename();
					if (fname.endsWith(".js")) {
						if (javascripts == null)
							javascripts = new ArrayList<String>();
						javascripts.add(fname);
					} else if (fname.endsWith(".css") || fname.endsWith(".less")) {
						if (stylesheets == null)
							stylesheets = new ArrayList<String>();
						stylesheets.add(fname);
					} else if (fname.endsWith(".vm")) {
						template = path + "/" + resource.getFilename();
					} else if (fname.endsWith(".properties")) {
						properties = fname;
					}
				}

				try {
					resources = applicationContext.getResources("classpath:" + basePath + TRANSLATIONS_FOLDER + "/*");
					for (final Resource resource : resources) {
						if (translations == null) {
							translations = new HashMap<String, String>();
						}
						if (Application.DEVELOPER) {
							lastmodified = Math.max(lastmodified, resource.lastModified());
						}
						String tname = resource.getFilename();
						tname = tname.substring(0, tname.lastIndexOf("."));
						translations.put(tname, prefix + TRANSLATIONS_FOLDER + "/" + resource.getFilename());
					}
				} catch (FileNotFoundException e) {
					// The translation folder doesn't exist.
				}

				if (template != null) {
					final Module module = new Module(path, prefix, properties, template, lastmodified, javascripts,
							stylesheets, translations, configurationService);
					if (!Application.DEVELOPER) {
						cache.put(path, module);
					}
					return module;
				}
			} catch (IOException e) {
				log.error("Error while looking for classpath resources in '" + basePath + "'.", e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getLastModified(java.lang.String)
	 */
	public long getLastModified(String path) {
		if (Application.DEVELOPER) {
			final Resource resource = applicationContext.getResource("classpath:" + path);
			if (resource != null) {
				try {
					return resource.lastModified();
				} catch (IOException e) {
				}
			}
		}
		return Application.STARTUP_DATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String path) throws IOException {
		if (path.startsWith(PREFIX)) {
			path = path.substring(PREFIX.length());
			return ClasspathModuleProvider.class.getResourceAsStream("/" + path);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getOrder()
	 */
	@Override
	public int getOrder() {
		return 100;
	}
}
