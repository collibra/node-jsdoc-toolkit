/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.ExpressionEngine;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.service.ConfigurationService;

/**
 * Implementation of the configuration service
 * @author dieterwachters
 */
@Component
public class ConfigurationServiceImpl implements InitializingBean, ConfigurationService {
	private final Logger log = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	private long lastModified = System.currentTimeMillis();

	private final CompositeConfiguration config;
	private XMLConfiguration saveConfig;
	private static final ExpressionEngine expressionEngine = new XPathExpressionEngine();

	@SuppressWarnings("unused")
	@Autowired
	private Application application;

	public ConfigurationServiceImpl() {
		HierarchicalConfiguration.setDefaultExpressionEngine(expressionEngine);
		config = new CompositeConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		final File configFile = new File(Application.CONFIG_DIR, "configuration.xml");
		log.info("Using configuration file " + configFile.getAbsolutePath());
		try {
			saveConfig = new XMLConfiguration();
			saveConfig.setListDelimiter('|');
			saveConfig.setFile(configFile);
			if (!configFile.createNewFile())
				saveConfig.load();
			final FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
			strategy.setRefreshDelay(10000);
			saveConfig.setReloadingStrategy(strategy);
			saveConfig.addConfigurationListener(new ConfigurationListener() {
				public void configurationChanged(ConfigurationEvent event) {
					updateLastModified();
				}
			});

			config.addConfiguration(saveConfig);

			contributeDefaults(getClass().getResource("/com/collibra/dgc/core/default-config.xml"));

			updateLastModified();
		} catch (Exception e) {
			log.error("Error while reading configuration file '" + configFile.getAbsolutePath() + "'.", e);
			throw e;
		}
	}

	private final void updateLastModified() {
		lastModified = System.currentTimeMillis();
	}

	public void contributeDefaults(final URL url) {
		try {
			final XMLConfiguration defaultConfig = new XMLConfiguration(url);
			defaultConfig.setListDelimiter('|');
			defaultConfig.setURL(url);
			defaultConfig.load();
			config.addConfiguration(defaultConfig);
			log.debug("Added configuration defaults '" + url.toExternalForm() + "'.");
		} catch (ConfigurationException e) {
			log.error("Error while contributing the defaults '" + url.toExternalForm() + "'.", e);
		}
	}

	public String getString(String path) {

		return getStringInternal(appendContextPath(path));
	}

	@Override
	public String getString(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";
		
		return getStringInternal(appendContextPath(path, resourceId, userName));
	}


	@Override
	public String getLocalizedString(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		Iterator<String> prioritizedPaths = buildPrioritizedContextPaths(path, resourceId, userName).iterator();
		String result = null;
		while (prioritizedPaths.hasNext()) {
			result = getStringInternal(prioritizedPaths.next());
			if (result != null)
				return result;
		}
		return null;
	}

	protected String getStringInternal(String path) {
		return config.getString(path);
	}

	@Override
	public Map<String, String> getConfigurationSection(String startPath) {
		Map<String, String> result = new TreeMap<String, String>();

		Iterator keys = config.getKeys("//" + startPath);
		while (keys.hasNext()) {
			String key = ((String) keys.next()).substring(2);
			result.put(key, getString(key));
		}

		return result;
	}

	@Override
	public Map<String, String> getConfigurationSection(String startPath, String resourceId, UserData user) {
		Map<String, String> result = new TreeMap<String, String>();

		Iterator keys = config.getKeys("//" + startPath);
		while (keys.hasNext()) {
			String key = ((String) keys.next()).substring(2);
			result.put(key, getString(key, resourceId, user));
		}

		return result;
	}

	public Integer getInteger(String path) {
		return getIntegerInternal(appendContextPath(path));
	}

	public Integer getInteger(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";
		return getIntegerInternal(appendContextPath(path, resourceId, userName));
	}

	public Integer getLocalizedInteger(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		Iterator<String> prioritizedPaths = buildPrioritizedContextPaths(path, resourceId, userName).iterator();
		Integer result = null;
		while (prioritizedPaths.hasNext()) {
			result = getIntegerInternal(prioritizedPaths.next());
			if (result != null)
				return result;
		}
		return null;
	}

	protected Integer getIntegerInternal(String path) {
		return config.getInteger(path, null);
	}

	@Override
	public Boolean getBoolean(String path) {
		return getBooleanInternal(appendContextPath(path));
	}

	@Override
	public Boolean getBoolean(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";
		return getBooleanInternal(appendContextPath(path, resourceId, userName));
	}


	@Override
	public Boolean getLocalizedBoolean(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		Iterator<String> prioritizedPaths = buildPrioritizedContextPaths(path, resourceId, userName).iterator();
		Boolean result = null;
		while (prioritizedPaths.hasNext()) {
			result = getBooleanInternal(prioritizedPaths.next());
			if (result != null)
				return result;
		}
		return null;
	}

