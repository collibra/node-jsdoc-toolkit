/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.util.Map;

/**
 * Represents a node in the page definition tree with a name and properties.
 * @author dieterwachters
 */
public abstract class PropertiesNode extends Node {
	protected Map<String, Object> properties;

	/**
	 * Constructor
	 */
	public PropertiesNode(final PageDefinition pageDefinition, final Node parent, String name,
			final Map<String, Object> properties) {
		super(pageDefinition, parent, name);
		this.properties = properties;
	}

	/**
	 * Constructor
	 */
	public PropertiesNode(final PageDefinition pageDefinition, final Node parent, String name) {
		super(pageDefinition, parent, name);
	}

	/**
	 * Set the list of sub-properties.
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Get the properties map.
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Retrieve the property with the given name.
	 */
	public Object getProperty(final String name) {
		return properties == null ? null : properties.get(name);
	}
}
