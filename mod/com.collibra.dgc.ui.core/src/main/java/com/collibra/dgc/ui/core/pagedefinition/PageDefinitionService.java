/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.FileMonitor;
import com.collibra.dgc.core.util.FileMonitor.EEvent;
import com.collibra.dgc.core.util.FileMonitor.IExecutor;
import com.collibra.dgc.ui.core.modules.ModuleService;
import com.collibra.dgc.ui.core.pagedefinition.model.MatchingRule;
import com.collibra.dgc.ui.core.pagedefinition.model.ModuleConfiguration;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinition;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionException;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionParser;
import com.collibra.dgc.ui.core.pagedefinition.model.Region;

/**
 * The service that will keep track of all the page definitions and find the best fit when requested.
 * @author dieterwachters
 */
@Service
public class PageDefinitionService implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(PageDefinitionService.class);
	private static final String DEFAULT = "[DEFAULT]";

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ConfigurationService configService;
	@Autowired
	private RepresentationService representationService;

	private FileAlterationMonitor fileMonitor;

	// The list of all page definitions to handle.
	private final List<PageDefinition> pageDefinitions = new ArrayList<PageDefinition>();
	// The map for mapping the filename to the page definitions.
	private final Map<String, PageDefinition> nameMappings = new HashMap<String, PageDefinition>();
	// Definitions that don't had a definition XML file, but match a normal (page) module.
	private final Map<String, PageDefinition> undefinedDefinitions = new HashMap<String, PageDefinition>();

	// Keep the page definitions per path and per type to quickly find the right one depending on the inputs.
	private final Map<String, Map<String, PageDefinition>> pathMappings = new HashMap<String, Map<String, PageDefinition>>();
	// Maps the id to the page definition
	private final Map<String, PageDefinition> idMapping = new HashMap<String, PageDefinition>();

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	// private InitializeThread initializeThread;

	/**
	 * Find the page definition for this path. The sub part defines that we immediately want to start somewhere deeper
	 * in the page definition structure.
	 */
	public final PageData createPageData(final String path, final String subPart, final boolean bare)
			throws PageDefinitionException {
		// TODO how to do caching for subParts?

		String p = normalizePath(path);

		String id = null;
		if (p.contains("/")) {
			// Check if the last part of the path is a UUID.
			id = p.substring(p.lastIndexOf("/") + 1);
			try {
				UUID.fromString(id);
				p = p.substring(0, p.lastIndexOf("/"));
				// Find a page definition for the given path and resource
				PageDefinition def = doFindPageDefinition(p, id);
				if (def != null) {
					def = def.getSubPart(subPart);
					def.initialize(moduleService, configService);
					return new PageData(def, id);
				}
			} catch (IllegalArgumentException e) {
				// last part of the UI is not a UUID, so never mind.
				id = null;
			}
		}
		// Now only find a page definition for the given path.
		PageDefinition def = doFindPageDefinition(p, null);
		if (def != null) {
			def = def.getSubPart(subPart);
			def.initialize(moduleService, configService);
			return new PageData(def, id);
		}

		if (p.startsWith("module/")) {
			p = p.substring(7);
		} else {
			p = "pages/" + p;
		}

		def = undefinedDefinitions.get(p);
		if (def == null) {
			def = new PageDefinition("dummy", null);
			final ModuleConfiguration page = new ModuleConfiguration(def, def, bare ? "pages/shared/bare"
					: "pages/shared/page");
			def.setModuleConfiguration(page);
			final Region region = new Region(def, page, "content");
			page.addRegion(region);
			final ModuleConfiguration moduleConfig = new ModuleConfiguration(def, def, p);
			region.addModuleConfiguration(moduleConfig);
			undefinedDefinitions.put(p, def);
		}

		try {
			def = def.getSubPart(subPart);
			def.initialize(moduleService, configService);
		} catch (Exception e) {
			log.error("The page module '" + p + "' not found.", e);
			return null;
		}
		return new PageData(def, id);
	}

	/**
	 * Find the page definition for this path.
	 */
	public final PageData createPageData(final String path) throws PageDefinitionException {
		return createPageData(path, null, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		initialize();
	}

	/**
	 * Initialize the page definitions by reading all the available ones.
	 */
	private final void initialize() {
		lock.writeLock().lock();
		try {
			try {
				if (fileMonitor != null) {
					fileMonitor.stop();
				}
				fileMonitor = FileMonitor.startFileMonitor(Application.PAGE_DEFINITIONS_DIR, new IExecutor() {
					@Override
					public void run(final File file, final EEvent event) {
						if (event == EEvent.CHANGE) {
							final PageDefinition existing = nameMappings.remove(file.getAbsolutePath());
							readPageDefinition(file);
							if (existing != null) {
								pageDefinitions.remove(existing);
							}
						} else if (event == EEvent.DELETE) {
							final PageDefinition existing = nameMappings.remove(file.getAbsolutePath());
							if (existing != null) {
								pageDefinitions.remove(existing);
							}
						} else if (event == EEvent.CREATE) {
							readPageDefinition(file);
						}
						recalculateMappings();
						log.info("A change was detected and handled in the page definitions ('"
								+ file.getAbsolutePath() + "') in the user home directory.");
					}
				}, 60000, Arrays.asList(EEvent.CHANGE, EEvent.DELETE, EEvent.CREATE));
			} catch (Exception e) {
				log.error("Unable to start file monitor for page definitions.", e);
			}

			// Initialize

			// First we load all the ones from the classpath (fallbacks).
			try {
				final Resource[] resources = applicationContext.getResources("classpath*:page-definitions/*.xml");
				for (final Resource resource : resources) {
					try {
						final PageDefinition def = PageDefinitionParser.parsePageDefinition(resource.getInputStream());
						def.setBuiltIn(true);
						pageDefinitions.add(def);
					} catch (IOException e) {
						log.error("Unable to read the internal file '" + resource.getFilename() + "'.", e);
					} catch (PageDefinitionException e) {
						log.error("Error while parsing the page definition file '" + resource.getFilename() + "'.", e);
					}
				}
			} catch (IOException e1) {
				log.error("Unable to find out-of-the-box page definitions.");
			}

			// Now we override them with the ones from the user home directory.
			final File[] defs = Application.PAGE_DEFINITIONS_DIR.listFiles();
			if (defs != null) {
				for (final File def : defs) {
					if (def.getName().endsWith(".xml")) {
						readPageDefinition(def);
					}
				}
			}
			recalculateMappings();
			// // Start a new initialize thread
			// initializeThread = new InitializeThread();
			// initializeThread.start();
		} finally {
			lock.writeLock().unlock();
		}
	}

	private final PageDefinition readPageDefinition(final File def) {
		try {
			final PageDefinition pd = PageDefinitionParser.parsePageDefinition(new FileInputStream(def));
			pageDefinitions.add(pd);
			nameMappings.put(def.getAbsolutePath(), pd);
		} catch (FileNotFoundException e) {
			log.error("Unable to read the file '" + def.getAbsolutePath() + "'.", e);
		} catch (PageDefinitionException e) {
			log.error("Error while parsing the page definition file '" + def.getAbsolutePath() + "'.", e);
		}
		return null;
	}

	private final void recalculateMappings() {
		lock.writeLock().lock();
		try {
			pathMappings.clear();
			idMapping.clear();

			for (final PageDefinition pageDefinition : pageDefinitions) {
				final List<MatchingRule> rules = pageDefinition.getMatchingRules();
				for (final MatchingRule rule : rules) {
					Map<String, PageDefinition> pathDefs = pathMappings.get(rule.getPath());
					if (pathDefs == null) {
						pathDefs = new HashMap<String, PageDefinition>();
						pathMappings.put(normalizePath(rule.getPath()), pathDefs);
					}

					final String type = rule.getType() == null ? DEFAULT : rule.getType();
					pathDefs.put(type, pageDefinition);
				}
				idMapping.put(pageDefinition.getID(), pageDefinition);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Find the page definition for the given path and resource id (optional).
	 */
	private final PageDefinition doFindPageDefinition(final String path, final String resourceID) {
		lock.readLock().lock();
		try {
			final Map<String, PageDefinition> pathDefs = pathMappings.get(path);
			if (pathDefs != null) {
				if (resourceID == null && pathDefs.get(DEFAULT) != null) {
					return pathDefs.get(DEFAULT);
				} else if (resourceID != null) {
					// First we match at the exact resource id.
					if (pathDefs.containsKey(resourceID)) {
						return pathDefs.get(resourceID);
					}

					final Set<String> doneTypes = new HashSet<String>();
					final Representation rep = representationService.findRepresentationByResourceId(resourceID);
					if (rep != null) {
						Meaning type = rep.getMeaning();
						String trid = type.getId();
						while (!doneTypes.contains(trid)) {
							if (pathDefs.containsKey(trid)) {
								return pathDefs.get(trid);
							}
							doneTypes.add(trid);
							if (type instanceof Concept) {
								type = ((Concept) type).getType();
								trid = type.getId();
							} else {
								break;
							}
						}
					} else {
						final Vocabulary voc = representationService.findVocabularyByResourceId(resourceID);
						if (voc != null) {
							ObjectType type = voc.getType();
							String trid = type.getId();
							while (!doneTypes.contains(trid)) {
								if (pathDefs.containsKey(trid)) {
									return pathDefs.get(trid);
								}
								doneTypes.add(trid);
								type = ((Concept) type).getType();
								trid = type.getId();
							}
						}
					}
				}
			}
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Normalizing the path by stripping of the slashes at the beginning and end.
	 */
	private static final String normalizePath(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	// /**
	// * Thread that will start initializing the page definitions in the background to improve user experience.
	// * @author dieterwachters
	// */
	// private class InitializeThread extends Thread {
	// public boolean stop = false;
	//
	// /**
	// * Constructor
	// */
	// public InitializeThread() {
	// super("PageDefinition Initialize Thread");
	// }
	//
	// @Override
	// public void run() {
	// for (final PageDefinition def : idMapping.values()) {
	// def.initialize(moduleService);
	// if (stop) {
	// return;
	// }
	// }
	// }
	// }
}
