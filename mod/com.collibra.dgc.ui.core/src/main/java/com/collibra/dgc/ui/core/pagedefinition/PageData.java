/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition;

import java.util.List;
import java.util.Stack;

import com.collibra.dgc.ui.core.modules.Module;
import com.collibra.dgc.ui.core.pagedefinition.model.ModuleConfiguration;
import com.collibra.dgc.ui.core.pagedefinition.model.Node;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinition;
import com.collibra.dgc.ui.core.pagedefinition.model.Region;

/**
 * A structure to keep all the data needed to represent a page. This will combine the page definition data with the
 * actual module data.
 * @author dieterwachters
 */
public class PageData {
	private final PageDefinition pageDefinition;
	private final String resourceId;
	private final Stack<Node> currentNode = new Stack<Node>();

	/**
	 * Constructor
	 * @param pageDefintion The page definition for this page
	 * @param resourceId The (optional) resource id of the resource to show on this page.
	 */
	public PageData(final PageDefinition pageDefintion, final String resourceId) {
		this.pageDefinition = pageDefintion;
		this.resourceId = resourceId;
		currentNode.push(pageDefintion.getModuleConfiguration());
	}

	/**
	 * Enter the given region. This should be called before rendering the modules of this region. This will change the
	 * current state of this page data object.
	 * @param region The region to enter.
	 */
	public void enterRegion(final String region) {
		final ModuleConfiguration module = (ModuleConfiguration) currentNode.peek();
		final Region r = module.getRegion(region);
		if (r == null) {
			throw new RuntimeException("Unknown region '" + region + "' for module '" + module.getName() + "'");
		}
		currentNode.push(r);
	}

	/**
	 * Leave the current region. This should be called after rendering the modules of the current region. This will
	 * change the current state of this page data object.
	 */
	public void leaveRegion() {
		currentNode.pop();
	}

	/**
	 * Returns the template path of the currently active module. Note: this requires a module to be on the top of the
	 * stack.
	 * @return The full path to the Velocity template for this current module.
	 */
	public String getModuleTemplate() {
		final ModuleConfiguration module = (ModuleConfiguration) currentNode.peek();
		return getModuleTemplate(module.getName());
	}

	/**
	 * Retrieve the path to the given region inside the current module. This path can be used for lazily rendering parts
	 * of this page definition.
	 */
	public String getRegionPath(String region) {
		final ModuleConfiguration module = (ModuleConfiguration) currentNode.peek();
		if (module != null) {
			final Region r = module.getRegion(region);
			return r == null ? null : r.getPath();
		}
		return null;
	}

	/**
	 * Returns the template path of the given module.
	 * @param module The module to get the template from
	 * @return The full path to the Velocity template for this module.
	 */
	public final String getModuleTemplate(String module) {
		final Module m = pageDefinition.getModuleMap().get(module);
		return m == null ? null : m.getTemplate();
	}

	/**
	 * Enter the given module. This must be called before rendering this module. This changes the state of this page
	 * data object.
	 * @param module The module (configuration) to enter.
	 */
	public void enterModule(final ModuleConfiguration module) {
		currentNode.push(module);
	}

	/**
	 * Leave the current module. This must be called after rendering this module. This changes the state of this page
	 * data object.
	 */
	public void leaveModule() {
		currentNode.pop();
	}

	/**
	 * Returns the property of the currently active module.
	 * @param property The name of the property to retrieve.
	 * @return The property map or the String value if this is a leaf.
	 */
	public Object getProperty(String property) {
		if (currentNode.peek() instanceof ModuleConfiguration) {
			return ((ModuleConfiguration) currentNode.peek()).getProperty(property);
		}
		return null;
	}

	/**
	 * Returns the property of the given module.
	 * @param module The full name of the module to get the property from.
	 * @param property The name of the property to retrieve.
	 * @return The property map or the String value if this is a leaf.
	 */
	public Object getProperty(String module, String name) {
		// Next look at a property for this specific module
		final Module m = pageDefinition.getModuleMap().get(module);
		if (m == null) {
			return null;
		}
		Object property = m.getHierarchicalConfiguration().get(name);
		if (property == null) {
			// As last, check all the static properties as this might be global.
			property = pageDefinition.getStaticProperties().get(name);
		}
		return property;
	}

	/**
	 * Returns the property of the currently active module as an integer.
	 * @param property The name of the property to retrieve.
	 * @return The Integer value or null if this is not an integer value.
	 */
	public Integer getIntegerProperty(String property) {
		if (currentNode.peek() instanceof ModuleConfiguration) {
			return ((ModuleConfiguration) currentNode.peek()).getIntegerProperty(property);
		}
		return null;
	}

	/**
	 * Returns the property of the currently active module as an boolean.
	 * @param property The name of the property to retrieve.
	 * @return The Boolean value or null if this is not a boolean value.
	 */
	public Boolean getBooleanProperty(String property) {
		if (currentNode.peek() instanceof ModuleConfiguration) {
			return ((ModuleConfiguration) currentNode.peek()).getBooleanProperty(property);
		}
		return null;
	}

	/**
	 * Returns the property of the currently active module as an double.
	 * @param property The name of the property to retrieve.
	 * @return The Double value or null if this is not a double value.
	 */
	public Double getDoubleProperty(String property) {
		if (currentNode.peek() instanceof ModuleConfiguration) {
			return ((ModuleConfiguration) currentNode.peek()).getDoubleProperty(property);
		}
		return null;
	}

	/**
	 * Returns the list of modules for the currently active region. Note: for this to work a region must be at the top
	 * of the stack.
	 * @return The list of modules for this region.
	 */
	public List<ModuleConfiguration> getModules() {
		final Region region = (Region) currentNode.peek();
		return region.getModuleConfigurations();
	}

	/**
	 * @return The page definition object
	 */
	public PageDefinition getPageDefinition() {
		return pageDefinition;
	}

	/**
	 * @return The resource id for this page or null if not specified.
	 */
	public String getResourceId() {
		return resourceId;
	}
}
