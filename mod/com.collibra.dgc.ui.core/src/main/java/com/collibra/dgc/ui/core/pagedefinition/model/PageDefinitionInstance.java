/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.service.i18n.I18nService;
import com.collibra.dgc.core.service.i18n.Messages;
import com.collibra.dgc.core.util.Pair;
import com.collibra.dgc.ui.core.UICore;
import com.collibra.dgc.ui.core.modules.Module;
import com.collibra.dgc.ui.core.resources.CSSCompiler;
import com.collibra.dgc.ui.core.resources.JavaScriptOptimizer;

/**
 * Contains all the data for a page definition instance. This will contain the javascript and css files that need to be
 * included for this page definition + locale.
 * @author dieterwachters
 */
public class PageDefinitionInstance {
	private static final Logger log = LoggerFactory.getLogger(PageDefinitionInstance.class);

	private static final String GLOBAL_LESS = "library/less/global.less";

	private final boolean optimizeCSS;
	private final boolean optimizeJS;

	private final Map<String, List<String>> javascriptFiles;
	private final Map<String, List<String>> cssFiles;
	private final Messages messages;

	private final I18nService i18nService;

	/**
	 * Constructor
	 */
	public PageDefinitionInstance(final I18nService i18nService, final PageDefinition pageDefinition,
			final Locale locale, final boolean optimizeJS, final boolean optimizeCSS, final String context,
			final List<String> excludedModules) throws PageDefinitionException {
		this.i18nService = i18nService;
		this.optimizeJS = optimizeJS;
		this.optimizeCSS = optimizeCSS;

		// Get the list of files that don't need to be optimized
		final Set<String> noOptimize = gatherOptimizeExceptions(pageDefinition.moduleList);
		if (log.isDebugEnabled()) {
			log.debug("Not optimizing the following: " + noOptimize);
		}

		// Build the list of modules to look at.
		final List<Module> modules = new ArrayList<Module>(pageDefinition.moduleList);
		if (excludedModules != null) {
			for (final Module module : pageDefinition.moduleList) {
				if (excludedModules.contains(module.getName())) {
					modules.remove(module);
				}
			}
		}

		javascriptFiles = createJavaScriptInclusions(locale, context, modules, noOptimize);
		cssFiles = createStylesheetInclusions(locale, context, modules, noOptimize);
		messages = createMessages(modules, locale);
	}

	private final Messages createMessages(final List<Module> modules, Locale locale) {
		final Locale defaultLocale = new Locale("en");
		final Messages msg = i18nService.createMessages(locale);
		for (final Module module : modules) {
			final String namespace = module.getNamespace();
			addMessages(module.getTranslationsMap(defaultLocale), namespace, msg);
			addMessages(module.getTranslationsMap(locale), namespace, msg);
		}
		return msg;
	}

	private final void addMessages(final Properties props, String namespace, final Messages msg) {
		if (props != null) {
			if (namespace.length() > 0 && !namespace.endsWith(".")) {
				namespace += ".";
			}
			for (final Object key : props.keySet()) {
				msg.put(namespace + key, props.getProperty((String) key));
			}
		}
	}

	/**
	 * @return The map of javascript files to include for this page (key = group, value = javascript files)
	 */
	public Map<String, List<String>> getJavascriptInclusions() {
		return javascriptFiles;
	}

	/**
	 * @return The map of CSS files to include for this page (key = group, value = CSS files)
	 */
	public Map<String, List<String>> getCSSInclusions() {
		return cssFiles;
	}

	/**
	 * @return The translation messages.
	 */
	public Messages getMessages() {
		return messages;
	}

