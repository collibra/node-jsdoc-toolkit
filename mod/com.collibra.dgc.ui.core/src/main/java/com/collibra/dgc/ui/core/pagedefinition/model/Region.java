/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a region in the page definition tree.
 * @author dieterwachters
 */
public class Region extends Node {
	protected List<ModuleConfiguration> modules;

	/**
	 * Constructor
	 * @param name The name of this region.
	 * @param modules The list of modules in this region.
	 */
	public Region(final PageDefinition pageDefinition, final Node parent, final String name,
			final List<ModuleConfiguration> modules) {
		super(pageDefinition, parent, name);
		this.modules = modules;
	}

	/**
	 * Constructor
	 * @param name The name of this region.
	 */
	public Region(final PageDefinition pageDefinition, final Node parent, final String name) {
		super(pageDefinition, parent, name);
	}

	/**
	 * Add a module configuration to the region.
	 * @param module The module configuration to add.
	 */
	public void addModuleConfiguration(final ModuleConfiguration module) {
		if (modules == null) {
			modules = new ArrayList<ModuleConfiguration>();
		}
		modules.add(module);
	}

	/**
	 * Get the list of module configurations in this region.
	 * @return The list of module configurations in this region.
	 */
	public List<ModuleConfiguration> getModuleConfigurations() {
		return this.modules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.pagedefinition.model.Node#getPath()
	 */
	@Override
	public String getPath() {
		return (parent == null ? "" : parent.getPath()) + "|region|" + name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.pagedefinition.model.Node#findMatchingNode(java.lang.String)
	 */
	@Override
	public Node findMatchingNode(String path) throws PageDefinitionException {
		if (path == null || path.isEmpty()) {
			return this;
		}

		String firstPiece = path;
		if (firstPiece.startsWith("|")) {
			firstPiece = firstPiece.substring(1);
		}
		String secondPiece = null;
		if (firstPiece.contains("|")) {
			secondPiece = firstPiece.substring(firstPiece.indexOf("|") + 1);
			firstPiece = firstPiece.substring(0, firstPiece.indexOf("|"));
		}

		final int index = Integer.parseInt(firstPiece);
		if (index >= 0 && index < modules.size()) {
			final ModuleConfiguration module = modules.get(index);
			return module.findMatchingNode(secondPiece);
		}
		throw new PageDefinitionException("No child node found for path '" + path + "' in node '" + name + "'.");
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
			if (modules != null) {
				for (final ModuleConfiguration module : modules) {
					module.accept(visitor);
				}
			}
		}
	}
}