	protected Boolean getBooleanInternal(String path) {
		return config.getBoolean(path, null);
	}

	@SuppressWarnings("unchecked")
	public List<String> getStringList(String path) {
		return getStringListInternal(appendContextPath(path));
	}

	@Override
	public List<String> getStringList(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";
		return getStringListInternal(appendContextPath(path, resourceId, userName));
	}

	@Override
	public List<String> getLocalizedStringList(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		Iterator<String> prioritizedPaths = buildPrioritizedContextPaths(path, resourceId, userName).iterator();
		List<String> result = null;
		while (prioritizedPaths.hasNext()) {
			result = getStringListInternal(prioritizedPaths.next());
			if (result != null)
				return result;
		}
		return null;
	}

	protected List<String> getStringListInternal(String path) {
		return config.getList(path);
	}

	@Override
	public long getLastModified() {
		return lastModified;
	}

	@Override
	public Map<String, String> getProperties(String path) {
		return getPropertiesInternal(appendContextPath(path));
	}

	@Override
	public Map<String, String> getProperties(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";
		return getPropertiesInternal(appendContextPath(path, resourceId, userName));
	}

	@Override
	public Map<String, String> getLocalizedProperties(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		Iterator<String> prioritizedPaths = buildPrioritizedContextPaths(path, resourceId, userName).iterator();
		Map<String, String> result = null;
		while (prioritizedPaths.hasNext()) {
			result = getPropertiesInternal(prioritizedPaths.next());
			if (result != null)
				return result;
		}
		return null;
	}

	public Map<String, String> getPropertiesInternal(String path) {
		final Map<String, String> ret = new HashMap<String, String>();
		String sv = config.getString(path);
		if (sv != null) {
			Properties props = new Properties();
			try {
				props.load(new StringReader(sv));
				for (final Object keyObj : props.keySet()) {
					ret.put((String) keyObj, props.getProperty((String) keyObj));
				}
			} catch (IOException e) {
				log.error("Error while reading properties format of configuration path '" + path + "'. The value: "
						+ sv);
			}
		}
		return ret;
	}

	@Override
	public void clear(String path) {
		clearInternal(appendContextPath(path));
	}

	@Override
	public void clear(String path, String resourceId, UserData user) {
		String userName = user != null ? user.getUserName() : "";

		clearInternal(appendContextPath(path, resourceId, userName));
	}

	protected void clearInternal(String path) {
		saveConfig.clearProperty(path);
	}

	@Override
	public void setProperty(String path, Object value) {
		setPropertyInternal(appendContextPath(path), value);
	}

	protected void setPropertyInternal(String path, Object value) {
		saveConfig.setProperty(path, value);
	}

	@Override
	public void setProperty(String path, Object value, UserData user) {
		setPropertyInternal(appendContextPath(path, null, user.getUserName()), value);
	}

	@Override
	public void setProperty(String path, Object value, String resourceId) {
		setPropertyInternal(appendContextPath(path, resourceId, null), value);
	}

	@Override
	public void setProperty(String path, Object value, String resourceId, UserData user) {
		if (user == null)
			setProperty(path, value, resourceId);
		else
			setPropertyInternal(appendContextPath(path, resourceId, user.getUserName()), value);
	}

	@Override
	public void flush() {
		try {
			saveConfig.save();
		} catch (ConfigurationException e) {
			log.error("Error while saving the configuration.", e);
		}
	}

	protected String appendContextPath(String path, String resourceId, String user) {
		if ((resourceId == null || resourceId.isEmpty()) && (user == null || user.isEmpty())) {
			return appendContextPath(path);
		} else if (resourceId == null || resourceId.isEmpty()) {
			return "preferences/users/" + user + "/" + path;
		} else if (user == null || user.isEmpty()) {
			return "preferences/resources/r" + resourceId + "/" + path;
		} else {
			return "preferences/resources/r" + resourceId + "/users/" + user + "/" + path;
		}
	}

	protected List<String> buildPrioritizedContextPaths(String path, String resourceId, String user) {
		List<String> prioritizedPaths = new LinkedList<String>();

		if (resourceId != null && !(user == null || user.isEmpty()))
			prioritizedPaths.add(appendContextPath(path, resourceId, user));

		if (!(user == null || user.isEmpty()))
			prioritizedPaths.add(appendContextPath(path, null, user));

		if (resourceId != null)
			prioritizedPaths.add(appendContextPath(path, resourceId, null));

		prioritizedPaths.add(appendContextPath(path, null, null));

		return prioritizedPaths;
	}

	protected String appendContextPath(String path) {
		if (path.startsWith("/"))
			return "system" + path;
		else
			return "system/" + path;
	}
}
