/**
 * 
 */
package com.collibra.dgc.ui.core.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.ui.core.resources.ResourceServlet;

/**
 * Structure representing all the files and data needed for a module.
 * @author dieterwachters
 */
public class Module {
	private static final Logger log = LoggerFactory.getLogger(Module.class);

	private static final String FALLBACK_LANGUAGE = "en";
	private static final String PROPERTY_DEPENDENCIES = "dependencies";
	private static final String PROPERTY_NAMESPACE = "namespace";
	private static final String PROPERTY_OPTIMIZE_EXCEPTIONS = "optimize.exceptions";
	private static final String PROPERTY_JAVASCRIPT_EXTERNS = "javascript.externs";
	private static final String PROPERTY_GROUP = "group";

	private final long lastmodified;
	private final String name;
	private final String template;
	private final String properties;
	private final List<String> javascripts;
	private final List<String> stylesheets;
	private final Map<String, String> translations;
	private final String urlPath;
	private final ConfigurationService config;

	private Properties propertiesCache;
	private long propertiesReadAt = 0;

	private Map<String, String> configProperties;
	private Map<String, Object> configHierarchicalProperties;
	private Map<String, Properties> translationsCache;
	private Set<String> optimizeExceptionsCache;

	public Module(final String name, final String urlPath, final String properties, final String template,
			final long lastmodified, List<String> javascript, List<String> stylesheets,
			Map<String, String> translations, final ConfigurationService config) {
		this.lastmodified = lastmodified;
		this.urlPath = urlPath;
		this.properties = properties;
		this.name = name;
		this.template = template;
		this.javascripts = javascript;
		this.stylesheets = stylesheets;
		this.translations = translations;
		this.config = config;
	}

	/**
	 * Retrieve the properties map if they are specified.
	 * @return The properties map.
	 */
	public synchronized Properties getPropertiesMap() {
		if (propertiesCache == null || config.getLastModified() > propertiesReadAt) {
			propertiesReadAt = System.currentTimeMillis();
			optimizeExceptionsCache = null;
			configProperties = null;
			configHierarchicalProperties = null;
			propertiesCache = new Properties();
			if (properties != null) {
				try {
					propertiesCache.load(ResourceServlet.getFileData(properties));

					String path = name;
					if (path.startsWith("/")) {
						path = path.substring(1);
					}
					if (path.endsWith("/")) {
						path = path.substring(0, path.length() - 1);
					}
					final Map<String, String> configProps = config.getProperties("ui/modules/" + path + "/properties");
					if (configProps != null) {
						propertiesCache.putAll(configProps);
					}
				} catch (IOException e) {
					log.error("Error while reading properties of module '" + name + "'.");
				}
			}
		}
		return propertiesCache;
	}

	public long getPropertiesLastReadAt() {
		return propertiesReadAt;
	}

	private String findLanguage(Locale locale) {
		String lang = locale.toString();
		while (!translations.containsKey(lang)) {
			if (lang.contains("_")) {
				lang = lang.substring(0, lang.lastIndexOf("_"));
			} else if (translations.containsKey(FALLBACK_LANGUAGE)) {
				return FALLBACK_LANGUAGE;
			} else {
				return null;
			}
		}

		return lang;
	}

	/**
	 * Retrieve the properties map if they are specified.
	 * @return The properties map.
	 */
	public synchronized Properties getTranslationsMap(Locale locale) {
		if (translations == null) {
			return null;
		}
		final String lang = findLanguage(locale);
		if (lang == null) {
			return null;
		}

		if (translationsCache == null) {
			translationsCache = new HashMap<String, Properties>();
		}
		if (translationsCache.containsKey(lang)) {
			return translationsCache.get(lang);
		}
		final Properties props = new Properties();
		try {
			props.load(ResourceServlet.getFileData(translations.get(lang)));
			translationsCache.put(lang, props);
		} catch (IOException e) {
			log.error("Error while reading properties of module '" + name + "'.");
		}
		return props;
	}

	/**
	 * @return The map of configuration properties (which can be overridden by the configuration.
	 */
	public synchronized Map<String, String> getConfigurationProperties() {
		if (configProperties == null || config.getLastModified() > propertiesReadAt) {
			final Properties props = getPropertiesMap();
			configProperties = new HashMap<String, String>();
			for (final Object keyObj : props.keySet()) {
				final String key = (String) keyObj;
				if (!key.startsWith(PROPERTY_DEPENDENCIES) && !key.equals(PROPERTY_NAMESPACE)
						&& !key.equals(PROPERTY_OPTIMIZE_EXCEPTIONS) && !key.equals(PROPERTY_JAVASCRIPT_EXTERNS)
						&& !key.equals(PROPERTY_GROUP)) {
					configProperties.put(key, props.getProperty(key));
				}
			}
			// make the hierarchy.
			configHierarchicalProperties = makeHierarchy(configProperties);
		}
		return configProperties;
	}

	public String getProperty(final String property) {
		getConfigurationProperties();
		return configProperties.get(property);
	}