	/**
	 * Create the JavaScript inclusions by concatenating and compressing.
	 */
	private final Map<String, List<String>> createJavaScriptInclusions(final Locale locale, final String context,
			final List<Module> modules, final Set<String> noOptimize) throws PageDefinitionException {
		if (modules != null && modules.size() > 0) {
			final Map<String, List<String>> result = new HashMap<String, List<String>>();

			final Map<String, List<Pair<Module, String>>> files = gatherFiles(modules, true);

			if (optimizeJS) {
				for (final String group : files.keySet()) {
					final List<Pair<Module, String>> groupFiles = files.get(group);
					if (groupFiles == null || groupFiles.size() == 0) {
						continue;
					}

					List<String> groupResultFiles = result.get(group);
					if (groupResultFiles == null) {
						groupResultFiles = new ArrayList<String>();
						result.put(group, groupResultFiles);
					}

					final List<Pair<Module, String>> todo = new ArrayList<Pair<Module, String>>();

					for (final Pair<Module, String> groupFile : groupFiles) {
						if (!noOptimize.contains(groupFile.second)) {
							todo.add(groupFile);
						}
					}
					groupFiles.removeAll(todo);

					compileJavaScript(locale, context, groupResultFiles, todo, optimizeJS);
				}
			}

			// The ones that are left now, don't need to be optimized. Let's handle them now
			for (final String group : files.keySet()) {
				final List<Pair<Module, String>> todo = files.get(group);
				if (todo == null || todo.size() == 0) {
					continue;
				}

				List<String> groupResultFiles = result.get(group);
				if (groupResultFiles == null) {
					groupResultFiles = new ArrayList<String>();
					result.put(group, groupResultFiles);
				}

				for (final Pair<Module, String> ss : todo) {
					compileJavaScript(locale, context, groupResultFiles, Collections.singletonList(ss), false);
				}
			}

			return result;
		}
		return null;
	}

