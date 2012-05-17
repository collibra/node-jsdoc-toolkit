/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.i18n.I18nService;
import com.collibra.dgc.ui.core.UICore;
import com.collibra.dgc.ui.core.modules.Module;
import com.collibra.dgc.ui.core.modules.ModuleService;

/**
 * Represents a page definition.
 * @author dieterwachters
 */
public class PageDefinition extends Node {
	private String moduleNotFound;

	public static final String CONFIG_OPTIMIZE_JAVASCRIPT = UICore.CONFIG_PATH + "/optimize/javascript";
	public static final String CONFIG_OPTIMIZE_CSS = UICore.CONFIG_PATH + "/optimize/css";

	private static final Logger log = LoggerFactory.getLogger(PageDefinition.class);
	protected final String id = UUID.randomUUID().toString();

	protected String description;
	protected ModuleConfiguration module;
	protected final List<MatchingRule> rules = new ArrayList<MatchingRule>();

	// The calculated modules.
	protected final List<Module> moduleList = new ArrayList<Module>();
	protected final Map<String, Module> moduleMap = new HashMap<String, Module>();
	protected final Map<String, Object> staticProperties = new HashMap<String, Object>();

	private final Map<String, PageDefinitionInstance> instances = new HashMap<String, PageDefinitionInstance>();

	private boolean builtIn = false;

	private long lastInitialized = 0;
	private long lastChecked = 0;
	private boolean lastOptimizeCSS;
	private boolean lastOptimizeJS;
	// Only check for updates once a minute (or less).
	private static final long UPDATE_TIMEOUT = 60000;

	/**
	 * Constructor
	 * @param name The name of this page definition.
	 */
	public PageDefinition(String name, final String description) {
		super(null, null, name);
		this.description = description;
	}

	public boolean getOptimizeCSS() {
		return lastOptimizeCSS;
	}

	public boolean getOptimizeJS() {
		return lastOptimizeJS;
	}

	/**
	 * Returns the model that was not found during initialization.
	 * @return
	 */
	public String getModuleNotFound() {
		return moduleNotFound;
	}

	public void setBuiltIn(final boolean builtIn) {
		this.builtIn = builtIn;
	}

	public boolean isBuiltIn() {
		return builtIn;
	}

	/**
	 * Get the description of this page definition.
	 * @return The description of this page definition.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set the module configuration for this page definition.
	 * @param module The module configuration for this page definition.
	 */
	public void setModuleConfiguration(final ModuleConfiguration module) {
		this.module = module;
	}

	/**
	 * Retrieve the module configuration for this page definition.
	 * @return The module configuration for this page definition.
	 */
	public ModuleConfiguration getModuleConfiguration() {
		return module;
	}

	/**
	 * Add a new matching rule to the list.
	 * @param rule The new matching rule to add.
	 */
	public void addMatchingRule(final MatchingRule rule) {
		rules.add(rule);
	}

	/**
	 * Retrieve the matching rules for this page definition.
	 * @return The matching rules for this page definition.
	 */
	public List<MatchingRule> getMatchingRules() {
		return rules;
	}

	/**
	 * Returns the sub part of this page definition to which the path points to.
	 * @param path The path which points to a sub part of this page definition.
	 * @return The new page definition. The name and description will be the same as the original. The module will point
	 *         to the right one in the tree and the rules will be empty. This will return this object when the requested
	 *         path is null or empty.
	 */
	public PageDefinition getSubPart(final String path) throws PageDefinitionException {
		if (path == null || path.isEmpty()) {
			return this;
		}
		final Node subNode = findMatchingNode(path);
		if (subNode != null) {
			final PageDefinition def = new PageDefinition(name, description);
			def.module = new ModuleConfiguration(def, def, "pages/shared/bare");
			final List<ModuleConfiguration> modules = subNode instanceof ModuleConfiguration ? Arrays
					.asList((ModuleConfiguration) subNode) : ((Region) subNode).getModuleConfigurations();
			def.module.addRegion(new Region(def, def.module, "content", modules));
			return def;
		}
		throw new PageDefinitionException("No subpart '" + path + "' found for page definition '" + name + "'.");
	}

	@Override
	public String getPath() {
		return module.getPath();
	}

	@Override
	public Node findMatchingNode(String path) throws PageDefinitionException {
		return module.findMatchingNode(path);
	}

	/**
	 * @return The unique id of this page definition.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Retrieve a page definition instance, which is a page definition for a specific locale (since this has different
	 * JS).
	 */
	public synchronized PageDefinitionInstance getPageDefinitionInstance(I18nService i18nService, Locale locale,
			final String context, final String excludedModules) throws PageDefinitionException {
		List<String> em = null;

		String key = locale.toString();
		if (excludedModules != null && !excludedModules.isEmpty()) {
			em = new ArrayList<String>();
			em.addAll(Arrays.asList(excludedModules.split(",")));
			Collections.sort(em);
			key += "-" + em.toString();
		}
		PageDefinitionInstance instance = instances.get(key);
		// Note: no need to check that the translations have been changes, since the messages class will handle this.
		if (instance == null) {
			instance = new PageDefinitionInstance(i18nService, this, locale, lastOptimizeJS, lastOptimizeCSS, context,
					em);
			instances.put(key, instance);
		}
		return instance;
	}