	/**
	 * @return The hierarchical map of configuration properties for this module. The hierarchy is split on the '.' (dot)
	 *         character in the property keys.
	 */
	public Map<String, Object> getHierarchicalConfiguration() {
		// Making sure we update first if needed.
		getConfigurationProperties();
		return configHierarchicalProperties;
	}

	/**
	 * @return The namespace of the module, which can be used to prepend the
	 */
	public String getNamespace() {
		final Properties props = getPropertiesMap();
		final String namespace = props.getProperty(PROPERTY_NAMESPACE);
		return namespace == null ? "" : namespace;
	}

	/**
	 * Returns the externs code to use for optimizing.
	 */
	public String getExterns() {
		final Properties properties = getPropertiesMap();
		if (properties.containsKey(PROPERTY_JAVASCRIPT_EXTERNS)) {
			String e = properties.getProperty(PROPERTY_JAVASCRIPT_EXTERNS);
			// If empty we also return null.
			return e == null || e.trim().length() == 0 ? null : e;
		}
		return null;
	}

	/**
	 * @return the name of the group this module belongs too. If not set (default group) null will be returned.
	 */
	public String getGroup() {
		final Properties properties = getPropertiesMap();
		return properties.getProperty(PROPERTY_GROUP, null);
	}

	/**
	 * Finds the modules on which this module depends.
	 * @return A map with as key the part in which the modules (given in the value of the map) need to be inserted.
	 */
	public List<String> getDependentModules() {
		final List<String> dependencies = new ArrayList<String>();
		final Properties properties = getPropertiesMap();
		final String depProp = properties.getProperty(PROPERTY_DEPENDENCIES);
		if (depProp != null) {
			final String[] modules = depProp.split(",");
			for (String module : modules) {
				module = module.trim();
				if (module.startsWith("/")) {
					module = module.substring(1);
				}
				if (!module.isEmpty()) {
					dependencies.add(module);
				}
			}
		}
		return dependencies;
	}

	/**
	 * Gather the list files that should not be optimized.
	 */
	public synchronized Set<String> getOptimizeExceptions() {
		if (optimizeExceptionsCache == null || config.getLastModified() > propertiesReadAt) {
			final Properties properties = getPropertiesMap();
			optimizeExceptionsCache = new HashSet<String>();

			final String opts = properties.getProperty(PROPERTY_OPTIMIZE_EXCEPTIONS);
			if (opts != null && opts.length() > 0) {
				final String[] optA = opts.split(",");
				for (String opt : optA) {
					opt = opt.trim();
					if (opt.length() > 0) {
						optimizeExceptionsCache.add(urlPath + opt);
					}
				}
			}
		}
		return optimizeExceptionsCache;
	}

	/**
	 * @return The short name of the widgets, which is only the part after the last /. For example 'my-widget-1'
	 */
	public final String getShortName() {
		return name.contains("/") ? name.substring(name.lastIndexOf("/") + 1) : name;
	}

	/**
	 * @return the full name of the module. For example 'widgets/my-widget-1'
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the template
	 */
	public final String getTemplate() {
		return template;
	}

	/**
	 * @return the javascripts
	 */
	public final List<String> getJavascripts() {
		return javascripts;
	}

	/**
	 * @return the stylesheets
	 */
	public final List<String> getStylesheets() {
		return stylesheets;
	}

	/**
	 * @return the translations
	 */
	public final Map<String, String> getTranslations() {
		return translations;
	}

	/**
	 * @return the lastmodified
	 */
	public final long getLastmodified() {
		return lastmodified;
	}

	/**
	 * @return the properties
	 */
	public final String getProperties() {
		return properties;
	}

	/**
	 * @return the urlPath
	 */
	public String getUrlPath() {
		return urlPath;
	}

	@Override
	public String toString() {
		return "[Module: " + name + "]";
	}

	public static final Map<String, Object> makeHierarchy(final Map<String, String> props) {
		final Map<String, Object> ret = new HashMap<String, Object>();
		for (String key : props.keySet()) {
			final String origKey = key;
			String value = props.get(key);
			Map<String, Object> currentMap = ret;
			while (true) {
				String part = key;
				if (part.contains(".")) {
					part = part.substring(0, part.indexOf("."));
					key = key.substring(key.indexOf(".") + 1);

					Object existing = currentMap.get(part);
					if (existing == null) {
						Map<String, Object> newMap = new HashMap<String, Object>();
						currentMap.put(part, newMap);
						currentMap = newMap;
					} else if (existing instanceof Map) {
						currentMap = (Map<String, Object>) existing;
					} else {
						throw new IllegalArgumentException("Property '" + origKey
								+ "' points to both a hierarchy as a string value.");
					}
				} else {
					if (currentMap.containsKey(part) && !(currentMap.get(part) instanceof String)) {
						throw new IllegalArgumentException("Property '" + origKey
								+ "' points to both a hierarchy as a string value.");
					}
					currentMap.put(part, value);
					break;
				}
			}
		}

		return ret;
	}
}