	private void compileJavaScript(final Locale locale, final String context, final List<String> result,
			final List<Pair<Module, String>> todo, boolean optimizeJS) throws PageDefinitionException {
		final String hash = createFileHash(locale.toString(), todo);
		final String name;
		if (todo.size() == 1) {
			String origName = todo.get(0).second;
			if (origName.contains("/")) {
				origName = origName.substring(origName.lastIndexOf("/") + 1);
			}
			name = hash + "-" + origName + ".js";
		} else {
			name = hash + ".js";
		}

		final String generatedName = context + "resources/generated/" + name;
		if (result.contains(generatedName)) {
			return;
		}

		final File hashFile = new File(UICore.getCacheGeneratedDirectory(), name);
		if (!hashFile.exists()) {
			log.debug("Cache miss: generating javascript file '" + name + "' now.");

			try {
				final FileOutputStream fos = new FileOutputStream(hashFile);
				try {
					final List<Module> moduleList = new ArrayList<Module>();
					final Set<String> externs = new HashSet<String>();
					final List<String> jss = new ArrayList<String>();
					for (final Pair<Module, String> p : todo) {
						jss.add(p.second);
						moduleList.add(p.first);
						final String ext = p.first.getExterns();
						if (ext != null) {
							externs.add(ext);
						}
					}

					final String translations = createJavaScriptI18n(moduleList, locale);

					if (optimizeJS) {
						JavaScriptOptimizer.optimizeJavaScripts(fos, context, jss, externs, translations);
					} else {
						IOUtils.write(translations, fos);
						JavaScriptOptimizer.concatenateFiles(jss, fos, context);
					}

					// Add the hash file to the result
					result.add(generatedName);
				} catch (Exception e) {
					log.error("An error occurred while writing the compressed hash file.", e);
					hashFile.delete();
					throw new PageDefinitionException("Error while optimizing JavaScript.", e);
				} finally {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
			} catch (FileNotFoundException e) {
				log.error("Unable to write the compressed hash file.", e);
			}

		} else {
			log.debug("Cache hit: generated javascript file '" + name + "' already exists.");
			result.add(generatedName);
		}
	}

	/**
	 * Creates the JavaScript code to register all the translations.
	 */
	private final String createJavaScriptI18n(final List<Module> moduleList, final Locale locale) {
		final StringBuilder buf = new StringBuilder("");
		final StringBuilder keys = new StringBuilder("[");
		final StringBuilder values = new StringBuilder("[");

		for (final Module module : moduleList) {
			final String namespace = module.getNamespace();
			final Map<String, String> translations = new HashMap<String, String>();
			// First get everything for English
			final Properties defaultProps = module.getTranslationsMap(new Locale("en"));
			if (defaultProps != null) {
				for (final Object key : defaultProps.keySet()) {
					final String k = ((namespace != null && !namespace.isEmpty()) ? namespace + "." : "")
							+ (String) key;
					translations.put(k, defaultProps.getProperty((String) key));
				}
			}
			// Now get the real language (added later so has priority).
			final Properties props = module.getTranslationsMap(locale);
			if (props != null) {
				for (final Object key : props.keySet()) {
					final String k = ((namespace != null && !namespace.isEmpty()) ? namespace + "." : "")
							+ (String) key;
					translations.put(k, props.getProperty((String) key));
				}
			}

			if (translations.size() > 0) {
				// Use the messages to allow overrides from
				final Messages messages = i18nService.createMessages(locale, translations);
				buf.append("core.i18n.add(\"").append(namespace).append("\", ");

				keys.setLength(1);
				values.setLength(1);

				final Set<String> keySet = translations.keySet();
				for (final String key : keySet) {
					final String k = (namespace != null && !namespace.isEmpty()) ? key
							.substring(namespace.length() + 1) : key;
					keys.append("\"").append(k).append("\", ");
					values.append("\"").append(messages.get(key, null).replaceAll("\"", "'")).append("\", ");
				}
				values.setLength(values.length() - 2);
				keys.setLength(keys.length() - 2);

				buf.append(keys).append("], ").append(values).append("]);");
			}
		}
		return buf.toString();
	}

	/**
	 * Gathers the files (javascript or stylesheets) from the list of modules. The result is split per module group. The
	 * values are a combination of the module and the specific file.
	 */
	private final Map<String, List<Pair<Module, String>>> gatherFiles(final List<Module> modules, boolean javascript) {
		final Map<String, List<Pair<Module, String>>> ret = new HashMap<String, List<Pair<Module, String>>>();
		for (final Module module : modules) {
			String group = module.getGroup();
			if (group == null) {
				group = "";
			}

			final List<String> files = javascript ? module.getJavascripts() : module.getStylesheets();
			if (files == null || files.size() == 0) {
				continue;
			}

			List<Pair<Module, String>> groupFiles = ret.get(group);
			if (groupFiles == null) {
				groupFiles = new ArrayList<Pair<Module, String>>();
				ret.put(group, groupFiles);
			}

			for (final String file : files) {
				groupFiles.add(new Pair<Module, String>(module, file));
			}
		}

		return ret;
	}

	/**
	 * Crates the hash filename by gathering all the needed information (name = lastmodified) from the files.
	 */
	private String createFileHash(final String extra, List<Pair<Module, String>> files) {
		List<Pair<Module, String>> sortedFiles = new ArrayList<Pair<Module, String>>(files);
		if (sortedFiles.size() > 1) {
			Collections.sort(sortedFiles, new Comparator<Pair<Module, String>>() {
				@Override
				public int compare(Pair<Module, String> o1, Pair<Module, String> o2) {
					return o1.second.compareTo(o2.second);
				}
			});
		}
		StringBuilder buf = new StringBuilder(extra);
		for (final Pair<Module, String> m : sortedFiles) {
			buf.append(m.second).append(m.first.getLastmodified());
		}
		return DigestUtils.shaHex(buf.toString());
	}

	/**
	 * Helper method to gather all the files that don't need to be optimized.
	 */
	private final Set<String> gatherOptimizeExceptions(final List<Module> modules) {
		final Set<String> noOptimize = new HashSet<String>();
		for (final Module module : modules) {
			noOptimize.addAll(module.getOptimizeExceptions());
		}
		return noOptimize;
	}

	/**
	 * Creates the set of stylesheet inclusions by running over all the modules, requesting their list of stylesheets.
	 * It compiles LESS code and takes stylesheets toghether where possible.
	 */
	private final Map<String, List<String>> createStylesheetInclusions(final Locale locale, final String context,
			final List<Module> moduleList, final Set<String> noOptimize) throws PageDefinitionException {
		if (moduleList != null && moduleList.size() > 0) {
			final Map<String, List<String>> result = new HashMap<String, List<String>>();

			final Map<String, List<Pair<Module, String>>> files = gatherFiles(moduleList, false);

			if (optimizeCSS) {
				for (final String group : files.keySet()) {
					final List<Pair<Module, String>> groupFiles = files.get(group);
					if (groupFiles == null || groupFiles.size() == 0) {
						continue;
					}

					List<String> groupResultFiles = result.get(group);
					if (groupResultFiles == null) {
						groupResultFiles = new ArrayList<String>();
						result.put(group, groupResultFiles);
					}

					final List<Pair<Module, String>> todo = new ArrayList<Pair<Module, String>>();

					for (final Pair<Module, String> groupFile : groupFiles) {
						if (!noOptimize.contains(groupFile.second)) {
							todo.add(groupFile);
						}
					}
					groupFiles.removeAll(todo);

					compileCSS(locale, context, groupResultFiles, todo, optimizeCSS);
				}
			}

			// The ones that are left now, don't need to be optimized. Let's handle them now
			for (final String group : files.keySet()) {
				final List<Pair<Module, String>> todo = files.get(group);
				if (todo == null || todo.size() == 0) {
					continue;
				}

				List<String> groupResultFiles = result.get(group);
				if (groupResultFiles == null) {
					groupResultFiles = new ArrayList<String>();
					result.put(group, groupResultFiles);
				}

				for (final Pair<Module, String> ss : todo) {
					compileCSS(locale, context, groupResultFiles, Collections.singletonList(ss), false);
				}
			}

			return result;
		}
		return null;
	}

	/**
	 * Compiles a list of CSS/Less files into I hashed file.
	 */
	protected void compileCSS(final Locale locale, final String context, final List<String> result,
			final List<Pair<Module, String>> todo, boolean optimizeCSS) throws PageDefinitionException {
		final String hash = createFileHash(locale.toString(), todo);
		final String name;
		if (todo.size() == 1) {
			String origName = todo.get(0).second;
			if (origName.contains("/")) {
				origName = origName.substring(origName.lastIndexOf("/") + 1);
			}
			name = hash + "-" + origName + ".css";
		} else {
			name = hash + ".css";
		}
		final File hashFile = new File(UICore.getCacheGeneratedDirectory(), name);
		if (!hashFile.exists()) {
			log.debug("Cache miss: generating stylesheet file '" + name + "' now.");
			// Now we render the remaining files in the 'todo' list.
			final Set<String> stylesheets = new HashSet<String>();
			stylesheets.add(GLOBAL_LESS);

			final Map<String, String> modulePaths = new HashMap<String, String>();
			for (final Pair<Module, String> m : todo) {
				modulePaths.put(m.second, context + "resources/" + m.first.getUrlPath());
				stylesheets.add(m.second);
			}

			try {
				final FileOutputStream fos = new FileOutputStream(hashFile);
				try {
					CSSCompiler.compile(fos, stylesheets, modulePaths, context, optimizeCSS);
					result.add(context + "resources/generated/" + name);
				} catch (IOException e) {
					log.error("An error occurred while writing the compressed hash file.", e);
					hashFile.delete();
					throw new PageDefinitionException("Error while optimizing CSS.", e);
				} finally {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
			} catch (FileNotFoundException e) {
				log.error("Unable to write the compressed hash file.", e);
				throw new RuntimeException(e);
			}

		} else {
			log.debug("Cache hit: generated stylesheet file '" + name + "' already exists.");
			result.add(context + "resources/generated/" + name);
		}
	}
}