	/**
	 * Will initialize this page definition by gathering all the dependent modules.
	 */
	public synchronized void initialize(final ModuleService moduleService, final ConfigurationService config)
			throws PageDefinitionException {
		boolean needToInitialize = lastInitialized == 0;
		if (!needToInitialize && (System.currentTimeMillis() > lastChecked + UPDATE_TIMEOUT)) {
			for (final Module module : moduleList) {
				if (module.getLastmodified() < moduleService.getModuleLastModified(module.getName())) {
					needToInitialize = true;
					break;
				}
			}
			lastChecked = System.currentTimeMillis();
		}
		if (!needToInitialize
				&& (lastOptimizeCSS != config.getBoolean(CONFIG_OPTIMIZE_CSS) || lastOptimizeJS != config
						.getBoolean(CONFIG_OPTIMIZE_JAVASCRIPT))) {
			needToInitialize = true;
		}

		if (needToInitialize) {
			lastOptimizeCSS = config.getBoolean(CONFIG_OPTIMIZE_CSS);
			lastOptimizeJS = config.getBoolean(CONFIG_OPTIMIZE_JAVASCRIPT);
			instances.clear();
			moduleMap.clear();
			moduleList.clear();

			accept(new ModuleGatherer(moduleService));
			gatherProperties();

			lastInitialized = System.currentTimeMillis();
		}
	}

	private class ModuleGatherer extends AbstractPageDefinitionVisitor {
		private final ModuleService moduleService;

		public ModuleGatherer(final ModuleService moduleService) {
			this.moduleService = moduleService;
		}

		@Override
		public boolean visit(ModuleConfiguration moduleConfig) throws PageDefinitionException {
			final String moduleName = moduleConfig.getName();
			if (!moduleMap.containsKey(moduleName)) {
				final Module module = moduleService.findModule(moduleName);

				if (module != null) {
					findDependencies(moduleService, module);
					if (!moduleList.contains(module)) {
						moduleList.add(module);
					}
					moduleMap.put(moduleName, module);
					if (module.getProperty("resolve.regions") != null
							&& "false".equalsIgnoreCase(module.getProperty("resolve.regions"))) {
						return false;
					}
				} else {
					throw new PageDefinitionException("Module '" + moduleName + "' not found.");
				}
			}
			return true;
		}

		/**
		 * This method will recursively find all the dependent modules and add them to the given maps accordingly.
		 */
		private void findDependencies(final ModuleService moduleService, final Module parent) {
			final List<String> dependentModules = parent.getDependentModules();

			if (dependentModules != null && !dependentModules.isEmpty()) {
				for (final String moduleName : dependentModules) {
					if (!moduleList.contains(moduleName)) {
						final Module module = moduleService.findModule(moduleName);
						if (module != null) {
							findDependencies(moduleService, module);
						} else {
							log.error("Module '" + moduleName + "' could not be found.");
						}
					}
				}
			}
			moduleMap.put(parent.getName(), parent);
			moduleList.add(parent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.pagedefinition.model.Node#accept(com.collibra.dgc.ui.core.pagedefinition.model.
	 * PageDefinitionVisitor)
	 */
	@Override
	public void accept(PageDefinitionVisitor visitor) throws PageDefinitionException {
		if (visitor.visit(this)) {
			module.accept(visitor);
			if (rules != null) {
				for (final MatchingRule rule : rules) {
					rule.accept(visitor);
				}
			}
		}
	}

	public List<Module> getModules() {
		return moduleList;
	}

	public Map<String, Module> getModuleMap() {
		return moduleMap;
	}

	/**
	 * @return The hierarchical map representing all the static (defined in properties files of the modules) properties.
	 */
	public final Map<String, Object> getStaticProperties() {
		return staticProperties;
	}

	/**
	 * Helper method to gather all the properties of the modules.
	 */
	private final void gatherProperties() {
		staticProperties.clear();
		for (final Module module : moduleList) {
			mergeProperties(staticProperties, module.getHierarchicalConfiguration());
		}
	}

	/**
	 * Merge two hierarchical maps.
	 */
	private final void mergeProperties(final Map<String, Object> dest, final Map<String, Object> input) {
		if (input != null) {
			for (final String key : input.keySet()) {
				final Object value = input.get(key);
				final Object origValue = dest.get(key);
				if (value instanceof String) {
					if (origValue instanceof Map) {
						log.warn("Property '" + key + "' points to both a String value as a hierarchy.");
					} else {
						dest.put(key, value);
					}
				} else if (value instanceof Map) {
					if (origValue == null) {
						dest.put(key, value);
					} else if (origValue instanceof Map) {
						mergeProperties((Map) origValue, (Map) value);
					} else {
						log.warn("Property '" + key + "' points to both a String value as a hierarchy.");
					}
				}
			}
		}
	}
}
