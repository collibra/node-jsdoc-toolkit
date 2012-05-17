/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a module in a page definition tree.
 * @author dieterwachters
 */
public class ModuleConfiguration extends PropertiesNode {
	private static final Logger log = LoggerFactory.getLogger(ModuleConfiguration.class);

	private List<Region> regions;
	private Map<String, Region> regionMap;

	/**
	 * Constructor
	 */
	public ModuleConfiguration(final PageDefinition pageDefinition, final Node parent, final String name,
			Map<String, Object> properties, final List<Region> regions) {
		super(pageDefinition, parent, name, properties);
		this.regions = regions;
	}

	/**
	 * Constructor
	 */
	public ModuleConfiguration(final PageDefinition pageDefinition, final Node parent, final String name) {
		super(pageDefinition, parent, name);
	}

	/**
	 * Add a region to this module
	 * @param region The new region to add.
	 */
	public void addRegion(final Region region) {
		if (regions == null) {
			regions = new ArrayList<Region>();
		}
		regions.add(region);
	}

	/**
	 * Set the list of regions for this module.
	 * @param regions The region for this module.
	 */
	public void setRegions(final List<Region> regions) {
		this.regions = regions;
	}

	/**
	 * Retrieve all the available regions.
	 * @return All the available regions.
	 */
	public List<Region> getRegions() {
		return regions;
	}

	/**
	 * Retrieve the region map with the given name.
	 * @param name The name of the region to get.
	 * @return The found region or null if nothing was found.
	 */
	public Region getRegion(final String name) {
		if (regionMap == null && regions != null) {
			regionMap = new HashMap<String, Region>();
			for (final Region region : regions) {
				regionMap.put(region.name, region);
			}
		}
		return regionMap == null ? null : regionMap.get(name);
	}

	@Override
	public String getPath() {
		if (parent instanceof Region) {
			int index = ((Region) parent).modules.indexOf(this);
			return parent.getPath() + "|" + index;
		}
		return "";
	}

	@Override
	public Object getProperty(String name) {
		// First look for a dynamic property as defined in the page definition
		Object property = super.getProperty(name);
		if (property == null) {
			// Next look at a property for this specific module
			if (pageDefinition.getModuleMap().containsKey(getName())) {
				property = pageDefinition.getModuleMap().get(getName()).getHierarchicalConfiguration().get(name);
			}
			if (property == null) {
				// As last, check all the static properties as this might be global.
				property = pageDefinition.getStaticProperties().get(name);
			}
		}
		return property;
	}

	public Integer getIntegerProperty(String name) {
		final Object value = getProperty(name);
		if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				log.warn("Found property value '" + (String) value + "' for '" + name
						+ "' is not a valid integer. Null will be returned.");
			}
		}
		return null;
	}

	public Boolean getBooleanProperty(String name) {
		final Object value = getProperty(name);
		if (value instanceof String) {
			try {
				return Boolean.parseBoolean((String) value);
			} catch (Exception e) {
				log.warn("Found property value '" + (String) value + "' for '" + name
						+ "' is not a valid boolean. Null will be returned.");
			}
		}
		return null;
	}

	public Double getDoubleProperty(String name) {
		final Object value = getProperty(name);
		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (Exception e) {
				log.warn("Found property value '" + (String) value + "' for '" + name
						+ "' is not a valid double. Null will be returned.");
			}
		}
		return null;
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

		if (firstPiece.equals("region") && secondPiece != null) {
			String regionName = secondPiece;
			String rest = null;
			if (regionName.contains("|")) {
				rest = regionName.substring(regionName.indexOf("|") + 1);
				regionName = regionName.substring(0, regionName.indexOf("|"));
			}
			final Region region = getRegion(regionName);
			if (region != null) {
				return region.findMatchingNode(rest);
			}
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
			if (regions != null) {
				for (final Region region : regions) {
					region.accept(visitor);
				}
			}
		}
	}
}
